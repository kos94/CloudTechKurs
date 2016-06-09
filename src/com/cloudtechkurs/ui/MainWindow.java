package com.cloudtechkurs.ui;

import com.cloudtechkurs.core.TaskRegistry;

public class MainWindow {
	
	public static void main(String[] args) {
		new TaskRegistry().load();
	}
}
