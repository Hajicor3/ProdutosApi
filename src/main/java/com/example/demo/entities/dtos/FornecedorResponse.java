package com.example.demo.entities.dtos;

import java.util.List;

import com.example.demo.entities.Produto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FornecedorResponse {
	
	private Long id;
	private String nome;
	private List<Produto> produtos;
}
