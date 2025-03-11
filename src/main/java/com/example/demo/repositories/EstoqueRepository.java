package com.example.demo.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entities.dtos.EstoqueRequest;
import com.example.demo.entities.dtos.MovimentacaoRequest;

@FeignClient(name = "apiestoque", url = "http://localhost:8081")
public interface EstoqueRepository {

	@PostMapping("/estoques")
	public EstoqueRequest salvarEstoque(@RequestBody EstoqueRequest estoque);
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarEstoquePorIdProduto(@PathVariable("id") Long idProduto);
	
	@PostMapping("/movimentacoes")
	public ResponseEntity<Void> salvarMovimentacao(MovimentacaoRequest movimentacaoRequest);
}
