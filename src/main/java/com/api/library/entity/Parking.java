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
	
	/**
	 * Allows to determine the Parking Slots for a specified car type
	 * 
	 * @param carType the type of the car to be parked
	 * @return the Parking Slot related to the car to park
	 */
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
	
	/**
	 * Get the pricing policy
	 * 
	 * @return the pricing policy of the parking
	 */
	public PricingPolicy getPricingPolicy() {
		return pricingPolicy;
	}
}
