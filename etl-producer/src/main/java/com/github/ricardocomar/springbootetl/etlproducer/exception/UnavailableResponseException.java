package com.github.ricardocomar.springbootetl.etlproducer.exception;

public class UnavailableResponseException extends Exception {

	private static final long serialVersionUID = -2167596404229805642L;

	public UnavailableResponseException() {
		super();
	}
	
	public UnavailableResponseException(String msg) {
		super(msg);
	}

	public UnavailableResponseException(Throwable cause) {
		super(cause);
	}
	
	public UnavailableResponseException(String msg, Throwable cause) {
		super(msg, cause);
	}

	
	
	
	
}
