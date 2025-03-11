package com.example.demo.entities.dtos;

import java.time.LocalDate;

import com.example.demo.entities.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProdutoResponse {
	
	private Long id;
	private String nomeProduto;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd/MM/yyyy")
	private LocalDate data;
	private Status status;
	private String finalidade;
	private Long quantidade;
}
