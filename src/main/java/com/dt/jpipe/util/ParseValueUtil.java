package com.dt.jpipe.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * parse value between types with fallback values
 *
 * @author ofisheye
 * @date 2018-12-13
 */
public class ParseValueUtil {

    private static ThreadLocal<DateFormat> formatterThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static String[] positiveStrings = new String[]{"true", "Y", "y", "1"};

    /**
     * parse src to enum, return defaultValue if parse failed
     *
     * @param enumClass    enum type wish to parse to
     * @param src          parse from this string
     * @param defaultValue fallback value
     * @param <T>          enum generic type
     */
    public static <T extends Enum> T asEnum(Class<T> enumClass, String src, T defaultValue) {
        try {
            return (T) Enum.valueOf(enumClass.asSubclass(Enum.class), src);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * parse src to string, return defaultValue if parse failed
     *
     * @param src          parse from, null is acceptable
     * @param defaultValue fallback value
     */
    public static String asString(Object src, String defaultValue) {
        return null == src ? defaultValue : src.toString();
    }

    /**
     * parse src to bool, return defaultValue if parse failed
     *
     * @param src          parse from, null is acceptable
     * @param defaultValue fallback value
     * @return true if src is true(Boolean) or "true" or "y" or "Y" or "1", return false otherwise
     */
    public static Boolean asBool(Object src, Boolean defaultValue) {
        if (null == src) {
            return defaultValue;
        }
        String val = src.toString();
        for (String positiveString : positiveStrings) {
            if (positiveString.equals(val)) {
                return true;
            }
        }
        return false;
    }

    /**
     * parse src to byte, return defaultValue if parse failed
     *
     * @param src          parse from, null is acceptable
     * @param defaultValue fallback value
     */
    public static Byte asByte(Object src, Byte defaultValue) {
        if (null == src) {
            return defaultValue;
        }
        try {
            return Byte.valueOf(src.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    /**
     * parse src to char, return defaultValue if parse failed
     *
     * @param src          parse from, null is acceptable
     * @param defaultValue fallback value
     */
    public static Character asChar(Object src, Character defaultValue) {
        if (null == src) {
            return defaultValue;
        }
        String val = src.toString();
        if (val.isEmpty()) {
            return defaultValue;
        }
        return val.charAt(0);
    }

    /**
     * parse src to short, return defaultValue if parse failed
     *
     * @param src          parse from, null is acceptable
     * @param defaultValue fallback value
     */
    public static Short asShort(Object src, Short defaultValue) {
        if (null == src) {
            return defaultValue;
        }
        try {
            return Short.valueOf(src.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * parse src to in, return defaultValue if parse failed
     *
     * @param src          parse from, null is acceptable
     * @param defaultValue fallback value
     */
    public static Integer asInt(Object src, Integer defaultValue) {
        if (null == src) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(src.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * parse src to long, return defaultValue if parse failed
     *
     * @param src          parse from, null is acceptable
     * @param defaultValue fallback value
     */
    public static Long asLong(Object src, Long defaultValue) {
        if (null == src) {
            return defaultValue;
        }
        try {
            return Long.valueOf(src.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * parse src to float, return defaultValue if parse failed
     *
     * @param src          parse from, null is acceptable
     * @param defaultValue fallback value
     */
    public static Float asFloat(Object src, Float defaultValue) {
        if (null == src) {
            return defaultValue;
        }
        try {
            return Float.valueOf(src.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * parse src to double, return defaultValue if parse failed
     *
     * @param src          parse from, null is acceptable
     * @param defaultValue fallback value
     */
    public static Double asDouble(Object src, Double defaultValue) {
        if (null == src) {
            return defaultValue;
        }
        try {
            return Double.valueOf(src.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String dateToString(Date date) {
        return formatterThreadLocal.get().format(date);
    }
}
