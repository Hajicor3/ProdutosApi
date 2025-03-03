package com.example.demo.entities.dtos;

import java.time.LocalDate;

import com.example.demo.entities.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProdutoResponse {
	
	private Long id;
	private String nomeProduto;
	private LocalDate data;
	private Status status;
	private String finalidade;
}
