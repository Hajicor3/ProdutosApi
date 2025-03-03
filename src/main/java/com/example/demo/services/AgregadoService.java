package com.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Agregado;
import com.example.demo.entities.dtos.AgregadoModelDto;
import com.example.demo.repositories.AgregadorRepository;
import com.example.demo.services.exceptions.DataBaseException;
import com.example.demo.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AgregadoService {
	
	@Autowired
	private AgregadorRepository agregadoRepository;
	
	public Agregado salvar(Agregado agregador) {
		try {
		return agregadoRepository.save(agregador);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException(e.getMessage());
		}
	}
	
	public Agregado BuscarId(Long id) {	
		Optional<Agregado> agregado = agregadoRepository.findById(id);
		return agregado.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public void delete(Long id) {
			try {
				
				if(!agregadoRepository.existsById(id)) {
					throw new ResourceNotFoundException(id);
				}
				agregadoRepository.deleteById(id);
			}
			catch(EmptyResultDataAccessException e) {
				throw new ResourceNotFoundException(id);
			}
	}
	
	public void atualizar(Long id, AgregadoModelDto novo) {
		try {
			Agregado old = agregadoRepository.getReferenceById(id);
			update(old, novo);
			agregadoRepository.save(old);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	private void update(Agregado old, AgregadoModelDto novo) {
		old.setDensidade(novo.getDensidade());
		old.setPercentual(novo.getPercentual());
	}
}
