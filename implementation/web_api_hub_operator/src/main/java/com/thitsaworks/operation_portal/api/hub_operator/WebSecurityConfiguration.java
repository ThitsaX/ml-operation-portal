package com.thitsaworks.operation_portal.api.hub_operator;

import com.thitsaworks.operation_portal.api.hub_operator.security.ApiAuthenticationEntryPoint;
import com.thitsaworks.operation_portal.api.hub_operator.security.ApiAuthenticationTokenFilter;
import com.thitsaworks.operation_portal.api.hub_operator.security.ApiAuthenticator;
import com.thitsaworks.operation_portal.api.hub_operator.security.AuthFilterExceptionHandler;
import com.thitsaworks.operation_portal.api.hub_operator.security.Authenticator;
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
    public ApiAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {

        return new ApiAuthenticationTokenFilter(this.authenticator());
    }

    @Bean
    public Authenticator authenticator() {

        return new ApiAuthenticator();

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