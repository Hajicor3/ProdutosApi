package com.example.demo.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Fornecedor;
import com.example.demo.entities.dtos.FornecedorRequest;
import com.example.demo.entities.dtos.FornecedorResponse;
import com.example.demo.repositories.EstoqueRepository;
import com.example.demo.repositories.FornecedorRepository;
import com.example.demo.services.exceptions.DataBaseException;
import com.example.demo.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FornecedorService {
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	@Autowired
	private EstoqueRepository estoqueRepository;
	
	public Fornecedor salvarFornecedor(FornecedorRequest fornecedorDto) {
		Fornecedor fornecedor = new Fornecedor(fornecedorDto.getNome());
		return fornecedorRepository.save(fornecedor);
	}
	
	public FornecedorResponse buscarFornecedores(Long id){
		
		try {
			Fornecedor fornecedor = fornecedorRepository.getReferenceById(id);
			return FornecedorResponse
					.builder()
					.nome(fornecedor.getNome())
					.id(fornecedor.getId())
					.produtos(fornecedor.getProdutos())
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
