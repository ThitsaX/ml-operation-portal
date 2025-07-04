package com.thitsaworks.operation_portal.api.participant.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.participant.error.ErrorResponse;
import com.thitsaworks.operation_portal.api.participant.security.exception.SecurityErrors;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

        ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);

        Map<String, String> i18nErrorMessages = new HashMap<>();

        i18nErrorMessages.put("en", SecurityErrors.AUTHENTICATION_FAILED.description());

        response.getWriter().write(objectMapper.writeValueAsString(
                new ErrorResponse(SecurityErrors.AUTHENTICATION_FAILED.code(),
                                  SecurityErrors.AUTHENTICATION_FAILED.description(),i18nErrorMessages)));

    }

}
