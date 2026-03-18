package com.thitsaworks.operation_portal.api.operation.portal;

import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.request.ReportDownloadRequestManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(
        Settings settings) {

        return factory -> factory.setPort(settings.getPortNo());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry
            .addMapping("/**")
            .allowedOrigins(settings().getUrl().split(","))
            .allowedMethods("*");
    }

    @Bean
    public Settings settings() {

        String port = System.getProperty("OPERATION_PORTAL_PORT_NO");
        String frontendUrls = System.getProperty("OPERATION_PORTAL_FRONTEND_ENDPOINT");
        return new Settings(Integer.parseInt(port), frontendUrls);
    }

    @Bean
    public ReportGenerator.Settings reportSettings() {

        return new ReportGenerator.Settings(
            Integer.parseInt(System.getProperty("REPORT_PAGE_SIZE", "100000")));
    }

    @Bean
    public ReportDownloadRequestManager.Settings reportDownloadRequestSettings() {

        return new ReportDownloadRequestManager.Settings(
            Integer.parseInt(System.getProperty("REPORT_MAX_RETRY", "3")));
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