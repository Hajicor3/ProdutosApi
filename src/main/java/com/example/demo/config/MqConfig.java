package com.example.demo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MqConfig {
	
	@Value("${mq.queue.produto-estoque-queue}")
	private String produtoQueue;
	
	@Bean
	Queue produtoQueue() {
		return new Queue(produtoQueue);
	}
}
