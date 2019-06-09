package com.api.library.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.api.library.exception.CarAlreadyParkedException;
import com.api.library.exception.FullParkingException;
import com.api.library.exception.NoCarToLeaveException;

public abstract class ParkingSlot {

	protected Map<Car, Slot> parkSlotMap = new HashMap<Car, Slot>();

	public Integer freeTheSlot(Car carToLeave) throws NoCarToLeaveException {
		Slot slot = manageSlot(carToLeave, ActionOnSlot.LEAVE);
		if (slot != null) {
			return slot.getHoursSpentInParking();
		} else {
			throw new NoCarToLeaveException();
		} 
	}
	
	public void occupyTheSlot(Car carToPark) throws FullParkingException, CarAlreadyParkedException {
		if (!isFull()) {
			if (parkSlotMap.get(carToPark) != null) {
				throw new CarAlreadyParkedException(carToPark, "The car with this plate is already in the Parking");
			}
			manageSlot(carToPark, ActionOnSlot.PARK);
		} else {
			throw new FullParkingException(carToPark, "The Parking is FULL !");
		}
	}

	public boolean isFull() {
		return getAvailableSlots() == 0;
	}

	public Slot manageSlot(Car car, ActionOnSlot action) {
		Slot slot = null;
		if (action.equals(ActionOnSlot.PARK)) {
			slot = new Slot();
			slot.setStartPark(new Date());
			parkSlotMap.put(car, slot);
		} else {
			slot = parkSlotMap.get(car);
			if (slot != null) {
				parkSlotMap.remove(car);
				slot.setStopPark(new Date());
			}
		}
		return slot;
	}

	public Car getCarToLeave(String plate) {
		for(Car car: parkSlotMap.keySet()) {
			if (car.getPlate().equals(plate)) {
				return car;
			}
		}
		return null;
	}
	
	public abstract Car createCarToPark(String plate, CarType type);

	public abstract int getMaxNumberOfSlots();
	
	public abstract String getName();
	
	public abstract Integer getAvailableSlots();

}
