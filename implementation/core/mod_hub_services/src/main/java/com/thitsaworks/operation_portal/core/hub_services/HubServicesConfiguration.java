package com.thitsaworks.operation_portal.core.hub_services;

import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.core.hub_services")
@Import(value = {MiscConfiguration.class})
public class HubServicesConfiguration {

    @Bean
    public HubServicesConfiguration.Settings hubServiceConfigurationSettings() {

        return new HubServicesConfiguration.Settings(System.getProperty("hubEndpoint"));

    }
    public record Settings(String hubEndpoint) {}

}
