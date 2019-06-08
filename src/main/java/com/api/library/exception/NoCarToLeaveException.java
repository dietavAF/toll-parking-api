package com.api.library.exception;

public class NoCarToLeaveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoCarToLeaveException() {
		super();
	}
	
	public NoCarToLeaveException(String plate) {
		super("There is no car to leave with plate " + plate);
	}

}
