package com.api.library.entity;

public class HourlyPolicy implements PricingPolicy {
	
	private static final int HOURLY_PRICE = 2;
	
	@Override
	public int getBill(int numberOfHours) {
		return HOURLY_PRICE * numberOfHours;
	}

}
