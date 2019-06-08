package com.api.library.entity;

public enum CarType {
	SEDAN("Sedan"), ELECTRIC_20("E20kw"), ELECTRIC_50("E50kw");
	
	private String type;
	
	CarType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
