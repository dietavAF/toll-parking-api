package com.api.library.exception;

public class WrongCarTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongCarTypeException(String type) {
		super("The car type '" + type + "' is not allowed");
	}
}
