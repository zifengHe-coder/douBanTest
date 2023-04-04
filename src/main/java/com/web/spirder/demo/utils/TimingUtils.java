package com.web.spirder.demo.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

/**
 * @author hezifeng
 * @create 2023/4/4 15:21
 */
public class TimingUtils {
    private static final int DEFAULT_NESTING_TICK_TOCK = 4;
    private static final ThreadLocal<LinkedList<TickTock>> TIMING_HELPER = ThreadLocal.withInitial(LinkedList::new);

    public TimingUtils() {
    }

    private static TimingUtils.TickTock popRecentTickTock() {
        LinkedList<TimingUtils.TickTock> tickTocks = (LinkedList)TIMING_HELPER.get();
        return (TimingUtils.TickTock)tickTocks.pollLast();
    }

    private static TimingUtils.TickTock pushNewTickTock() {
        LinkedList<TimingUtils.TickTock> tickTocks = (LinkedList)TIMING_HELPER.get();
        TimingUtils.TickTock tickTock = newTickTock();
        tickTocks.push(tickTock);
        return tickTock;
    }

    public static TimingUtils.TickTock newTickTock() {
        return new TimingUtils.TickTock();
    }

    public static TimingUtils.TickTock tick() {
        reset();
        return pushNewTickTock().tick();
    }

    public static TimingUtils.TickTock nestTick() {
        return pushNewTickTock().tick();
    }

    public static void reset() {
        TIMING_HELPER.remove();
    }

    public static TimingUtils.TickTock tock() {
        TimingUtils.TickTock tickTock = popRecentTickTock();
        if (tickTock == null) {
            tickTock = newTickTock().tick();
        }

        return tickTock.tock();
    }

    public static class TickTock {
        private static final float MILLIS_PER_SECONDS = 1000.0F;
        private Instant start;
        private Instant end;

        public TickTock() {
        }

        public TimingUtils.TickTock tick() {
            this.start = Instant.now();
            return this;
        }

        public TimingUtils.TickTock tock() {
            if (this.start == null) {
                throw new IllegalStateException("未调用tick()方法启动计时");
            } else {
                this.end = Instant.now();
                return this;
            }
        }

        public TimingUtils.TickTock reset() {
            this.start = null;
            this.end = null;
            return this;
        }

        public long toMillis() {
            return Duration.between(this.start, this.end).toMillis();
        }

        public float toSeconds() {
            return (float)Duration.between(this.start, this.end).toMillis() / 1000.0F;
        }

        public Instant getStart() {
            return this.start;
        }

        public Instant getEnd() {
            return this.end;
        }
    }
}
