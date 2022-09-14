package com.biit.webforms.serialization;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonUtils {

    public static ExclusionStrategy getStrategyToAvoidAnnotation() {
        return new ExclusionStrategy() {
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }

            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                return field.getAnnotation(ExcludeFromJson.class) != null;
            }
        };
    }
}
