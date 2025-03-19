package com.example.demo.entities;

import java.time.Instant;

import com.example.demo.entities.enums.TipoDeMovimentacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movimentacao {

	private Long id;
	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss'Z'",timezone = "GMT")
	private Instant data;
	private Long idProduto;
	private Long quantidade;
	private TipoDeMovimentacao tipoDeMovimentacao;
}
