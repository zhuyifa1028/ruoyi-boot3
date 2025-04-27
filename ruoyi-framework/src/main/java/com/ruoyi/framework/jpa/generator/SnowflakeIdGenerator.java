package com.ruoyi.framework.jpa.generator;

public class SnowflakeIdGenerator {

    // 起始时间戳，这里设置为 2025-01-01 00:00:00
    private static final long startTimestamp = 1735660800000L;

    // 机器ID 所占的位数
    private static final long workerIdBits = 5L;
    // 数据中心ID 所占的位数
    private static final long dataCenterIdBits = 5L;
    // 序列号 所占的位数
    private static final long sequenceBits = 12L;

    // 机器ID 最大值
    private static final long maxWorkerId = ~(-1L << workerIdBits);
    // 数据中心ID 最大值
    private static final long maxDataCenterId = ~(-1L << dataCenterIdBits);
    // 序列号 最大值
    private static final long maxSequence = ~(-1L << sequenceBits);

    // 机器ID 向左移位数
    private static final long workerIdShift = sequenceBits;
    // 数据中心ID 向左移位数
    private static final long dataCenterIdShift = sequenceBits + workerIdBits;
    // 时间戳 向左移位数
    private static final long timestampShift = sequenceBits + workerIdBits + dataCenterIdBits;

    // 工作机器 ID
    private final long workerId;
    // 数据中心 ID
    private final long dataCenterId;
    // 序列号
    private long sequence = 0L;
    // 上一次生成 ID 的时间戳
    private long lastTimestamp = -1L;

    public static final SnowflakeIdGenerator DEFAULT = new SnowflakeIdGenerator(1, 1);

    public SnowflakeIdGenerator(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("Worker ID 不能大于 " + maxWorkerId + " 或小于 0");
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException("数据中心 ID 不能大于 " + maxDataCenterId + " 或小于 0");
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    public String nextIdAsString() {
        return String.valueOf(nextId());
    }

    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨异常，当前时间小于上一次生成 ID 的时间：" + (lastTimestamp - currentTimestamp) + " 毫秒");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                // 序列号溢出，等待下一毫秒
                currentTimestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - startTimestamp) << timestampShift) |
                (dataCenterId << dataCenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}
