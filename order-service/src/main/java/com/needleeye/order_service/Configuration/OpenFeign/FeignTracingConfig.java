package com.needleeye.order_service.Configuration.OpenFeign;

import org.springframework.context.annotation.Configuration;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;

@Configuration
public class FeignTracingConfig {
    @Bean
    public MicrometerCapability micrometerCapability(MeterRegistry meterRegistry) {
        return new MicrometerCapability(meterRegistry);
    }
}
