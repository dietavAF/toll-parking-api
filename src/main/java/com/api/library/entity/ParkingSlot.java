package com.api.library.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.api.library.exception.CarAlreadyParkedException;
import com.api.library.exception.FullParkingException;
import com.api.library.exception.NoCarToLeaveException;

public abstract class ParkingSlot {

	protected Map<Car, Slot> parkSlotMap = new HashMap<Car, Slot>();
	
	/**
	 * Allows to free the slot when the freeSlot service will be called
	 * 
	 * @param carToLeave the car which is leaving the parking
	 * @return the number of hours spent in the parking
	 * @throws NoCarToLeaveException
	 * 				the exception thrown if carToLeave is not in the parking and no slot has been assigned then
	 */
	public Integer freeTheSlot(Car carToLeave) throws NoCarToLeaveException {
		Slot slot = manageSlot(carToLeave, ActionOnSlot.LEAVE);
		if (slot != null) {
			return slot.getHoursSpentInParking();
		} else {
			throw new NoCarToLeaveException();
		} 
	}
	
	/**
	 * Allows to park the car and occupy a slot
	 * 
	 * @param carToPark the car that enters in the parking and has to be parked
	 * @throws FullParkingException
	 * 				the exception thrown if the parking is full
	 * @throws CarAlreadyParkedException
	 * 				the exception thrown if this car (carToPark) has already been parked
	 */
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
	
	/**
	 * Allows to determine if the parking is full
	 * 
	 * @return true/false if the parking is full
	 */
	public boolean isFull() {
		return getAvailableSlots() == 0;
	}

	/**
	 * Allows to manage the slot
	 * If the action is to park, then a slot is created and assigned to the car with a start time of the park
	 * If the action is to leave, the the slot is retrieved and a end time of the park is assigned to calculate the toll
	 * 
	 * @param car the managed car
	 * @param action the action to perform (park/leave)
	 * @return
	 */
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

	/**
	 * Allows to retrieve the car to leave by its plate
	 * 
	 * @param plate the plate of the car to leave
	 * @return the car 
	 */
	public Car getCarToLeave(String plate) {
		for(Car car: parkSlotMap.keySet()) {
			if (car.getPlate().equals(plate)) {
				return car;
			}
		}
		return null;
	}
	
	/**
	 * Allows to create a car object by its plate and type
	 * 
	 * @param plate the plate of the car
	 * @param type the type of the car
	 * @return the created car
	 */
	public abstract Car createCarToPark(String plate, CarType type);

	/**
	 * Allows to determine the maximum number of slots of the parking by its type
	 * 
	 * @return the number of slots of the parking
	 */
	public abstract int getMaxNumberOfSlots();
	
	/**
	 * Get the name of the Parking
	 * 
	 * @return the name of the parking
	 */
	public abstract String getName();
	
	/**
	 * Get the number of available slots for the parking type
	 * 
	 * @return the number of available slots
	 */
	public abstract Integer getAvailableSlots();

}
