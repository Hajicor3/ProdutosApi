package com.example.demo.entities.dtos;

import com.example.demo.entities.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdutoDto {
	
	private String nomeProduto;
	private Status status;
	private String finalidade;
	private Long fornecedorId;
}
