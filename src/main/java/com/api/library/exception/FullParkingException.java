package com.api.library.exception;

import com.api.library.entity.Car;

public class FullParkingException extends Exception {
	
	private Car carToPark;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FullParkingException(Car car, String reason) {
		super(reason);
		this.carToPark = car;
	}
	
	public Car getCarToPark() {
		return carToPark;
	}
	

}
