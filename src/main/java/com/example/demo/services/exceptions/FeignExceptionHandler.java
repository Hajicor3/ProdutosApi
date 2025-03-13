package com.example.demo.services.exceptions;

import lombok.Getter;

public class FeignExceptionHandler extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	@Getter
	private String error;
	
	public  FeignExceptionHandler(String error, String msg) {
		super(msg);
		this.error = error;
	}

}
