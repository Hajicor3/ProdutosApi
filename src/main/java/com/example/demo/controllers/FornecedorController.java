package com.example.demo.controllers;

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

import com.example.demo.entities.Fornecedor;
import com.example.demo.entities.dtos.FornecedorRequest;
import com.example.demo.entities.dtos.FornecedorResponse;
import com.example.demo.services.FornecedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {
	
	private final FornecedorService fornecedorService;
	
	@Operation(description = "Salva um fornecedor no banco de dados")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Salva o fornecedor no banco de dados"),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos")
	})
	@PostMapping
	public ResponseEntity<Fornecedor> criarFornecedor(@RequestBody FornecedorRequest fornecedor){
		
		Fornecedor novoFornecedor = fornecedorService.salvarFornecedor(fornecedor);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novoFornecedor.getId()).toUri();
		return ResponseEntity.created(uri).body(novoFornecedor);
	}
	
	@Operation(description = "Resgata um fornecedor do banco de dados pelo id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna um fornecedor"),
			@ApiResponse(responseCode = "404", description = "Não existe fornecedor no id informado")
	})
	@GetMapping(value = "/{id}")
	public ResponseEntity<FornecedorResponse> fornecedorPorId(@PathVariable Long id){
		FornecedorResponse fornecedor = fornecedorService.buscarFornecedores(id);
		
		return ResponseEntity.ok().body(fornecedor);
	}
	
	@Operation(description = "Retorna uma lista de Dto´s de todos os fornecedores do banco. este Dto contem apenas o nome dos fornecedores")
	@ApiResponses(value = @ApiResponse(responseCode = "200",description = "Retorna uma lista de fornecedoresDto"))
	@GetMapping
	public ResponseEntity<List<FornecedorRequest>> listaFornecedores(){
		List<FornecedorRequest> fornecedorDto = fornecedorService.listaDeFornecedores();
		return ResponseEntity.ok().body(fornecedorDto);
	}
	
	@Operation(description = "Atualiza um fornecedor no banco de dados pelo id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Efetua a atualização do fornecedor no banco de dados"),
			@ApiResponse(responseCode = "404", description = "Não existe fornecedor no id informado"),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos")
	})
	@PutMapping(value = "/{id}")
	public ResponseEntity<Fornecedor> atualizarFornecedor(@PathVariable Long id,@RequestBody FornecedorRequest fornecedor){
		fornecedorService.atualizar(id, fornecedor);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(description = "Deleta um fornecedor do banco de dados pelo id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Efetua a exclusão do fornecedor"),
			@ApiResponse(responseCode = "404", description = "Não existe fornecedor no id informado"),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos")
	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Fornecedor> deletarFornecedor(@PathVariable Long id){
		fornecedorService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
