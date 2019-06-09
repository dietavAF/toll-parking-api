package com.api.library.entity;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.api.library.utils.ParkingTollConstants;

@Component
@Qualifier("elec20")
public class Electric20ParkingSlot extends ParkingSlot {
	 
	@Value("${electric20.parking.slots}")
	private int maxElec20Slots;
	
	@Override
	public Car createCarToPark(String plate, CarType type) {
		Car carToPark = new Electric20kwCar();
		carToPark.setPlate(plate);
		return carToPark;
	}

	public int getMaxNumberOfSlots() {
		return maxElec20Slots;
	}

	@Override
	public String getName() {
		return ParkingTollConstants.ELECTRIC_20_PARKING_SLOT_NAME;
	}

	@Override
	public Integer getAvailableSlots() {
		return maxElec20Slots - parkSlotMap.size();
	}

}
