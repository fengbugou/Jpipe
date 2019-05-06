package com.dt.jpipe.core;


import com.dt.jpipe.util.DbMap;
import com.dt.jpipe.util.JsonUtil;

import java.util.Map;

/**
 * pipeline environment configuration
 *
 * @author ofisheye
 * @date 2019-01-25
 */
public class JobConfig {

    private final DbMap configData;

    public JobConfig(DbMap configData) {
        this.configData = configData;
    }

    public JobConfig() {
        this(new DbMap());
    }

    public String getStringCompelled(String key) {
        String value = configData.getString(key);
        if (value == null) {
            throw new IllegalArgumentException("config " + key + " must not be null");
        }
        return value;
    }

    public String getString(String key, String defaultValue) {
        return configData.getString(key, defaultValue);
    }

    public boolean getBool(String key, boolean defaultValue) {
        return configData.getBool(key, defaultValue);
    }

    public int getIntCompelled(String key) {
        Integer value = configData.getInt(key);
        if (value == null) {
            throw new IllegalArgumentException("config " + key + " must not be null");
        }
        return value;
    }

    public int getInt(String key, int defaultValue) {
        return configData.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        Long value = configData.getLong(key);
        if (value == null) {
            throw new IllegalArgumentException("config " + key + " must not be null");
        }
        return value;
    }

    public long getLong(String key, long defaultValue) {
        return configData.getLong(key, defaultValue);
    }

    public void put(String key, Object value) {
        this.configData.put(key, value);
    }

    public void putAll(Map<? extends String, ?> m) {
        configData.putAll(m);
    }

    public DbMap getAll() {
        return new DbMap(configData);
    }

    @Override
    public String toString() {
        return JsonUtil.beanToJson(configData);
    }
}
