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
	
	public int getHoursSpentInParking() {
		long diffTime = stopPark.getTime() - startPark.getTime();
		long diffHours = diffTime / (60 * 60 * 1000);
		return ((int) diffHours) + 1;
	}
	
	
}
