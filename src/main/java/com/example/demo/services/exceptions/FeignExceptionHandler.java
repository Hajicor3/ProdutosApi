package com.example.demo.services.exceptions;

public class FeignExceptionHandler extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public  FeignExceptionHandler(String msg) {
		super(msg);
	}

}
