package org.example.config;

import org.example.service.RideService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppModeConfig {

    @Bean
    public RideService rideService() {
        return new RideService();
    }
}
