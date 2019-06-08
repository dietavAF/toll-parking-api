package com.api.library.response;

import com.api.library.entity.Car;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Details about the Parking Response when the car parks")
public class ParkResponse {
	
	@ApiModelProperty(notes = "The car which is parking")
	private Car car;
	
	@ApiModelProperty(notes = "The message for the car park")
	private String message;
	
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
