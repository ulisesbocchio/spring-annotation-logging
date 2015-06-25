package com.ulisesbocchio.springannotationlogging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author Ulises Bocchio
 */
@Aspect
public class LoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);

    private ExpressionParser elParser = new SpelExpressionParser();

    public LoggingAspect() {
        LOG.info("Annotation Logging Aspect Created.");
    }

    @Around("execution(* *(..)) && @annotation(annotation)")
    public Object profile(ProceedingJoinPoint pjp, Log annotation) throws Throwable {
        Object returnValue = pjp.proceed();
        Object target = pjp.getTarget();
        Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String message = getLogMessage(returnValue, signature.getParameterNames(), args, annotation);
        log(target, annotation.level(), message);
        LOG.info("Logging Aspect Activated on {}.{}, return value: {}", signature.getDeclaringTypeName(), signature.getName(), returnValue);
        return returnValue;
    }

    private String getLogMessage(Object returnValue, String[] argNames, Object[] args, Log annotation) {
        Expression exp = elParser.parseExpression(annotation.value());
        EvaluationContext ctx = new StandardEvaluationContext(returnValue);
        populateELContext(ctx, argNames, args);
        return exp.getValue(ctx, String.class);
    }

    private void log(Object target, Level level, String message) {
        Logger logger = LoggerFactory.getLogger(target.getClass());
        level.getLoggerFunction().accept(logger, message);
    }

    private void populateELContext(EvaluationContext ctx, String[] argNames, Object[] args) {
        IntStream.range(0, argNames.length)
                .forEach(i -> ctx.setVariable(argNames[i], args[i]));
    }

    @Autowired
    public void setEnvironment(Environment e) {
        LOG.info("Annotation Logging Aspect Initialized: " + Arrays.asList(e.getActiveProfiles()));
    }
}
