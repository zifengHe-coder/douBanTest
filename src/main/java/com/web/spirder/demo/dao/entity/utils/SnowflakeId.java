package com.web.spirder.demo.dao.entity.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

@Component
public class SnowflakeId {
    private static final Logger logger = LoggerFactory.getLogger(SnowflakeId.class);
    /**
     * 开始时间戳
     */
    private static final long TWEPOCH=1420041600000L;

    /**
     * 机器id所占位数
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 数据表示id所站位数
     */
    private static final long DATA_CENTER_ID_BITS = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private static final long MAX_WORKER_ID = -1L ^(-1L<<WORKER_ID_BITS);

    /**
     * 支持的最大数据标识id，结果是31
     *
     */
    private static final long MAX_DATA_CENTER_ID = -1L^(-1L<<DATA_CENTER_ID_BITS);

    /**
     * 序列在id中占的位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 机器ID向左移12位
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

    /**
     * 工作机器ID(0~31)
     */
    private static long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private static long datacenterId;
    /**
     * 工作机器ID(0~31) 配置值
     */
    //@Value("${snowflake.workerId}")
    //private long workerIdValue;

    /**
     * 数据中心ID(0~31) 配置值
     */
    //@Value("${snowflake.datacenterId}")
    //private long datacenterIdValue;

    /**
     * 毫秒内序列(0~4095)
     */
    private static long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private static long lastTimestamp = -1L;

    //==============================Constructors=====================================
    @PostConstruct
    public void init() {
	   /* //把临时变量获取到的值设置到static变量中
        String workerIdStr = CacheUtils.orgGet(CacheUtils.ORGANIZATION_WORKER_ID)==null?"0":CacheUtils.orgGet(CacheUtils.ORGANIZATION_WORKER_ID);
		this.workerId = Long.parseLong(workerIdStr);
		this.datacenterId = getDatacenterId(MAX_DATA_CENTER_ID);

		// 因为当前workerId和datacenterId位数都是5位，所以最大只能32个
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATA_CENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }*/
    }

    public SnowflakeId() {
        //把临时变量获取到的值设置到static变量中
        String workerIdStr = "0";
        this.workerId = Long.parseLong(workerIdStr);
        this.datacenterId = getDatacenterId(MAX_DATA_CENTER_ID);
    }

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    private SnowflakeId(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATA_CENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }
        SnowflakeId.workerId = workerId;
        SnowflakeId.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public static synchronized long getId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected static long timeGen() {
        return System.currentTimeMillis();
    }


    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return getId();
    }

    /**
     * <p>
     * 数据标识id部分
     * </p>
     */
    protected static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                id = ((0x000000FF & (long) mac[mac.length - 1])
                        | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (maxDatacenterId + 1);
            }
        } catch (Exception e) {
            logger.info(" getDatacenterId:{}", e.getMessage());
        }
        return id;
    }

    /**
     * <p>
     * 获取 maxWorkerId
     * </p>
     */
    protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuffer mpid = new StringBuffer();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            /*
             * GET jvmPid
             */
            mpid.append(name.split("@")[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    public static long getWorkerId(){
        return workerId;
    }
    public static long getDatacenterId(){
        return datacenterId;
    }
    //==============================Test=============================================
    /* *//** 测试 *//*
    public static void main(String[] args) {
    	long start = System.currentTimeMillis();
    	 datacenterId = getDatacenterId(MAX_DATA_CENTER_ID);
         workerId = getMaxWorkerId(datacenterId, MAX_WORKER_ID);
        //SnowflakeId idWorker = new SnowflakeId();
        for (int i = 0; i < 1000; i++) {
            long id = getId();
            //logger.info(Long.toBinaryString(id));
            logger.info(id);
        }
        logger.info(datacenterId);
        logger.info(workerId);

    	logger.info("Time taken to Generate id " + (System.currentTimeMillis() - start) + "ms");

    }*/
}
