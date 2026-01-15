package com.thitsaworks.operation_portal.component.misc.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Around("@within(log) || @annotation(log)")
    public Object log(ProceedingJoinPoint joinPoint, Log log) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        LOGGER.info("Entering method: {} with arguments: {}", methodName, Arrays.toString(args));

        Object result;

        try {

            result = joinPoint.proceed();

        } catch (Throwable throwable) {

            LOGGER.error("Exception in method: {} with message: {}", methodName, throwable.getMessage(), throwable);
            throw throwable;
        }

        LOGGER.info("Exiting method: {} with result: {}", methodName, result);

        return result;
    }

}
