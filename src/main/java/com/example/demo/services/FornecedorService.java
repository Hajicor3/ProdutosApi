package com.example.demo.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Fornecedor;
import com.example.demo.entities.dtos.FornecedorMenorDto;
import com.example.demo.repositories.FornecedorRepository;
import com.example.demo.services.exceptions.DataBaseException;
import com.example.demo.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FornecedorService {
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	public Fornecedor salvarFornecedor(Fornecedor fornecedor) {
		return fornecedorRepository.save(fornecedor);
	}
	
	public Fornecedor buscarFornecedores(Long id){
		Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);
		return fornecedor.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public List<FornecedorMenorDto> listaDeFornecedores(){
		List<FornecedorMenorDto> fornecedoresDto = fornecedorRepository.findAll().stream().map(x -> new FornecedorMenorDto(x.getNome())).toList();
		return fornecedoresDto;
	}
	
	public void atualizar(Long id, FornecedorMenorDto novo) {
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
			
			fornecedorRepository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException(e.getMessage());
		}
	}
	
	private void update(Fornecedor old, FornecedorMenorDto novo) {
		
		old.setNome(novo.getNome());
	}
}
