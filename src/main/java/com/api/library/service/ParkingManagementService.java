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

@Service
public class ParkingManagementService {
	
	@Autowired
	private Parking parking;
	
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
