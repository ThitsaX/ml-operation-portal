package com.thitsaworks.operation_portal.api.hub_operator.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.hub_operator.security.exception.AuthenticationFailureException;
import com.thitsaworks.operation_portal.component.spring.SpringContext;
import com.thitsaworks.operation_portal.component.spring.SpringRestServiceExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOG = LoggerFactory.getLogger(ApiAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        LOG.info("inside AuthEntryPoint : {}", authException.getClass().getName());

        LOG.error("Exception : ", authException);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        SpringRestServiceExceptionHandler.SpringRestErrorResponseBuilder errorResponseBuilder =
                SpringContext.getBean(SpringRestServiceExceptionHandler.SpringRestErrorResponseBuilder.class);
        ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);

        response.getWriter().write(objectMapper.writeValueAsString(
                errorResponseBuilder.build(new AuthenticationFailureException())));

    }

}
