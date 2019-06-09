package com.api.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The starting point of the Java Library API for the Toll Parking Management
 * 
 * @author diegotavolaro
 *
 */
@SpringBootApplication
public class ParkingTollApplication {
	
	public static void main(String ...string) {
		SpringApplication.run(ParkingTollApplication.class, string);
	}
}
