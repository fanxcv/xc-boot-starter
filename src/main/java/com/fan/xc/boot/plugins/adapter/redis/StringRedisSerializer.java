package com.fan.xc.boot.plugins.adapter.redis;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Simple {@link java.lang.String} to {@literal byte[]} (and back) serializer. Converts {@link java.lang.String Strings}
 * into bytes and vice-versa using the specified charset (by default {@literal UTF-8}).
 * <p>
 * Useful when the interaction with the Redis happens mainly through Strings.
 * <p>
 * Does not perform any {@literal null} conversion since empty strings are valid keys/values.
 * @author Costin Leau
 * @author Christoph Strobl
 * @author Mark Paluch
 */
public class StringRedisSerializer {

    private final Charset charset;

    /**
     * {@link org.springframework.data.redis.serializer.StringRedisSerializer} to use 8 bit UCS Transformation Format.
     * @see StandardCharsets#UTF_8
     * @since 2.1
     */
    public static final StringRedisSerializer UTF_8 = new StringRedisSerializer(StandardCharsets.UTF_8);

    /**
     * Creates a new {@link StringRedisSerializer} using {@link StandardCharsets#UTF_8 UTF-8}.
     */
    public StringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    /**
     * Creates a new {@link StringRedisSerializer} using the given {@link Charset} to encode and decode strings.
     * @param charset must not be {@literal null}.
     */
    public StringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    public String deserialize(@Nullable byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    public byte[] serialize(@Nullable String string) {
        return (string == null ? null : string.getBytes(charset));
    }
}
