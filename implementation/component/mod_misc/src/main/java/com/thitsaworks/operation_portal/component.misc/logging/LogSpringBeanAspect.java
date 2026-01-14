package com.thitsaworks.operation_portal.component.misc.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.util.MaskPassword;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class LogSpringBeanAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogSpringBeanAspect.class);

    private final ObjectMapper objectMapper;

    public LogSpringBeanAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around(
            "(" +
                    "@within(org.springframework.stereotype.Service) || " +
                    "@within(org.springframework.stereotype.Component) || " +
                    "@target(org.springframework.stereotype.Controller) || " +
                    "@target(org.springframework.web.bind.annotation.RestController)" +
                    ") && (" +
                    "execution(* com.thitsaworks.operation_portal..*.execute(..)) || " +
                    "execution(* com.thitsaworks.operation_portal..*.onExecute(..))" +
                    ")"
    )
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        String rawArgs = this.objectMapper.writeValueAsString(args);
        String safeArgs = MaskPassword.maskPassword(this.objectMapper, rawArgs);

        LOGGER.info("Entering method: {} with arguments: {}", methodName, safeArgs);

        Object result;

        try {

            result = joinPoint.proceed();

        } catch (Throwable throwable) {

            LOGGER.error("Exception in method: {} with message: {}", methodName, throwable.getMessage(),
                    throwable);

            throw throwable;
        }

        String rawResult = this.objectMapper.writeValueAsString(result);
        String safeResult = MaskPassword.maskPassword(this.objectMapper, rawResult);

        LOGGER.info("Exiting method: {} with result: {}", methodName, safeResult);

        return result;
    }

}
