package com.api.library.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Parking {
	
	@Autowired
	private PricingPolicy pricingPolicy;
	
	@Autowired @Qualifier("sedan")
	private SedanParkingSlot sedanParkingSlot;
	
	@Autowired @Qualifier("elec20")
	private Electric20ParkingSlot elec20ParkingSlot;
	
	@Autowired @Qualifier("elec50")
	private Electric50ParkingSlot elec50ParkingSlot;
	
	public ParkingSlot determineParkingSlot(CarType carType) {
		if (carType.equals(CarType.SEDAN)) {
			return sedanParkingSlot;
		}
		if (carType.equals(CarType.ELECTRIC_20)) {
			return elec20ParkingSlot;
		}
		if (carType.equals(CarType.ELECTRIC_50)) {
			return elec50ParkingSlot;
		}
		return null;
	}
	
	public PricingPolicy getPricingPolicy() {
		return pricingPolicy;
	}
}
