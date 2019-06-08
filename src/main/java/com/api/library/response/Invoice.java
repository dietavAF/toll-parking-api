package com.api.library.response;

import com.api.library.entity.Car;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Details about the Invoice for the car which leaves the parking")
public class Invoice {
	
	@ApiModelProperty(notes = "The car which leaves the parking")
	private Car car;
	
	@ApiModelProperty(notes = "The bill for the car which leaves the parking")
	private Integer bill;
	
	@ApiModelProperty(notes = "The hours spent by the car in the parking")
	private Integer hoursSpentInParking;
	
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Integer getBill() {
		return bill;
	}

	public void setBill(Integer bill) {
		this.bill = bill;
	}

	public Integer getHoursSpentInParking() {
		return hoursSpentInParking;
	}

	public void setHoursSpentInParking(Integer hoursSpentInParking) {
		this.hoursSpentInParking = hoursSpentInParking;
	}
	
	
	
}
