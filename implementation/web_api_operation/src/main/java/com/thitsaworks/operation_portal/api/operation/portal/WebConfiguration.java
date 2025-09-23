package com.thitsaworks.operation_portal.api.operation.portal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@EnableWebMvc
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(Settings settings) {
        return factory -> factory.setPort(settings.getPortNo());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(settings().getUrl().split(","))
                .allowedMethods("*");
    }

    @Bean
    public Settings settings() {
        String port = System.getProperty("OPERATION_PORTAL_PORT_NO");
        String frontendUrls = System.getProperty("OPERATION_PORTAL_FRONTEND_ENDPOINT");
        return new Settings(Integer.parseInt(port), frontendUrls);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Settings {
        private int portNo;
        private String url;
    }
}