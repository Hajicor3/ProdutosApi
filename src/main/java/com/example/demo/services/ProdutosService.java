package com.example.demo.services;

import java.net.ConnectException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Fornecedor;
import com.example.demo.entities.Produto;
import com.example.demo.entities.dtos.EstoqueRequest;
import com.example.demo.entities.dtos.MovimentacaoRequest;
import com.example.demo.entities.dtos.ProdutoRequest;
import com.example.demo.entities.dtos.ProdutoResponse;
import com.example.demo.repositories.EstoqueRepository;
import com.example.demo.repositories.FornecedorRepository;
import com.example.demo.repositories.ProdutosRepository;
import com.example.demo.services.exceptions.DataBaseException;
import com.example.demo.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutosService {
	
	@Autowired
	private ProdutosRepository ProdutosRepository;
	@Autowired
	private FornecedorRepository fornecedorRepository;
	@Autowired
	private EstoqueRepository estoqueRepository;

	public Produto salvar(ProdutoRequest produto) throws ConnectException {
		try {
		Fornecedor fornecedor = fornecedorRepository.getReferenceById(produto.getFornecedorId());
		Produto novoProduto = new Produto(
				produto.getNomeProduto(),
				produto.getStatus(),
				produto.getFinalidade(),
				fornecedor);
		
		var produtoSalvo = ProdutosRepository.save(novoProduto);
		
		var estoque = EstoqueRequest
				.builder()
				.idProduto(produtoSalvo.getId())
				.idFornecedor(produtoSalvo.getFornecedor().getId())
				.build();
		
		estoqueRepository.salvarEstoque(estoque);
		
		return produtoSalvo;
		}
		catch(EntityNotFoundException e) {
			throw new DataBaseException(e.getMessage());
		}
	}
	
	public void registrarMovimentacao(MovimentacaoRequest movimentacaoRequest) {
		
		estoqueRepository.salvarMovimentacao(movimentacaoRequest);
	}
	
	public ProdutoResponse produtoPorId(Long id) {
		try {
		Produto produto = ProdutosRepository.getReferenceById(id);
		return ProdutoResponse
				.builder()
				.data(produto.getData())
				.finalidade(produto.getFinalidade())
				.nomeProduto(produto.getNomeProduto())
				.status(produto.getStatus())
				.id(produto.getId())
				.build();
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	public List<ProdutoResponse> resgatarListaDeProdutos(){
		List<ProdutoResponse> produtos = ProdutosRepository.findAll().stream().map(x -> ProdutoResponse
				.builder()
				.data(x.getData())
				.finalidade(x.getFinalidade())
				.id(x.getId())
				.nomeProduto(x.getNomeProduto())
				.status(x.getStatus())
				.build()).toList();
		return produtos;
	}
	
	public void excluirProduto(Long id) {
		try {
			
			if(!ProdutosRepository.existsById(id)) {
				throw new ResourceNotFoundException(id);
			}
			estoqueRepository.deletarEstoquePorIdProduto(id);
			ProdutosRepository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException(e.getMessage());
		}
	}
	
	public void updateProduto(Long id,ProdutoResponse novo) {
		try {
		Produto old = ProdutosRepository.getReferenceById(id);
		update(old, novo);
		ProdutosRepository.save(old);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	private void update(Produto old, ProdutoResponse novo) {
		old.setFinalidade(novo.getFinalidade());
		old.setNomeProduto(novo.getNomeProduto());
		old.setStatus(novo.getStatus());
	}
}
