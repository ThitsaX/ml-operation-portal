package com.thitsaworks.operation_portal.api.participant;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan(
        value = {
                "com.thitsaworks.operation_portal.api.participant"})
public class WebConfiguration {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(PortalPortSetting portalPortSetting) {

        return factory -> factory.setPort(portalPortSetting.portNo());
    }

    public record PortalPortSetting(int portNo) { }

}
