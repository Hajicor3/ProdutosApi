package com.example.demo.entities;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="tb_agregadores")
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Agregado implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Double densidade;
	private Integer percentual;
	@OneToOne
	@JoinColumn(name = "produto_id")
	@Schema(example = "produtoId")
	private Produto produto;
	
	public Agregado(Double densidade, Integer percentual, Produto produto) {
		setDensidade(densidade);
		setPercentual(percentual);
		setProduto(produto);
	}
	
	public void setDensidade(Double densidade) {
		if(densidade == null || densidade <= 0) {
			throw new IllegalArgumentException("A densidade deve ser um valor positivo.");
		}
		this.densidade = densidade;
	}
	
	public void setPercentual(Integer percentual) {
		if(percentual == null || percentual < 0 || percentual > 100) {
			throw new IllegalArgumentException("O percentual deve estar entre 0 e 100.");
		}
		this.percentual = percentual;
	}
	
	public void setProduto(Produto produto) {
		Objects.requireNonNull(produto, "O produto não pode ser nulo!");
		
		this.produto = produto;
	}
	
	public void deleteProduto() {
		this.produto = null;
	}
}
