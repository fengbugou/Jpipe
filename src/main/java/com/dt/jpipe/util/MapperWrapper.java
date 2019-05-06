
package com.dt.jpipe.util;

import com.dt.jpipe.core.Nullable;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * TODO desc
 *
 * @author ofisheye
 * @date 2019-01-31
 */
public class MapperWrapper {

    private final ObjectMapper mapper;

    MapperWrapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * parse object to json string, throw exception if parse failed
     *
     * @param obj object to parse
     */
    public String beanToJson(Object obj) {
        if (obj instanceof CharSequence) {
            return obj.toString();
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * parse bean to a map object, throw exception if parse failed
     */
    public DbMap beanToMap(Object obj) {
        try {
            String json = beanToJson(obj);
            return jsonToMap(json);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * parse json to an instance with specific class, throw exception if parse failed
     */
    public <T> T jsonToBean(String jsonString, Class<T> requiredClass) {
        try {
            return mapper.readValue(jsonString, requiredClass);
        } catch (Throwable e) {
            throw new RuntimeException("json=" + jsonString, e);
        }
    }

    /**
     * parse json string to map object, throw exception if parse failed
     */
    public DbMap jsonToMap(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return new DbMap();
        }
        try {
            return mapper.readValue(jsonString, DbMap.class);
        } catch (Throwable e) {
            throw new RuntimeException("json=" + jsonString, e);
        }
    }

    /**
     * parse map to object, throw exception if parse failed
     */
    @Nullable
    public <T> T mapToBean(Map<?, ?> map, Class<T> requiredClass) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            String jsonString = beanToJson(map);
            return mapper.readValue(jsonString, requiredClass);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * parse json array string to list of object with specific type, throw exception if parse failed
     */
    public <T> List<T> jsonArrayToBeanList(String jsonArrayString, Class<T> requiredClass) {
        try {
            JavaType jt = mapper.getTypeFactory()
                                .constructCollectionType(List.class, requiredClass);
            return mapper.readValue(jsonArrayString, jt);
        } catch (Throwable e) {
            throw new RuntimeException("json=" + jsonArrayString, e);
        }
    }

    /**
     * parse json array string to list of map, throw exception if parse failed
     */
    public List<DbMap> jsonArrayToMapList(String jsonArrayString) {
        try {
            JavaType jt = mapper.getTypeFactory()
                                .constructCollectionType(List.class, DbMap.class);
            return mapper.readValue(jsonArrayString, jt);
        } catch (Throwable e) {
            throw new RuntimeException("json=" + jsonArrayString, e);
        }
    }
}
