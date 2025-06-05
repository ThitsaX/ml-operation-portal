package com.thitsaworks.dfsp_portal.api.participant.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.dfsp_portal.api.participant.security.exception.ApiSecurityException;
import com.thitsaworks.dfsp_portal.component.spring.SpringContext;
import com.thitsaworks.dfsp_portal.component.spring.SpringRestServiceExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilterExceptionHandler extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(AuthFilterExceptionHandler.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {

        ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);

        try {

            LOG.info("inside Exception Handler");
            filterChain.doFilter(request, response);
            LOG.info("finish : inside Exception Handler");

        } catch (ApiSecurityException e) {

            LOG.error("Error :", e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            SpringRestServiceExceptionHandler.SpringRestErrorResponseBuilder errorResponseBuilder =
                    SpringContext.getBean(SpringRestServiceExceptionHandler.SpringRestErrorResponseBuilder.class);

            response.getWriter().write(objectMapper.writeValueAsString(errorResponseBuilder.build(e)));

        } catch (Exception e) {

            LOG.error("Error : ", e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            SpringRestServiceExceptionHandler.SpringRestErrorResponseBuilder errorResponseBuilder =
                    SpringContext.getBean(SpringRestServiceExceptionHandler.SpringRestErrorResponseBuilder.class);

            response.getWriter().write(objectMapper.writeValueAsString(errorResponseBuilder.build(e)));

        }

    }

}