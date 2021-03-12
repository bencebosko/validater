package org.validater;

import java.util.HashMap;
import java.util.Map;

class PrimitiveMapper {

    private static final Map<Class<?>, Class<?>> primitiveToWrapper;

    static {
        primitiveToWrapper = new HashMap<Class<?>, Class<?>>() {{
            put(boolean.class, Boolean.class);
            put(byte.class, Byte.class);
            put(char.class, Character.class);
            put(double.class, Double.class);
            put(float.class, Float.class);
            put(int.class, Integer.class);
            put(long.class, Long.class);
            put(short.class, Short.class);
            put(void.class, Void.class);
        }};

    }

    static Class<?> getWrapper(Class<?> primitiveClass) {
        return primitiveToWrapper.get(primitiveClass);
    }
}
