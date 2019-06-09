package com.api.library.entity;

import java.util.Date;

public class Slot {

	private Date startPark;
	
	private Date stopPark;

	public Date getStartPark() {
		return startPark;
	}

	public void setStartPark(Date startPark) {
		this.startPark = startPark;
	}

	public Date getStopPark() {
		return stopPark;
	}

	public void setStopPark(Date stopPark) {
		this.stopPark = stopPark;
	}
	
	/**
	 * Allows to get the hours spent in the parking for a specified slot
	 * Every slot is assigned to a car in the parking
	 * At the exit, the slot will determine how much time the car has been in the parking
	 * 
	 * @return the numbers of hours spent in the parking
	 */
	public int getHoursSpentInParking() {
		long diffTime = stopPark.getTime() - startPark.getTime();
		long diffHours = diffTime / (60 * 60 * 1000);
		return ((int) diffHours) + 1;
	}
	
	
}
