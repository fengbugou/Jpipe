package com.dt.jpipe.util;


import com.dt.jpipe.core.Nullable;

import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * @author ofisheye
 * @date 2018/9/30
 * gain rich API getInt/getLong/getBool... by implement just one method: getString()
 * suitable for cache like data accessing class to implement
 */
public interface KeyValueGetter {

    /**
     * get string from this object with specific key
     */
    @Nullable
    String getString(String key);

    /**
     * get string from this object with specific key, if key is not present, return defaultValue instead
     */
    default String getString(String key, String defaultValue) {
        String value = getString(key);
        return value == null ? defaultValue : value;
    }

    /**
     * get boolean from this object with specific key, if key is not present, return null
     */
    @Nullable
    default Boolean getBool(String key) {
        return ParseValueUtil.asBool(getString(key), null);
    }

    /**
     * get boolean from this object with specific key, if key is not present, return defaultValue instead
     */
    default boolean getBool(String key, boolean defaultValue) {
        return ParseValueUtil.asBool(getString(key), defaultValue);
    }

    /**
     * get Byte from this object with specific key, if key is not present, return null
     */
    @Nullable
    default Byte getByte(String key) {
        return ParseValueUtil.asByte(getString(key), null);
    }

    /**
     * get byte from this object with specific key, if key is not present, return defaultValue instead
     */
    default byte getByte(String key, byte defaultValue) {
        return ParseValueUtil.asByte(getString(key), defaultValue);
    }

    /**
     * get character from this object with specific key, if key is not present, return null
     */
    @Nullable
    default Character getChar(String key) {
        return ParseValueUtil.asChar(getString(key), null);
    }

    /**
     * get char from this object with specific key, if key is not present, return defaultValue instead
     */
    default char getChar(String key, char defaultValue) {
        return ParseValueUtil.asChar(getString(key), defaultValue);
    }

    /**
     * get short from this object with specific key, if key is not present, return null
     */
    @Nullable
    default Short getShort(String key) {
        return ParseValueUtil.asShort(getString(key), null);
    }

    /**
     * get short from this object with specific key, if key is not present, return defaultValue instead
     */
    default Short getShort(String key, Short defaultValue) {
        return ParseValueUtil.asShort(getString(key), defaultValue);
    }

    /**
     * get integer from this object with specific key, if key is not present, return null
     */
    @Nullable
    default Integer getInt(String key) {
        return ParseValueUtil.asInt(getString(key), null);
    }

    /**
     * get int from this object with specific key, if key is not present, return defaultValue instead
     */
    default int getInt(String key, int defaultValue) {
        return ParseValueUtil.asInt(getString(key), defaultValue);
    }

    default int getInt(String key, int defaultValue, Predicate<Integer> condition) {
        Integer value = ParseValueUtil.asInt(getString(key), defaultValue);
        if (condition.test(value)) {
            return value;
        } else {
            return defaultValue;
        }
    }

    /**
     * get long from this object with specific key, if key is not present, return null
     */
    @Nullable
    default Long getLong(String key) {
        return ParseValueUtil.asLong(getString(key), null);
    }

    /**
     * get long from this object with specific key, if key is not present, return defaultValue instead
     */
    default long getLong(String key, long defaultValue) {
        return ParseValueUtil.asLong(getString(key), defaultValue);
    }

    /**
     * get float from this object with specific key, if key is not present, return null
     */
    @Nullable
    default Float getFloat(String key) {
        return ParseValueUtil.asFloat(getString(key), null);
    }

    /**
     * get float from this object with specific key
     */
    default float getFloat(String key, float defaultValue) {
        return ParseValueUtil.asFloat(getString(key), defaultValue);
    }

    /**
     * get Double from this object with specific key, if key is not present, return null
     */
    @Nullable
    default Double getDouble(String key) {
        return ParseValueUtil.asDouble(getString(key), null);
    }

    /**
     * get double from this object with specific key, if key is not present, return defaultValue instead
     */
    default double getDouble(String key, double defaultValue) {
        return ParseValueUtil.asDouble(getString(key), defaultValue);
    }

    /**
     * get string with specific key,
     * if key is not present, return null,
     * if present, parse to LocalDateTime object then return
     */
    @Nullable
    default LocalDateTime getDate(String key) {
        return getDate(key, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * get string with specific key,
     * if key is not present, return null,
     * if present, parse to LocalDateTime object with specified format then return
     */
    @Nullable
    default LocalDateTime getDate(String key, String dateFormat) {
        String value = getString(key);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateFormat);
    }

    /**
     * get Enum of type T from this object with specific key, if key is not present, return defaultValue instead
     */
    default <T extends Enum<T>> T getEnum(String key, Class<T> cls, T defaultValue) {
        String value = getString(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return ParseValueUtil.asEnum(cls, value, defaultValue);
    }
}
