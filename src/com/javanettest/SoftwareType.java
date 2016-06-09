package com.javanettest;

public enum SoftwareType {
	GCC("3", "GCC v4.9.2"),
	PYTHON2("4", "Python v2c"),
	R("7", "R v3.2.0"),
	JAVA8("8", "Java SE v8"),
	OCTAVE("9", "GNU Octave v4.0.0"),
	BLENDER("10", "Blender"),
	SCILAB("11", "Scilab"),
	JULIA("12", "Julia"),
	PYTHON3("13", "Python v3"),
	GROMACS("14", "GROMACS"),
	OPENFOAM("15", "OpenFOAM"),
	JAVA7("16", "Java SE v7");
	
	private String mId;
	private String mName;
	
	private SoftwareType(String id, String name) {
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
