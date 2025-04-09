package com.example.demo.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.annotations.Filter;

import com.example.demo.entities.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="tb_produtos")
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Filter(name = "softDeleteFilter", condition = "deleted = :isDeleted")
public class Produto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nomeProduto;
	private Double preco;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd/MM/yyyy")
	@Schema(hidden = true)
	private LocalDate data;
	private Status status;
	private String finalidade;
	private Boolean deleted = false;
	@ManyToOne
	@JoinColumn(name = "fornecedor_id")
	@JsonIgnore
	private Fornecedor fornecedor;
	
	public Produto(String nomeProduto,Double preco, Status status, String finalidade, Fornecedor fornecedor) {
		setNomeProduto(nomeProduto);
		setPreco(preco);
		this.data = LocalDate.now();
		setStatus(status);
		setFinalidade(finalidade);
		associarFornecedor(fornecedor);
	}
	
	public void setNomeProduto(String nomeProduto) {
		if(nomeProduto == null || nomeProduto.trim().isEmpty()) {
			throw new IllegalArgumentException("O nome do produto não pode ser nulo ou vazio !");
		}
		this.nomeProduto = nomeProduto;
	}
	
	public void setPreco(Double preco) {
		if(preco <= 0 || preco == null) {
			throw new IllegalArgumentException("O nome do produto não pode ter o preço menor ou igual a zero!");
		}
		this.preco = preco;
	}
	
	public void setStatus(Status status) {
		Objects.requireNonNull(status, "O status não pode ser nulo!");
		this.status = status;
	}
	
	public void setFinalidade(String finalidade) {
		if(finalidade == null || finalidade.trim().isEmpty()) {
			throw new IllegalArgumentException("O nome do produto não pode ser nulo ou vazio !");
		}
		this.finalidade = finalidade;
	}
	
	public void associarFornecedor(Fornecedor fornecedor) {
		Objects.requireNonNull(fornecedor, "O fornecedor não pode ser nulo!");
		if(this.fornecedor != fornecedor) {
			this.fornecedor = fornecedor;
			fornecedor.adicionarProdutos(this);
		}
	}
	
	public void removerFornecedor() {
		if(this.fornecedor != null) {
			Fornecedor fornecedorAnterior = this.fornecedor;
			this.fornecedor = null;
			fornecedorAnterior.removerProdutos(this);
		}
	}
	
	public void setDeleted(boolean deleted) {
		
		this.deleted = deleted;
	}
}
