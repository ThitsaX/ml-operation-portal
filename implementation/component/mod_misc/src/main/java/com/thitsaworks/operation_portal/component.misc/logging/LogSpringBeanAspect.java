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
public class LogSpringBeanAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogSpringBeanAspect.class);

    @Around("(@within(org.springframework.stereotype.Service) || @within(org.springframework.stereotype.Component)) && (execution(* com.thitsaworks.operation_portal..*.execute(..)) || execution(* com.thitsaworks.operation_portal..*.onExecute(..)))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        LOGGER.info("Entering method: {} with arguments: {}", methodName, Arrays.toString(args));

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            LOGGER.error(
                "Exception in method: {} with message: {}", methodName, throwable.getMessage(),
                throwable);
            throw throwable;
        }

        LOGGER.info("Exiting method: {} with result: {}", methodName, result);

        return result;
    }

}
