package com.example.demo.controllers;

import java.net.ConnectException;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.entities.Movimentacao;
import com.example.demo.entities.Produto;
import com.example.demo.entities.dtos.MovimentacaoRequest;
import com.example.demo.entities.dtos.ProdutoRequest;
import com.example.demo.entities.dtos.ProdutoResponse;
import com.example.demo.services.ProdutosService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/produtos")
@RequiredArgsConstructor
public class ProdutosController {
	
	private final ProdutosService produtosService;
	
	@Operation(description = "Salva um produto no banco de dados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Salva o produto no banco de dados."),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos.")
	})
	@PostMapping
	public ResponseEntity<ProdutoResponse> salvarProduto(@RequestBody ProdutoRequest produto) throws ConnectException{
		
		ProdutoResponse prod = produtosService.salvar(produto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(prod.getId()).toUri();
		return ResponseEntity.created(uri).body(prod);
	}
	
	@Operation(description = "Solicita o salvamento de uma movimentação na api de estoque.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Salva uma movimentação na api de estoque."),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos.")
	})
	@PostMapping(value = "/movimentacao")
	public ResponseEntity<Movimentacao> registrarMovimentacaoDeProduto(@RequestBody MovimentacaoRequest movimentacaoRequest) {
		var mov = produtosService.registrarMovimentacao(movimentacaoRequest);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.created(uri).body(mov);
	}
	
	@Operation(description = "Cancela uma movimentacao já feita, pelo id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "cancela uma movimentacao."),
			@ApiResponse(responseCode = "404", description = "Não existe movimentacao no id informado.")
	})
	@PutMapping(value = "/movimentacao/{id}")
	public ResponseEntity<Void> cancelarMovimentacaoPorId(@PathVariable Long id) throws JsonProcessingException{
		produtosService.cancelarMovimentacao(id);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(description = "Retorna uma lista de Dto´s de todas as movimentações do banco.")
	@ApiResponses(value = @ApiResponse(responseCode = "200",description = "Retorna uma lista de todas as movimentações."))
	@GetMapping(value = "/movimentacao")
	public ResponseEntity<List<Movimentacao>> resgatarTodasMovimentacoes(){
		var movimentacoes = produtosService.resgatarTodasMovimentacoes();
		return ResponseEntity.ok().body(movimentacoes);
	}
	
	@Operation(description = "Resgata uma movimentação do banco de dados pelo id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna uma movimentação."),
			@ApiResponse(responseCode = "404", description = "Não existe movimentação no id informado.")
	})
	@GetMapping(value = "movimentacao/{id}")
	public ResponseEntity<Movimentacao> buscarMovimentacaoPorId(@PathVariable Long id){
		var mov = produtosService.resgatarMovimentacaoPorId(id);
		return ResponseEntity.ok().body(mov);
	}
	
	@Operation(description = "Retorna uma lista de Dto´s de todas as movimentações de um produto.")
	@ApiResponses(value = @ApiResponse(responseCode = "200",description = "Retorna uma lista de todas as movimentações."))
	@GetMapping(value = "movimentacao/list/produto/{id}")
	public ResponseEntity<List<Movimentacao>> resgatarMovimentacoesPorIdProduto(@PathVariable Long id){
		var movimentacoes = produtosService.resgatarTodasMovimentacoesPorId(id);
		return ResponseEntity.ok().body(movimentacoes);
	}

	@Operation(description = "Resgata um produto do banco de dados pelo id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna um produto."),
			@ApiResponse(responseCode = "404", description = "Não existe produto no id informado.")
	})
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProdutoResponse> pegarProduto(@PathVariable Long id){
		
		ProdutoResponse produto = produtosService.produtoPorId(id);
		return ResponseEntity.ok().body(produto);
	}
	
	@Operation(description = "Retorna uma lista de Dto´s de todos os produtos do banco.")
	@ApiResponses(value = @ApiResponse(responseCode = "200",description = "Retorna uma lista de fornecedoresDto."))
	@GetMapping
	public ResponseEntity<List<ProdutoResponse>> listaDeProdutos(){
		List<ProdutoResponse> produtos = produtosService.resgatarListaDeProdutos();
		return ResponseEntity.ok().body(produtos);
	}
	
	@Operation(description = "Atualiza um produto no banco de dados pelo id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Efetua a atualização do produto no banco de dados."),
			@ApiResponse(responseCode = "404", description = "Não existe produto no id informado."),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos.")
	})
	@PutMapping(value = "/{id}")
	public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoResponse produto){
		
		produtosService.updateProduto(id, produto);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(description = "Deleta um produto do banco de dados pelo id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Efetua a exclusão do produto."),
			@ApiResponse(responseCode = "404", description = "Não existe produto no id informado."),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos.")
	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteProdutoPorId(@PathVariable Long id){
		produtosService.excluirProduto(id);
		return ResponseEntity.noContent().build();
	}
}
