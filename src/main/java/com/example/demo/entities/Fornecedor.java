package com.example.demo.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="tb_fornecedores")
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@FilterDef(name = "softDeleteFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "softDeleteFilter", condition = "deleted = :isDeleted")
public class Fornecedor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema
	private Long id;
	private String nome;
	private Boolean deleted = false;
	
	@Filter(name = "softDeleteFilter", condition = "deleted = :isDeleted")
	@OneToMany(mappedBy = "fornecedor",cascade = CascadeType.ALL,orphanRemoval = true)
	@Schema(hidden = true)
	private List<Produto> produtos = new ArrayList<>();
	
	public Fornecedor(String nome) {
		setNome(nome);
	}
	
	public void setNome(String nome) {
		if(nome == null || nome.trim().isEmpty()) {
			throw new IllegalArgumentException("O nome do fornecedor não pode ser nulo ou vazio!");
		}
		this.nome = nome;
	}
	
	public void adicionarProdutos(Produto produto) {
		Objects.requireNonNull(produto, "O produto não pode ser nulo!");
		if(!produtos.contains(produto)) {
			produtos.add(produto);
			produto.associarFornecedor(this);
		}
	}
	
	public void removerProdutos(Produto produto) {
		Objects.requireNonNull(produto, "O produto não pode ser nulo!");
		if(!produtos.remove(produto)) {
			produto.associarFornecedor(null);
		}
	}
	
	public List<Produto> getProdutos(){
		return Collections.unmodifiableList(produtos);
	}
	
	public void softDeleted() {
		this.deleted = true;
		produtos.forEach(x -> x.setDeleted(true));
	}
}
