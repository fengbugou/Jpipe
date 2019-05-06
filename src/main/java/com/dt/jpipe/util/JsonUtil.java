
package com.dt.jpipe.util;

import com.dt.jpipe.core.Nullable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * parse json util
 *
 * @author ofisheye
 * @date 2018-12-25
 */
public class JsonUtil {
    private static MapperWrapper defaultMapperWrapper;
    private static MapperWrapper prettyJsonMapperWrapper;
    private static MapperWrapper nullFreeMapperWrapper;
    private static MapperWrapper sortedMapperWrapper;

    static {
        JsonFactory jf = new JsonFactory();
        jf.enable(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);
        jf.enable(Feature.ALLOW_COMMENTS);
        jf.enable(Feature.ALLOW_SINGLE_QUOTES);
        jf.enable(Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        jf.enable(Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        jf.enable(Feature.ALLOW_NON_NUMERIC_NUMBERS);
        jf.enable(Feature.ALLOW_YAML_COMMENTS);
        jf.enable(Feature.ALLOW_MISSING_VALUES);

        DateFormat defaultDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        NullKeySerializer nullKeySerializer = new NullKeySerializer();
        ObjectMapper defaultMapper = new ObjectMapper(jf);
        defaultMapper.setDateFormat(defaultDF);
        defaultMapper.setSerializationInclusion(Include.NON_NULL);
        defaultMapper.getSerializerProvider().setNullKeySerializer(nullKeySerializer);
        defaultMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        defaultMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        defaultMapperWrapper = new MapperWrapper(defaultMapper);

        ObjectMapper nullFreeMapper = new ObjectMapper(jf);
        nullFreeMapper.setDateFormat(defaultDF);
        nullFreeMapper.setSerializerProvider(new CustomSerializerProvider());
        nullFreeMapper.getSerializerProvider().setNullKeySerializer(nullKeySerializer);
        nullFreeMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        nullFreeMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        nullFreeMapperWrapper = new MapperWrapper(nullFreeMapper);

        ObjectMapper prettyJsonMapper = new ObjectMapper(jf);
        prettyJsonMapper.setDateFormat(defaultDF);
        prettyJsonMapper.setSerializationInclusion(Include.NON_NULL);
        prettyJsonMapper.getSerializerProvider().setNullKeySerializer(nullKeySerializer);
        prettyJsonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        prettyJsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        prettyJsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        prettyJsonMapperWrapper = new MapperWrapper(prettyJsonMapper);

        ObjectMapper sortedKeyMapper = new ObjectMapper(jf);
        //sorted keys is required! to guarantee beans with identical properties will generate exactly same fingerprint
        sortedKeyMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        sortedKeyMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        sortedKeyMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        sortedKeyMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        sortedKeyMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        sortedMapperWrapper = new MapperWrapper(sortedKeyMapper);
    }

    public static MapperWrapper pretty() {
        return prettyJsonMapperWrapper;
    }

    public static MapperWrapper nullFree() {
        return nullFreeMapperWrapper;
    }

    public static MapperWrapper sortedKey() {
        return sortedMapperWrapper;
    }

    /**
     * parse object to json string, throw exception if parse failed
     */
    public static String beanToJson(Object obj) {
        return defaultMapperWrapper.beanToJson(obj);
    }

    /**
     * parse bean to a map object, throw exception if parse failed
     */
    public static DbMap beanToMap(Object obj) {
        return defaultMapperWrapper.beanToMap(obj);
    }

    /**
     * parse json to an instance with specific class, throw exception if parse failed
     */
    public static <T> T jsonToBean(String jsonString, Class<T> requiredClass) {
        return defaultMapperWrapper.jsonToBean(jsonString, requiredClass);
    }

    /**
     * parse json string to map object, throw exception if parse failed
     */
    public static DbMap jsonToMap(String jsonString) {
        return defaultMapperWrapper.jsonToMap(jsonString);
    }

    /**
     * parse map to object, throw exception if parse failed
     */
    @Nullable
    public static <T> T mapToBean(Map<?, ?> map, Class<T> requiredClass) {
        return defaultMapperWrapper.mapToBean(map, requiredClass);
    }


    /**
     * parse json array string to list of object with specific type, throw exception if parse failed
     */
    public static <T> List<T> jsonArrayToBeanList(String jsonArrayString, Class<T> requiredClass) {
        return defaultMapperWrapper.jsonArrayToBeanList(jsonArrayString, requiredClass);
    }

    /**
     * parse json array string to list of map, throw exception if parse failed
     */
    public static List<DbMap> jsonArrayToMapList(String jsonArrayString) {
        return defaultMapperWrapper.jsonArrayToMapList(jsonArrayString);
    }

    private static class NullKeySerializer extends StdSerializer<Object> {
        public NullKeySerializer() {
            this(null);
        }

        public NullKeySerializer(Class<Object> t) {
            super(t);
        }

        @Override
        public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused) throws IOException {
            jsonGenerator.writeFieldName("");
        }
    }

    private static class NullStringValueSerializer extends JsonSerializer<Object> {
        private static final NullStringValueSerializer INSTANCE = new NullStringValueSerializer();

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString("");
        }
    }

    private static class NullNumberValueSerializer extends JsonSerializer<Object> {
        private static final NullNumberValueSerializer INSTANCE = new NullNumberValueSerializer();

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(0);
        }
    }

    private static class NullArraySerializer extends JsonSerializer<Object> {
        private static final NullArraySerializer INSTANCE = new NullArraySerializer();

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber("[]");
        }
    }

    private static class CustomSerializerProvider extends DefaultSerializerProvider {

        public CustomSerializerProvider() {
        }

        public CustomSerializerProvider(SerializerProvider src, SerializationConfig config, SerializerFactory f) {
            super(src, config, f);
        }

        @Override
        public DefaultSerializerProvider createInstance(SerializationConfig serializationConfig, SerializerFactory serializerFactory) {
            return new CustomSerializerProvider(this, serializationConfig, serializerFactory);
        }

        @Override
        public JsonSerializer<Object> findNullValueSerializer(BeanProperty property) throws JsonMappingException {
            Class propertyClass = property.getType().getRawClass();
            if (CharSequence.class.isAssignableFrom(propertyClass)) {
                return NullStringValueSerializer.INSTANCE;
            } else if (Number.class.isAssignableFrom(propertyClass)) {
                return NullNumberValueSerializer.INSTANCE;
            } else if (Collection.class.isAssignableFrom(propertyClass)) {
                return NullArraySerializer.INSTANCE;
            }
            return super.findNullValueSerializer(property);
        }
    }

}
