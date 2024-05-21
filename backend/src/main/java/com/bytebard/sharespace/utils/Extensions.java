package com.bytebard.sharespace.utils;

import io.micrometer.common.util.StringUtils;

import java.util.function.Consumer;

public class Extensions {

    public static void executeIfTrue(boolean condition, Consumer<Void> consumer) {
        if (condition) {
            consumer.accept(null);
        }
    }

    public static void executeIfNotBlank(String str, Consumer<String> consumer) {
        if (StringUtils.isNotBlank(str)) {
            consumer.accept(str);
        }
    }

    public static <T> void executeIfNotNull(T obj, Consumer<T> consumer) {
        if (obj != null) {
            consumer.accept(obj);
        }
    }
}
