package com.example.demo.repositories;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entities.Movimentacao;
import com.example.demo.entities.dtos.EstoqueRequest;
import com.example.demo.entities.dtos.MovimentacaoRequest;

@FeignClient(name = "apiestoque", url = "${api.estoque.url}")
public interface EstoqueRepository {

	@PostMapping("/estoques")
	public EstoqueRequest salvarEstoque(@RequestBody EstoqueRequest estoque);
	
	@DeleteMapping("estoques/{id}")
	public ResponseEntity<Void> deletarEstoquePorIdProduto(@PathVariable Long id);
	
	@PostMapping("/movimentacoes")
	public ResponseEntity<Movimentacao> salvarMovimentacao(MovimentacaoRequest movimentacaoRequest);
	
	@GetMapping(value = "estoques/qntd/produto/{id}")
	public ResponseEntity<Long> quantidadeEmEstoquePorIdProduto(@PathVariable Long id);
	
	@GetMapping(value = "estoques/qntd/produto/list")
	public ResponseEntity<Map<Long,Long>> produtoQuantidadeLista();
	
	@DeleteMapping(value = "estoques/fornecedor/{id}")
	public ResponseEntity<Void> deletarEstoquesDeFornecedor(@PathVariable Long id);
	
	@PutMapping(value = "movimentacoes/{id}")
	public ResponseEntity<Void> cancelarMovimentacaoPorid(@PathVariable Long id);
}
