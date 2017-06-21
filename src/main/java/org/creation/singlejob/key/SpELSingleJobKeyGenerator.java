package org.creation.singlejob.key;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpELSingleJobKeyGenerator implements SingleJobKeyGenerator {
    static SpELSingleJobKeyGenerator instance;
    static ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    static ExpressionParser parser = new SpelExpressionParser();
    static StandardEvaluationContext context = new StandardEvaluationContext();

    public static SpELSingleJobKeyGenerator getInstance() {
        if (instance == null) {
            synchronized (SpELSingleJobKeyGenerator.class) {
                if (instance == null)
                    instance = new SpELSingleJobKeyGenerator();
            }
        }
        return instance;
    }

    @Override
    public String generate(String SpELExpression, Method method, Object[] params) {
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        for (int i = 0;i<params.length;i++) {
            context.setVariable(parameterNames[i], params[i]);
        }
        if (null != SpELExpression && StringUtils.isNotBlank(SpELExpression)) {
            Expression expr = parser.parseExpression(SpELExpression);
            return expr.getValue(context, params).toString();
        }
        else {
            return "";
        }
    }

}
