package com.api.library.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.api.library.entity.FixedAndHourlyPolicy;
import com.api.library.entity.HourlyPolicy;
import com.api.library.entity.PricingPolicy;

@Configuration
public class PricingPolicyConfiguration {
	
	@Profile({"hourly", "default"})
    @Bean
    public PricingPolicy hourlyPolicy() {
        return new HourlyPolicy();
    }
 
	@Profile("fixedHourly")
    @Bean
    public PricingPolicy fixedAndHourlyPolicy() {
        return new FixedAndHourlyPolicy();
    }
	
}


