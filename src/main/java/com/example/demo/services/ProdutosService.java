package com.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Fornecedor;
import com.example.demo.entities.Produto;
import com.example.demo.entities.dtos.ProdutoDto;
import com.example.demo.entities.dtos.ProdutoModelDto;
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

	public Produto salvar(ProdutoDto produto) {
		try {
		Fornecedor fornecedor = fornecedorRepository.getReferenceById(produto.getFornecedorId());
		Produto novoProduto = new Produto(
				produto.getNomeProduto(),
				produto.getStatus(),
				produto.getFinalidade(),
				fornecedor);
			
		return ProdutosRepository.save(novoProduto);
		}
		catch(EntityNotFoundException e) {
			throw new DataBaseException(e.getMessage());
		}
	}
	
	public Produto produtoPorId(Long id) {
		Optional<Produto> produto = ProdutosRepository.findById(id);
		return produto.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public void excluirProduto(Long id) {
		try {
			
			if(!ProdutosRepository.existsById(id)) {
				throw new ResourceNotFoundException(id);
			}
			ProdutosRepository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException(e.getMessage());
		}
	}
	
	public void updateProduto(Long id,ProdutoModelDto novo) {
		try {
		Produto old = ProdutosRepository.getReferenceById(id);
		update(old, novo);
		ProdutosRepository.save(old);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	private void update(Produto old, ProdutoModelDto novo) {
		old.setFinalidade(novo.getFinalidade());
		old.setNomeProduto(novo.getNomeProduto());
		old.setStatus(novo.getStatus());
	}
}
