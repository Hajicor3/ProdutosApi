package com.example.demo.controllers;

import java.net.URI;

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

import com.example.demo.entities.Agregado;
import com.example.demo.entities.dtos.AgregadoResponse;
import com.example.demo.services.AgregadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/agregadores")
@RequiredArgsConstructor
public class AgregadoController {
	
	private final AgregadoService agregadoService;
	
	@Operation(description = "Salva um agregado no banco de dados")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Salva o agregado no banco de dados"),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos")
	})
	@PostMapping
	public ResponseEntity<Agregado> criarAgregador(@RequestBody Agregado agregador) {
		
		Agregado novoAgregador = agregadoService.salvar(agregador);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(agregador.getId()).toUri();
		
		return ResponseEntity.created(uri).body(novoAgregador);
		
	}
	
	@Operation(description = "Resgata um agregado do banco de dados pelo id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna um agregado"),
			@ApiResponse(responseCode = "404", description = "Não existe agregado no id informado")
	})
	@GetMapping(value = "/{id}")
	public ResponseEntity<Agregado> buscarPorId(@PathVariable Long id) {
		Agregado agregador = agregadoService.BuscarId(id);
		
		return ResponseEntity.ok().body(agregador);
	}
	
	@Operation(description = "Atualiza um agregado no banco de dados pelo id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Efetua a atualização do agregado no banco de dados"),
			@ApiResponse(responseCode = "404", description = "Não existe agregado no id informado"),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos")
	})
	@PutMapping(value = "/{id}")
	public ResponseEntity<Agregado> atualizarAgreagado(@PathVariable Long id, @RequestBody AgregadoResponse agregado){
		agregadoService.atualizar(id, agregado);
		return ResponseEntity.noContent().build();	
	}
	
	@Operation(description = "Deleta um agregado do banco de dados pelo id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Efetua a exclusão do agregado"),
			@ApiResponse(responseCode = "404", description = "Não existe agregado no id informado"),
			@ApiResponse(responseCode = "400", description = "Parametros inválidos")
	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteAgregador(@PathVariable Long id){
		agregadoService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
