package org.creation.singlejob.key;

import java.lang.reflect.Method;

public interface SingleJobKeyGenerator {
    /**
     * Generate a key for the given method and its parameters.
     * @param target the target instance
     * @param method the method being called
     * @param params the method parameters (with any var-args expanded)
     * @return a generated key
     */
    String generate(String Expression, Method method, Object[] params);
}
