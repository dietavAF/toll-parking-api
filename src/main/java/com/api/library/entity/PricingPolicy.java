package com.api.library.entity;

public interface PricingPolicy {
	
	/**
	 * Allows to determine the amount of money to pay at the exit of the parking
	 * 
	 * @param numberOfHours the number of hours spent in the parking
	 * @return the toll
	 */
	int getBill(int numberOfHours);
}
