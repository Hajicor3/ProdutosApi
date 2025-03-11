package com.example.demo.entities.dtos;

import com.example.demo.entities.enums.TipoDeMovimentacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimentacaoRequest {

	private Long idProduto;
	private TipoDeMovimentacao tipoDeMovimentacao;
}
