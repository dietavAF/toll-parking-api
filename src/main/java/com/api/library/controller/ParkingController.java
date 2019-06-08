package com.api.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.library.entity.CarType;
import com.api.library.exception.CarAlreadyParkedException;
import com.api.library.exception.FullParkingException;
import com.api.library.exception.NoCarToLeaveException;
import com.api.library.exception.WrongCarTypeException;
import com.api.library.response.Invoice;
import com.api.library.response.ParkResponse;
import com.api.library.response.ParkingStatus;
import com.api.library.service.ParkingManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/parking-toll/api/v1")
@Api(value = "Toll Parking", description = "The Toll Parking Management API")
public class ParkingController {

	@Autowired
	private ParkingManagementService parkingService;
	
	@ApiOperation(value= "Allows to park the car by its type")
	@ApiResponses(value = {
	        @ApiResponse(code = 201, message = "The car has been successfully parked"),
	        @ApiResponse(code = 400, message = "Bad Request on performing parking car api call"),
	        @ApiResponse(code = 406, message = "This parking api call operation cannot be performed")
	    })
	@PostMapping(value = "/car/{plate}/park", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ParkResponse> park(@ApiParam(value = "The car plate to park", required = true) @PathVariable("plate") String plate, 
			@ApiParam(value = "The type of the car which determines the parking slot to assign", required = true) @RequestParam("type") String carType) 
					throws CarAlreadyParkedException, FullParkingException, WrongCarTypeException {
		
		CarType type = null;
		ParkResponse response = null;
		HttpStatus httpStatus = HttpStatus.CREATED;
		try {
			type = CarType.valueOf(carType.toUpperCase());
			response = parkingService.parkCar(type, plate);
		} catch (IllegalArgumentException iae) {
			throw new WrongCarTypeException(carType.toUpperCase());
		}
		
		return new ResponseEntity<ParkResponse>(response, httpStatus);
	}
	
	@ApiOperation(value= "Allows to free the slot occupied by the car and get the bill")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "The car successfully left and get the bill"),
	        @ApiResponse(code = 400, message = "Bad Request on performing leaving car api call"),
	        @ApiResponse(code = 404, message = "The car has not been found")
	    })
	@PostMapping(value = "/car/{plate}/free", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Invoice> freeSlot(@ApiParam(value = "The car plate to leave", required = true) @PathVariable("plate") String plate, 
			@ApiParam(value = "The type of the car which determines the parking slot from which to take out the car", required = true) @RequestParam("type") String carType) 
					throws WrongCarTypeException, NoCarToLeaveException {
		CarType type = null;
		Invoice invoice = null;
		HttpStatus httpStatus = HttpStatus.OK;
		try {
			type = CarType.valueOf(carType.toUpperCase());
			invoice = parkingService.freeSlot(type, plate);
		} catch (IllegalArgumentException iae) {
			throw new WrongCarTypeException(carType.toUpperCase());
		}
		 
		return new ResponseEntity<Invoice>(invoice, httpStatus);
	}
	
	@ApiOperation(value= "Allows to get the number of cars for a given parking type")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "The information about the status of the parking have been successfully "),
	        @ApiResponse(code = 400, message = "Bad Request on performing leaving car api call")
	    })
	@GetMapping(value = "/car", produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<ParkingStatus> getNumberOfCarsByType(@ApiParam(value = "The type of the car which determines the parking slot from which to take status and available slots", required = true) @RequestParam("type") String carType) 
			throws WrongCarTypeException {
		CarType type = null;
		ParkingStatus status = null;
		HttpStatus httpStatus = HttpStatus.OK;
		try {
			type = CarType.valueOf(carType.toUpperCase());
			status = parkingService.getAvailableParkingSlots(type);
		} catch (IllegalArgumentException iae) {
			throw new WrongCarTypeException(carType.toUpperCase());
		}
		return new ResponseEntity<ParkingStatus>(status, httpStatus);
	}

}
