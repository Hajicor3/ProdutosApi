package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Produto;

public interface ProdutosRepository extends JpaRepository<Produto, Long> {

	@Query("SELECT f FROM Produto f WHERE f.deleted = false")
	List<Produto> findAll();
	
	@Query("SELECT f FROM Produto f WHERE f.id = :id AND f.deleted = false")
	Produto getReferenceById(Long id);
}
