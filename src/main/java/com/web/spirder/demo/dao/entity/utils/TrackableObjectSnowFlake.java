package com.web.spirder.demo.dao.entity.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idaoben.web.common.entity.Description;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@MappedSuperclass
public abstract class TrackableObjectSnowFlake {
    @Id
    @Column(name = "id")
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId",strategy = "com.web.spirder.demo.dao.entity.utils.SnowflakeId")
    @Description("ID")
    private Long id;

    /** "ID"属性名称 */
    public static final String ID_PROPERTY_NAME = "id";

    /** "创建时间"属性名称 */
    public static final String CREATE_DATE_PROPERTY_NAME = "creationTime";

    /** "最后更新时间"属性名称 */
    public static final String MODIFY_DATE_PROPERTY_NAME = "updateTime";

    @Column(name = "creation_time")
    @Description("创建时间")
    private ZonedDateTime creationTime;

    @Column(name = "update_time")
    @Description("更新时间")
    private ZonedDateTime updateTime;

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    protected void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * updatetime直接设置无用，加入只是为了某些情况可能只需要更新刷新时间，这时需要字段做变动
     * @param updateTime
     */
    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @PrePersist
    protected void fillCreationTime() {
        if(creationTime == null){
            this.creationTime = ZonedDateTime.now();
        }
        this.updateTime = creationTime;
    }

    @PreUpdate
    protected void fillUpdateTime() {
        this.updateTime = ZonedDateTime.now();
    }


    /**
     * @return id的值
     */
    @JsonProperty
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            id的值
     */
    @JsonProperty
    public void setId(Long id) {
        this.id = id;
    }
}
