package com.api.library.entity;

public class Electric20kwCar extends ElectricCar {
	
	private static final CarType ELECTRIC_20 = CarType.ELECTRIC_20;

	@Override
	public CarType getType() {
		return ELECTRIC_20;
	}

}
