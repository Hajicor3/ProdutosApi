package com.example.demo.entities.dtos;

import com.example.demo.entities.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdutoModelDto {

	private String nomeProduto;
	private Status status;
	private String finalidade;
}
