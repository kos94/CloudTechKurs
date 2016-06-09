package com.cloudtechkurs.ui;

import com.cloudtechkurs.core.TaskRegistry;

public class MainWindow {
	private static final String REGISTRY_PATH = "C:/registry.json";
	
	public static void main(String[] args) {
		new TaskRegistry().load();
//		TaskRegistry registry;
//		File regFile = new File(REGISTRY_PATH);
		
//		if(regFile.exists()) {
//			registry = XmlSerializer.deserialize(REGISTRY_PATH, TaskRegistry.class);
//		} else {
//			try {
//				regFile.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			registry = new TaskRegistry();
//			
//		}
//)		
//		registry.add(new Task());
//		
//		XmlSerializer.serialize(registry, REGISTRY_PATH);
	}
}
