package com.thitsaworks.operation_portal.api.operation.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@ComponentScan(value = {"com.thitsaworks.operation_portal.api.operation.portal"})
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private PortalFrontEndSetting portalFrontEndSetting;

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(PortalPortSetting portalPortSetting) {

        return factory -> factory.setPort(portalPortSetting.portNo());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins(portalFrontEndSetting.url())
                .allowedMethods("*");

    }

    public record PortalFrontEndSetting(String url) { }

    public record PortalPortSetting(int portNo) { }

}