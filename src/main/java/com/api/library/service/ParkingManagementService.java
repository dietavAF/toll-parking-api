package com.api.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.library.entity.Car;
import com.api.library.entity.CarType;
import com.api.library.entity.Parking;
import com.api.library.entity.ParkingSlot;
import com.api.library.exception.CarAlreadyParkedException;
import com.api.library.exception.FullParkingException;
import com.api.library.exception.NoCarToLeaveException;
import com.api.library.response.Invoice;
import com.api.library.response.ParkResponse;
import com.api.library.response.ParkingStatus;

/**
 * The Parking Management Service
 * 
 * @author diegotavolaro
 *
 */
@Service
public class ParkingManagementService {
	
	@Autowired
	private Parking parking;
	
	/**
	 * The service to park the car arriving at the parking
	 * The response will be the status of the parking
	 * 
	 * @param carType the type of the car
	 * @param plate the plate of the car to identify it
	 * @return the model response as a ParkResponse object
	 * @throws CarAlreadyParkedException
	 * 				the exception thrown if the car is already parked
	 * @throws FullParkingException
	 * 				the exception thrown if the parking is full
	 */
	public ParkResponse parkCar(CarType carType, String plate) throws CarAlreadyParkedException, FullParkingException {
		ParkingSlot ps = parking.determineParkingSlot(carType);
		Car carToPark = ps.createCarToPark(plate, carType);
		try {
			ps.occupyTheSlot(carToPark);
		} catch (FullParkingException fpe) {
			throw new FullParkingException(fpe.getCarToPark(), fpe.getMessage());
		} catch (CarAlreadyParkedException cape) {
			throw new CarAlreadyParkedException(cape.getCarToPark(), cape.getMessage());
		}
		
		ParkResponse response = new ParkResponse();
		response.setCar(carToPark);
		response.setMessage("The car has been parked");
		return response;
		
	}
	
	/**
	 * The service to free the slot and pay the toll with an invoice
	 * The response will be the invoice with the hours spent in the parking and the toll
	 * 
	 * @param carType the type of the car
	 * @param plate the plate of the car to identify it
	 * @return the model response as a Invoice object
	 * @throws NoCarToLeaveException
	 * 				the exception thrown if input of the API identifies a car which is not in the parking
	 */
	public Invoice freeSlot(CarType carType, String plate) throws NoCarToLeaveException {
		ParkingSlot ps = parking.determineParkingSlot(carType);
		Car carToLeave = ps.getCarToLeave(plate);
		Invoice invoice = new Invoice();
		Integer hoursSpentInParking = null;
		try {
			hoursSpentInParking = ps.freeTheSlot(carToLeave);
		} catch (NoCarToLeaveException nctle) {
			throw new NoCarToLeaveException(plate);
		}
		invoice.setCar(carToLeave);
		invoice.setHoursSpentInParking(hoursSpentInParking);
		invoice.setBill(parking.getPricingPolicy().getBill(hoursSpentInParking));
		return invoice;
	}
	
	/**
	 * The service to get the status of the parking based on the car type
	 * The response will be the status of the Parking with the available slots and occupied slots
	 * 
	 * @param carType the type of the car to identify the parking 
	 * @return the model response as a ParkingStatus object
	 */
	public ParkingStatus getAvailableParkingSlots(CarType carType) {
		ParkingSlot parkingSlot = parking.determineParkingSlot(carType);
		int availableSlots = parkingSlot.getAvailableSlots();
		ParkingStatus status = new ParkingStatus();
		status.setName(parkingSlot.getName());
		status.setAvailableSlots(availableSlots);
		status.setOccupiedSlots(parkingSlot.getMaxNumberOfSlots() - availableSlots);
		return status;
	}
}
