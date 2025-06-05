package com.thitsaworks.dfsp_portal.api.participant.security;

import com.thitsaworks.dfsp_portal.api.participant.security.exception.AccessDeniedException;
import com.thitsaworks.dfsp_portal.api.participant.security.exception.ApiSecurityException;
import com.thitsaworks.dfsp_portal.component.http.CachedBodyHttpServletRequest;
import com.thitsaworks.dfsp_portal.component.security.SecurityContext;
import com.thitsaworks.dfsp_portal.component.usecase.UseCaseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ApiAuthenticationTokenFilter.class);

    private static final String X_AUTH_HEADER = "X-AUTH-HEADER";

    private static final String X_ACCESS_KEY = "X-ACCESS-KEY";

    private Authenticator authenticator = null;

    public ApiAuthenticationTokenFilter(Authenticator authenticator) {

        super();
        this.authenticator = authenticator;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(X_AUTH_HEADER);
        String accessKey = request.getHeader(X_ACCESS_KEY);

        LOG.info("X-AUTH-HEADER : {}", authHeader);
        LOG.info("X-ACCESS-KEY : {}", accessKey);

        if ((authHeader == null || authHeader.isEmpty()) || (accessKey == null || accessKey.isEmpty())) {

            LOG.info("No authentication required.");
            // Ignore security check if these headers are missing. If accessing
            // resource is required authentication info,
            // subsequent filter will handle the actionType.
            filterChain.doFilter(request, response);

        } else {

            CachedBodyHttpServletRequest cachedBodyHttpServletRequest =
                    new CachedBodyHttpServletRequest((HttpServletRequest) request);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                UserContext uc;

                try {

                    uc = this.authenticator.authenticate(cachedBodyHttpServletRequest);

                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                    authorities.add(new SimpleGrantedAuthority("ROLE_PARTICIPANT"));

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(uc.getParticipantUserId(), "N.A", authorities);

                    authenticationToken.setDetails(uc);

                    UseCaseContext.set(uc);
                    UseCaseContext.set(new SecurityContext(uc.getParticipantUserId().getId().toString(),
                            uc.getAccessKey().getId().toString()));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                } catch (AccessDeniedException e) {

                    LOG.error("Error :", e);

                    throw new ApiSecurityException(e);

                } catch (IOException e) {

                    LOG.error("Error :", e);

                    throw e;
                }

            }

            filterChain.doFilter(cachedBodyHttpServletRequest, response);

        }

    }

}