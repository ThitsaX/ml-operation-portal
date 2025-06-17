package com.thitsaworks.operation_portal.api.participant;

import com.thitsaworks.operation_portal.api.participant.security.ApiAuthenticationEntryPoint;
import com.thitsaworks.operation_portal.api.participant.security.ApiAuthenticationTokenFilter;
import com.thitsaworks.operation_portal.api.participant.security.ApiAuthenticator;
import com.thitsaworks.operation_portal.api.participant.security.AuthFilterExceptionHandler;
import com.thitsaworks.operation_portal.api.participant.security.Authenticator;
import com.thitsaworks.operation_portal.core.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    // @@formatter:off

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ApiAuthenticationTokenFilter authenticationTokenFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement((sessionManagement)
                                       -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling((exceptionHandling)
                                       -> exceptionHandling.authenticationEntryPoint(unauthorizedHandler()))
            .authorizeHttpRequests(configure ->
                                           configure
                                                   .requestMatchers("/", "/public/**").permitAll()
                                                   .requestMatchers("/secured/**").authenticated())
            .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(authFilterExceptionHandler(), ApiAuthenticationTokenFilter.class);
        return http.build();
    }

    // @@formatter:on

    @Bean
    public ApiAuthenticationTokenFilter authenticationTokenFilterBean(PrincipalCache principalCache, ParticipantUserCache participantUserCache) throws Exception {

        return new ApiAuthenticationTokenFilter(this.authenticator(principalCache, participantUserCache));
    }

    @Bean
    public Authenticator authenticator(PrincipalCache principalCache, ParticipantUserCache participantUserCache) {

        return new ApiAuthenticator(principalCache, participantUserCache);

    }

    @Bean
    public AuthFilterExceptionHandler authFilterExceptionHandler() {

        return new AuthFilterExceptionHandler();
    }

    @Bean
    public ApiAuthenticationEntryPoint unauthorizedHandler() {

        return new ApiAuthenticationEntryPoint();
    }



}