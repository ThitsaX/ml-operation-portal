package com.thitsaworks.operation_portal.api.operation.portal.security;

import com.thitsaworks.operation_portal.component.misc.http.CachedBodyHttpServletRequest;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class MDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);

        try {

            MDC.put("TRACE_ID",
                    String.valueOf(Snowflake.get()
                                            .nextId()));

            filterChain.doFilter(cachedRequest, response);

        } finally {
            MDC.remove("TRACE_ID");
        }
    }

}
