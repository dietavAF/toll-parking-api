package com.api.library.entity;

public class SedanCar extends Car {
	
	private static final CarType SEDAN = CarType.SEDAN;

	@Override
	public CarType getType() {
		return SEDAN;
	}
	
	

}
