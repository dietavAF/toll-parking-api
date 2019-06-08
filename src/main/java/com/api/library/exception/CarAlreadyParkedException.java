package com.api.library.exception;

import com.api.library.entity.Car;

public class CarAlreadyParkedException extends Exception {
	
	private Car carToPark;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CarAlreadyParkedException(Car car, String message) {
		super(message);
		this.carToPark = car;
	}
	
	public Car getCarToPark() {
		return carToPark;
	}
	
}
