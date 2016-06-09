package com.cloudtechkurs.core;

public enum InstanceType {
	A0("A0", "A0 : 1 cores | 1GB RAM | 20GB HDD"),
	A1("A1", "A1 : 1 cores | 2GB RAM | 70GB HDD"),
	A2("A2", "A2 : 2 cores | 4GB RAM | 135GB HDD"),
	A3("A3", "A3 : 4 cores | 7GB RAM | 285GB HDD"),
	A4("A4", "A4 : 8 cores | 14GB RAM | 605GB HDD"),
	A5("A5", "A5 : 2 cores | 14GB RAM | 135GB HDD"),
	A6("A6", "A6 : 4 cores | 28GB RAM | 285GB HDD"),
	A7("A7", "A7 : 8 cores | 56GB RAM | 605GB HDD"),
	A10("A10", "A10 : 8 cores | 56GB RAM | 382GB HDD"),
	A11("A11", "A11 : 16 cores | 112GB RAM | 382GB HDD"),
	G1("G1", "G1 : 2 cores | 28GB RAM | 384GB HDD"),
	G2("G2", "G2 : 4 cores | 56GB RAM | 768GB HDD"),
	G3("G3", "G3 : 8 cores | 112GB RAM | 1536GB HDD"),
	G4("G4", "G4 : 16 cores | 224GB RAM | 3072GB HDD"),
	G5("G5", "G5 : 32 cores | 448GB RAM | 6144GB HDD");
	
	private String mId;
	private String mName;
	
	private InstanceType(String id, String name) {
		mId = id;
		mName = name;
	}
	
	public String getId() {
		return mId;
	}
	
	public String getName() {
		return mName;
	}
}
