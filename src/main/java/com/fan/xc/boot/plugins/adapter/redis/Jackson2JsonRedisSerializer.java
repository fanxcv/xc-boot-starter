package com.fan.xc.boot.plugins.adapter.redis;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * {@link org.springframework.data.redis.serializer.RedisSerializer} that can read and write JSON using
 * <a href="https://github.com/FasterXML/jackson-core">Jackson's</a> and
 * <a href="https://github.com/FasterXML/jackson-databind">Jackson Databind</a> {@link ObjectMapper}.
 * <p>
 * This converter can be used to bind to typed beans, or untyped {@link java.util.HashMap HashMap} instances.
 * <b>Note:</b>Null objects are serialized as empty arrays and vice versa.
 * @author Thomas Darimont
 * @since 1.2
 */
public class Jackson2JsonRedisSerializer<T> {
    private static final byte[] EMPTY_ARRAY = new byte[0];

    private final JavaType javaType;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates a new {@link org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer} for the given target {@link Class}.
     * @param type type
     */
    public Jackson2JsonRedisSerializer(Class<T> type) {
        this.javaType = getJavaType(type);
    }

    /**
     * Creates a new {@link org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer} for the given target {@link JavaType}.
     * @param javaType javaType
     */
    public Jackson2JsonRedisSerializer(JavaType javaType) {
        this.javaType = javaType;
    }

    @SuppressWarnings("unchecked")
    public T deserialize(@Nullable byte[] bytes) throws TypeNotPresentException {
        if (isEmpty(bytes)) {
            return null;
        }
        try {
            return (T) this.objectMapper.readValue(bytes, 0, bytes.length, javaType);
        } catch (Exception ex) {
            throw new TypeNotPresentException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    public byte[] serialize(@Nullable Object t) throws TypeNotPresentException {

        if (t == null) {
            return EMPTY_ARRAY;
        }
        try {
            return this.objectMapper.writeValueAsBytes(t);
        } catch (Exception ex) {
            throw new TypeNotPresentException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "'objectMapper' must not be null");
        this.objectMapper = objectMapper;
    }

    private JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }

    private boolean isEmpty(@Nullable byte[] data) {
        return (data == null || data.length == 0);
    }
}
