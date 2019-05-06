package com.dt.jpipe.core;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * metric util class
 *
 * @author ofisheye
 * @date 2019-01-20
 */
@Slf4j
public class Metric {

    private ConcurrentHashMap<String, AtomicLong> metrics = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ThreadLocal<Long>> marks = new ConcurrentHashMap<>();

    public void put(String key, long value) {
        metrics.put(key, new AtomicLong(value));
    }

    public void mark(String key) {
        ThreadLocal<Long> lastMark = marks.computeIfAbsent(key, k -> ThreadLocal.withInitial(System::currentTimeMillis));
        lastMark.set(System.currentTimeMillis());
    }

    public long computeMillisSinceMark(String key) {
        long now = System.currentTimeMillis();
        AtomicLong value = metrics.get(key);
        if (value == null) {
            throw new RuntimeException("put() not called yet");
        }
        ThreadLocal<Long> lastMark = marks.get(key);
        if (lastMark == null) {
            return 0;
        }
        Long lastMarkTime = lastMark.get();
        if (lastMarkTime > now) {
            throw new RuntimeException("mark() not called yet");
        }
        long elapsedMillis = now - lastMarkTime;
        value.addAndGet(elapsedMillis);
        return elapsedMillis;
    }

    public long get(String key) {
        AtomicLong value = metrics.get(key);
        return value == null ? 0L : value.longValue();
    }

    public long add(String key, long addHowMuch) {
        AtomicLong value = metrics.get(key);
        if (value == null) {
            throw new RuntimeException("put() not called yet");
        }
        return value.addAndGet(addHowMuch);
    }

    public ConcurrentHashMap<String, AtomicLong> getMetrics() {
        return metrics;
    }
}
