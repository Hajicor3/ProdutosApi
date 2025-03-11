package com.example.demo.controllers.exceptions;

import java.net.ConnectException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.services.exceptions.DataBaseException;
import com.example.demo.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> ResourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
		
		String error= "Recurso não encontrado!";
		HttpStatus status = HttpStatus.NOT_FOUND ;
		StandardError err = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandardError> database(DataBaseException e, HttpServletRequest request){
		
		String error = "Erro no banco de dados!";
		HttpStatus status = HttpStatus.BAD_REQUEST ;
		StandardError err = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	 @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<StandardError> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
		 	String error = "Request inválido";
		 	HttpStatus status = HttpStatus.BAD_REQUEST;
			StandardError err = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
			return ResponseEntity.status(status).body(err);
	 	}
	 
	 @ExceptionHandler(ConnectException.class)
	 public ResponseEntity<StandardError> handleConnectionException(ConnectException e, HttpServletRequest request){
		 String error = "Falha na conexão com o serviço";
		 HttpStatus status = HttpStatus.BAD_GATEWAY;
		 StandardError err = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
		 return ResponseEntity.status(status).body(err);
	 }
}
