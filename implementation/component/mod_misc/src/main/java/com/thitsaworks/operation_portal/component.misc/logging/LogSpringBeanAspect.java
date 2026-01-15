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

        String
            methodName =
            joinPoint.getSignature()
                     .toShortString();
        Object[] args = joinPoint.getArgs();

        String safeArgs = "[]";
        if (args != null && args.length > 0) {
            try {
                String rawArgs = this.objectMapper.writeValueAsString(args);
                safeArgs = MaskPassword.maskPassword(this.objectMapper, rawArgs);
            } catch (Exception e) {
                safeArgs = "[Arguments could not be serialized]";
            }
        }

        LOGGER.info("Entering method: {} with arguments: {}", methodName, safeArgs);

        Object result;

        try {

            result = joinPoint.proceed();

        } catch (Throwable throwable) {

            LOGGER.error("Exception in method: {} with message: {}", methodName, throwable.getMessage(),
                         throwable);

            throw throwable;
        }

        String safeResult = "{}";
        if (result != null) {
            try {
                String rawResult = this.objectMapper.writeValueAsString(result);
                safeResult = MaskPassword.maskPassword(this.objectMapper, rawResult);
            } catch (Exception e) {
                safeResult = "[Result could not be serialized]";
            }
        }
        LOGGER.info("Exiting method: {} with result: {}", methodName, safeResult);

        return result;
    }

}
