package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.services.exceptions.FeignErrorDecoder;

@Configuration
public class FeignConfig {

	@Bean
    FeignErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }
}
