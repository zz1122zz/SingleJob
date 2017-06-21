package org.creation.singlejob.key;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public class SimpleSingleJobKeyGenerator implements SingleJobKeyGenerator {
    static SimpleSingleJobKeyGenerator instance;
    static ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    @Override
    public String generate(String Expression, Method method, Object[] params) {
        // TODO Auto-generated method stub
        return paramsIdentification(Expression,method,params);
    }
    private String paramsIdentification(String Expression, Method method, Object[] args) {
        StringBuffer key = new StringBuffer();
        if (null != Expression && !Expression.isEmpty()) {
            ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
            try {
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                int i = 0;
                for (String parameterName : parameterNames) {
                    if (Expression.equalsIgnoreCase(parameterName)) {
                        key.append(args[i].hashCode());
                    }
                    i++;
                }
            } catch (SecurityException e) {
                return ""+Arrays.deepHashCode(args);
            }
        }
        else {
            return ""+Arrays.deepHashCode(args);
        }
        return key.toString();
    }
    public static SimpleSingleJobKeyGenerator getInstance() {
        if (instance == null) {
            synchronized (SimpleSingleJobKeyGenerator.class) {
                if (instance == null)
                    instance = new SimpleSingleJobKeyGenerator();
            }
        }
        return instance;
    }

}
