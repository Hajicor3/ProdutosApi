package com.example.demo.services;

import java.net.ConnectException;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Fornecedor;
import com.example.demo.entities.Movimentacao;
import com.example.demo.entities.Produto;
import com.example.demo.entities.dtos.EstoqueRequest;
import com.example.demo.entities.dtos.MovimentacaoRequest;
import com.example.demo.entities.dtos.ProdutoRequest;
import com.example.demo.entities.dtos.ProdutoResponse;
import com.example.demo.repositories.EstoqueRepository;
import com.example.demo.repositories.FornecedorRepository;
import com.example.demo.repositories.ProdutosRepository;
import com.example.demo.repositories.mqueues.ProdutoEstoqueQueuePublisher;
import com.example.demo.services.exceptions.DataBaseException;
import com.example.demo.services.exceptions.FeignExceptionHandler;
import com.example.demo.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

import feign.FeignException.FeignClientException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutosService {
	
	private final ProdutosRepository ProdutosRepository;
	private final FornecedorRepository fornecedorRepository;
	private final EstoqueRepository estoqueRepository;
	private final ProdutoEstoqueQueuePublisher estoqueQueuePublisher;

	@Transactional
	public ProdutoResponse salvar(ProdutoRequest produto) throws ConnectException {
		try {
			Fornecedor fornecedor = fornecedorRepository.getReferenceById(produto.getFornecedorId());
			Produto novoProduto = new Produto(
					produto.getNomeProduto(),
					produto.getPreco(),
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
			
			return ProdutoResponse
					.builder()
					.nomeProduto(produtoSalvo.getNomeProduto())
					.finalidade(produtoSalvo.getFinalidade())
					.data(produtoSalvo.getData())
					.id(produtoSalvo.getId())
					.preco(produtoSalvo.getPreco())
					.status(produtoSalvo.getStatus())
					.build();
		}
		catch(EntityNotFoundException e) {
			throw new DataBaseException(e.getMessage());
		}
		catch(NullPointerException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	@Transactional
	public Movimentacao registrarMovimentacao(MovimentacaoRequest movimentacaoRequest) {
		try {
			var mov = estoqueRepository.salvarMovimentacao(movimentacaoRequest).getBody();
			return mov;
		}
		catch(FeignClientException e) {
			throw new FeignExceptionHandler(e.status(),e.getMessage());
		}
	}
	
	@Transactional
	public void cancelarMovimentacao(Long id) throws JsonProcessingException {
		try {
			estoqueQueuePublisher.SolicitarcancelamentoMovimentacao(id);
		}
		catch(FeignClientException e) {
			throw new FeignExceptionHandler(e.status(),e.getMessage());
		}
	}
	
	@Transactional
	public Movimentacao resgatarMovimentacaoPorId(Long id) {
		try {
			return estoqueRepository.buscarMovimentacaoPorId(id).getBody();
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
		catch(NullPointerException e) {
			throw new ResourceNotFoundException(id);
		}
		catch(FeignClientException e) {
			throw new FeignExceptionHandler(e.status(),e.getMessage());
		}
	}
	
	@Transactional
	public List<Movimentacao> resgatarTodasMovimentacoes() {
		return estoqueRepository.resgatarMovimentacoes().getBody();
	}
	
	@Transactional
	public List<Movimentacao> resgatarTodasMovimentacoesPorId(Long id) {
		return estoqueRepository.resgatarMovimentacoesPorIdProduto(id).getBody();
	}
	
	@Transactional
	public ProdutoResponse produtoPorId(Long id) {
		try {
			Produto produto = ProdutosRepository.getReferenceById(id);
			Long qntd = estoqueRepository.quantidadeEmEstoquePorIdProduto(id).getBody();
			return ProdutoResponse
					.builder()
					.data(produto.getData())
					.finalidade(produto.getFinalidade())
					.nomeProduto(produto.getNomeProduto())
					.preco(produto.getPreco())
					.status(produto.getStatus())
					.id(produto.getId())
					.quantidade(qntd)
					.build();
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
		catch(NullPointerException e) {
			throw new ResourceNotFoundException(id);
		}
		catch(FeignClientException e) {
			throw new FeignExceptionHandler(e.status(),e.getMessage());
		}
	}
	
	@Transactional
	public List<ProdutoResponse> resgatarListaDeProdutos(){
		
		var listaDeQuantidadePorProduto = estoqueRepository.produtoQuantidadeLista().getBody();
		
		List<ProdutoResponse> produtos = ProdutosRepository.findAll().stream().map(x -> ProdutoResponse
				.builder()
				.data(x.getData())
				.finalidade(x.getFinalidade())
				.id(x.getId())
				.nomeProduto(x.getNomeProduto())
				.preco(x.getPreco())
				.status(x.getStatus())
				.quantidade(listaDeQuantidadePorProduto.getOrDefault(x.getId(), 0L))
				.build())
			.toList();
		return produtos;
	}
	
	@Transactional
	public void excluirProduto(Long id) {
		try {
			
			Produto prod = ProdutosRepository.getReferenceById(id);
			estoqueRepository.deletarEstoquePorIdProduto(id);
			ProdutosRepository.delete(prod);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException(e.getMessage());
		}
		catch(FeignClientException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}
	
	@Transactional
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
	
	@Transactional
	private void update(Produto old, ProdutoResponse novo) {
		old.setFinalidade(novo.getFinalidade());
		old.setNomeProduto(novo.getNomeProduto());
		old.setStatus(novo.getStatus());
		old.setPreco(novo.getPreco());
	}
}
