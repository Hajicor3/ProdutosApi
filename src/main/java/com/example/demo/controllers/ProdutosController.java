package com.example.demo.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.entities.Produto;
import com.example.demo.entities.dtos.ProdutoDto;
import com.example.demo.services.ProdutosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutosController {
	
	@Autowired
	private ProdutosService ProdutosService;
	
	@Operation(description = "Salva um produto no banco de dados")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Salva o produto no banco de dados"),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos")
	})
	@PostMapping
	public ResponseEntity<Produto> salvarProduto(@RequestBody ProdutoDto produto) {
		
		Produto prod = ProdutosService.salvar(produto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(prod.getId()).toUri();
		return ResponseEntity.created(uri).body(prod);
	}
	
	@Operation(description = "Resgata um produto do banco de dados pelo id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna um produto"),
			@ApiResponse(responseCode = "404", description = "Não existe produto no id informado")
	})
	@GetMapping(params = "id")
	public ResponseEntity<Produto> pegarProduto(@RequestParam Long id){
		
		Produto produto = ProdutosService.produtoPorId(id);
		return ResponseEntity.ok().body(produto);
	}
	
	@Operation(description = "Atualiza um produto no banco de dados pelo id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Efetua a atualização do produto no banco de dados"),
			@ApiResponse(responseCode = "404", description = "Não existe produto no id informado"),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos")
	})
	@PutMapping(params = "id")
	public ResponseEntity<Produto> atualizarProduto(@RequestParam Long id, @RequestBody Produto produto){
		
		ProdutosService.updateProduto(id, produto);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(description = "Deleta um produto do banco de dados pelo id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Efetua a exclusão do produto"),
			@ApiResponse(responseCode = "404", description = "Não existe produto no id informado"),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos")
	})
	@DeleteMapping(params = "id")
	public ResponseEntity<Void> deleteProdutoPorId(@RequestParam Long id){
		ProdutosService.excluirProduto(id);
		return ResponseEntity.noContent().build();
	}
}
