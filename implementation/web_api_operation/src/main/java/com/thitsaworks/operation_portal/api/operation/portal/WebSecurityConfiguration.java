package com.thitsaworks.operation_portal.api.operation.portal;

import com.thitsaworks.operation_portal.api.operation.portal.security.ApiAuthenticationEntryPoint;
import com.thitsaworks.operation_portal.api.operation.portal.security.ApiAuthenticationTokenFilter;
import com.thitsaworks.operation_portal.api.operation.portal.security.ApiAuthenticator;
import com.thitsaworks.operation_portal.api.operation.portal.security.AuthFilterExceptionHandler;
import com.thitsaworks.operation_portal.api.operation.portal.security.Authenticator;
import com.thitsaworks.operation_portal.api.operation.portal.security.MDCFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    // @@formatter:off

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   ApiAuthenticationTokenFilter authenticationTokenFilter,
                                                   WebConfiguration.Settings settings,
                                                   MDCFilter mdcFilter) throws Exception {
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
            .addFilterAfter(mdcFilter, ApiAuthenticationTokenFilter.class)
            .addFilterBefore(authFilterExceptionHandler(), ApiAuthenticationTokenFilter.class)
            // enable CORS with default permissive settings

            .cors(cors -> cors.configurationSource(corsConfigurationSource(settings)));


        return http.build();
    }

    // @@formatter:on

    @Bean
    public CorsConfigurationSource corsConfigurationSource(WebConfiguration.Settings settings) {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(settings.getUrl()));
        config.setAllowedMethods(List.of("*"));                         // allow all methods
        config.setAllowedHeaders(List.of("*"));                         // allow all headers
        config.setAllowCredentials(false);                                  // if cookies/sessions are needed

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

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

    @Bean
    public MDCFilter mdcFilter() { return new MDCFilter(); }

}