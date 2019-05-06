package com.dt.jpipe.util;



import com.dt.jpipe.core.Nullable;

import java.util.*;

/**
 * a Map implementation, easy to get typed value from it,
 * exp: int coreCount = dbMap.getInt("core_count", 1);
 */
public class DbMap extends LinkedHashMap<String, Object> implements KeyValueGetter {

    public DbMap() {
    }

    public DbMap(int size) {
        super(size);
    }

    public DbMap(Map<String, ?> map) {
        super(map);
    }

    /**
     * with multiple key1,value1,key2,value2... pairs to init this map
     */
    public DbMap(Object... keyValue) {
        super(keyValue.length);
        //key and value
        int lengthUnit = 2;

        if (keyValue.length % lengthUnit != 0) {
            throw new IllegalArgumentException("keyValue.length is invalid: " + keyValue.length);
        }
        for (int i = 0; i < keyValue.length; i += lengthUnit) {
            put(String.valueOf(keyValue[i]), keyValue[i + 1]);
        }
    }

    public DbMap getDbMap(String key, boolean createIfNotExists) {
        Object value = get(key);
        if (value == null) {
            if (createIfNotExists) {
                DbMap newMap = new DbMap();
                put(key, newMap);
                return newMap;
            } else {
                return null;
            }
        }
        if (value instanceof DbMap) {
            return (DbMap) value;
        } else if (value instanceof Map) {
            DbMap dbMap = new DbMap();
            ((Map) value).forEach((k, v) -> dbMap.put(k.toString(), v));
            return dbMap;
        } else {
            String maybeJson = value.toString();
            return JsonUtil.jsonToMap(maybeJson);
        }
    }

    public <T> List<T> getList(String key, Class<T> cls) {
        Object value = get(key);
        if (value instanceof List) {
            return (List<T>) value;
        } else if (value instanceof Collection) {
            return new ArrayList<T>((Collection) value);
        } else {
            String jsonArray = JsonUtil.beanToJson(value);
            return JsonUtil.jsonArrayToBeanList(jsonArray, cls);
        }
    }

    @Override
    @Nullable
    public String getString(String key) {
        Object value = get(key);
        if (value instanceof String) {
            return (String) value;
        } else {
            return value == null ? null : value.toString();
        }
    }
}
