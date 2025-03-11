package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
	
	@Query("SELECT f FROM Fornecedor f WHERE f.deleted = false")
	List<Fornecedor> findAll();
	
	@Query("SELECT f FROM Fornecedor f WHERE f.deleted = false")
	Fornecedor getReferenceById(Long id);
}
