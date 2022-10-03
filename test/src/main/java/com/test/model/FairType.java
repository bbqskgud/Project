package com.test.model;

public enum FairType {
	
	Fair("박람회"),
	Festivals("축제");
	
	private String value;
	
	private FairType(String value) {
		this.value=value;
	}
	
	public String getValue() {
		return value;
	}
}
