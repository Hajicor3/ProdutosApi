package com.example.demo.repositories.mqueues;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProdutoEstoqueQueuePublisher {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private Queue queueProdutoEstoque;
	
	public void SolicitarcancelamentoMovimentacao(Long id) throws JsonProcessingException {
		var json = convertIntoJson(id);
		rabbitTemplate.convertAndSend(queueProdutoEstoque.getName(), json);
	}
	
	public String convertIntoJson(Long id) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(id);
		return json;
	}
	
}
