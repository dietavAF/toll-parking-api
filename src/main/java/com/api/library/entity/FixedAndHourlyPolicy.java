package com.api.library.entity;

public class FixedAndHourlyPolicy implements PricingPolicy {
	
	private static final int HOURLY_PRICE = 1;
	private static final int FIXED_AMOUNT = 5;
	
	@Override
	public int getBill(int numberOfHours) {
		return FIXED_AMOUNT + HOURLY_PRICE * numberOfHours;
	}

}
