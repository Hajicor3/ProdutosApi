package com.example.demo.services.exceptions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ErrorDecoder defaultDecoder = new Default();
	
	
	@Override
	public Exception decode(String methodKey, Response response) {
		HttpStatus status = HttpStatus.resolve(response.status());
		String errorMessage = "Erro na comunicação com o microserviço";
		
		try {
			if(response.body() != null) {
				String responseBody = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
				JsonNode jsonNode = objectMapper.readTree(responseBody);
				
				String erro = jsonNode.has("erro") ? jsonNode.get("message").asText() : "Erro desconhecido"; 
				String mensagem = jsonNode.has("message") ? jsonNode.get("message").asText() : "Sem detalhes";
				
				errorMessage = String.format("Erro FeignClient [%d]: %s - %s", response.status(), erro,mensagem);
			}
		}
		catch(IOException e){
			errorMessage = "Erro ao processar a resposta do microserviço.";
		}
		
		 return new FeignExceptionHandler(status != null ? status.value() : 500, errorMessage);
	}

}
