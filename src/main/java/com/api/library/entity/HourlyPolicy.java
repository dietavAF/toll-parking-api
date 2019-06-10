package com.api.library.entity;

import org.springframework.beans.factory.annotation.Value;

public class HourlyPolicy implements PricingPolicy {
	
	@Value("${hourly.policy.hourly.amount}")
	private int hourlyPrice;
	
	@Override
	public int getBill(int numberOfHours) {
		return hourlyPrice * numberOfHours;
	}

}
