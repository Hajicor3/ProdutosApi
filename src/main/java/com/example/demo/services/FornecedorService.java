package com.example.demo.services;


import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Fornecedor;
import com.example.demo.entities.dtos.FornecedorRequest;
import com.example.demo.entities.dtos.FornecedorResponse;
import com.example.demo.entities.dtos.ProdutoResponse;
import com.example.demo.repositories.EstoqueRepository;
import com.example.demo.repositories.FornecedorRepository;
import com.example.demo.services.exceptions.DataBaseException;
import com.example.demo.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FornecedorService {
	
	private final FornecedorRepository fornecedorRepository;
	private final EstoqueRepository estoqueRepository;
	
	public FornecedorResponse salvarFornecedor(FornecedorRequest fornecedorDto) {
		Fornecedor fornecedor = new Fornecedor(fornecedorDto.getNome());
		var salvo = fornecedorRepository.save(fornecedor);
		var produtos = fornecedor.getProdutos().stream().map(x -> ProdutoResponse
				.builder()
				.data(x.getData())
				.finalidade(x.getFinalidade())
				.id(x.getId())
				.nomeProduto(x.getNomeProduto())
				.preco(x.getPreco())
				.quantidade(0L)
				.status(x.getStatus())
				.build())
				.toList();
		
		return FornecedorResponse
				.builder()
				.nome(salvo.getNome())
				.id(salvo.getId())
				.produtos(produtos)
				.build();
	}
	
	public FornecedorResponse buscarFornecedores(Long id){
		
		try {
			Fornecedor fornecedor = fornecedorRepository.getReferenceById(id);
			var quantidade = estoqueRepository.encontrarEstoquesPorIdFornecedor(id).getBody();
			var produtos = fornecedor.getProdutos().stream().map(x -> ProdutoResponse
					.builder()
					.data(x.getData())
					.finalidade(x.getFinalidade())
					.id(x.getId())
					.nomeProduto(x.getNomeProduto())
					.preco(x.getPreco())
					.quantidade(quantidade.getOrDefault(x.getId(), 0L))
					.status(x.getStatus())
					.build())
					.toList();
			
			return FornecedorResponse
					.builder()
					.nome(fornecedor.getNome())
					.id(fornecedor.getId())
					.produtos(produtos)
					.build();
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
		catch(NullPointerException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	public List<FornecedorRequest> listaDeFornecedores(){
		List<FornecedorRequest> fornecedoresDto = fornecedorRepository.findAll().stream().map(x -> FornecedorRequest
				.builder()
				.id(x.getId())
				.nome(x.getNome())
				.build())
				.toList();
		return fornecedoresDto;
	}
	
	public void atualizar(Long id, FornecedorRequest novo) {
		try {
			Fornecedor old = fornecedorRepository.getReferenceById(id);
			update(old, novo);
			fornecedorRepository.save(old);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	public void delete(Long id) {
		try {
			
			if(!fornecedorRepository.existsById(id)) {
				throw new ResourceNotFoundException(id);
			}
			
			var fornecedor = fornecedorRepository.getReferenceById(id);
			fornecedor.softDeleted();
			estoqueRepository.deletarEstoquesDeFornecedor(id);
			fornecedorRepository.save(fornecedor);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException(e.getMessage());
		}
	}
	
	private void update(Fornecedor old, FornecedorRequest novo) {
		
		old.setNome(novo.getNome());
	}
}
