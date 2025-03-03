package com.example.demo.entities.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FornecedorRequest {
	
	@Schema(hidden = true)
	private Long id;
	private String nome;
}
