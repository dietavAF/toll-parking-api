package com.api.library.entity;

public class Electric50kwCar extends ElectricCar {
	
	private static final CarType ELECTRIC_50 = CarType.ELECTRIC_50;

	@Override
	public CarType getType() {
		return ELECTRIC_50;
	}

}
