package com.thitsaworks.dfsp_portal.api.hub_operator;

import com.thitsaworks.dfsp_portal.api.hub_operator.security.ApiAuthenticationEntryPoint;
import com.thitsaworks.dfsp_portal.api.hub_operator.security.ApiAuthenticationTokenFilter;
import com.thitsaworks.dfsp_portal.api.hub_operator.security.ApiAuthenticator;
import com.thitsaworks.dfsp_portal.api.hub_operator.security.AuthFilterExceptionHandler;
import com.thitsaworks.dfsp_portal.api.hub_operator.security.Authenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

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

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        // we don't need CSRF because our token is invulnerable
        // httpSecurity.requiresChannel().anyRequest().requiresSecure();

        // @@formatter:off

        //httpSecurity.addFilterBefore(new UTF8EncodingFilter(), ChannelProcessingFilter.class);

        httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .exceptionHandling().authenticationEntryPoint(this.unauthorizedHandler()).and().authorizeRequests()
                    .antMatchers("/").permitAll().antMatchers("/public/**").permitAll().antMatchers("/secured/**")
                    .hasRole("HUB_OPERATOR").anyRequest().authenticated();
        // @@formatter:on

        // custom token based security filter
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(authFilterExceptionHandler(), ApiAuthenticationTokenFilter.class);
        // disable page caching
        httpSecurity.headers().cacheControl().disable();
        //disable csrf
        httpSecurity.csrf().disable();
        httpSecurity.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

    }

    @Bean
    public ApiAuthenticationEntryPoint unauthorizedHandler() {

        return new ApiAuthenticationEntryPoint();
    }

}