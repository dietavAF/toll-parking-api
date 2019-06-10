package com.api.library.entity;

import org.springframework.beans.factory.annotation.Value;

public class FixedAndHourlyPolicy implements PricingPolicy {
	
	@Value("${fixedhourly.policy.fixed.amount}")
	private int fixedAmount;
	
	@Value("${fixedhourly.policy.hourly.amount}")
	private int hourlyPrice;
	
	@Override
	public int getBill(int numberOfHours) {
		return fixedAmount + hourlyPrice * numberOfHours;
	}

}
