package com.api.library.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Details about the status of the selected parking")
public class ParkingStatus {
	
	@ApiModelProperty(notes = "The name of the parking")
	private String name;
	
	@ApiModelProperty(notes = "The number of slots which are occupied in the parking")
	private Integer occupiedSlots;
	
	@ApiModelProperty(notes = "The number of slots which are available in the parking")
	private Integer availableSlots;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOccupiedSlots() {
		return occupiedSlots;
	}

	public void setOccupiedSlots(Integer occupiedSlots) {
		this.occupiedSlots = occupiedSlots;
	}

	public Integer getAvailableSlots() {
		return availableSlots;
	}

	public void setAvailableSlots(Integer availableSlots) {
		this.availableSlots = availableSlots;
	}
	
}
