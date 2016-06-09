package com.cloudtechkurs.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class TaskRegistry {
	private static final String REGISTRY_PATH = "C:/registry.json";
	
	private APIManager mApiManager = new APIManager();
	
	private List<Task> mTasks = new ArrayList<Task>();
	
	public TaskRegistry() {}
	
	// load from JSON
	public void load() {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(REGISTRY_PATH));
			String jsonString = new String(encoded, "UTF-8");
			JSONArray jsonArr = new JSONArray(jsonString);
			
			for(int i=0; i<jsonArr.length(); i++) {
				JSONObject jsonTask = jsonArr.getJSONObject(i);
				mTasks.add(new Task(jsonTask));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// store to JSON
	public void store() {
		JSONArray jsonArr = new JSONArray();
		for(Task task : mTasks) {
			jsonArr.put(task.toJSON());
		}
		String s = jsonArr.toString();
		
		try {
			Files.write(Paths.get(REGISTRY_PATH), s.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getTaskCount() {
		return mTasks.size();
	}
	
	public Task get(int i) {
		if(i < 0 || i > mTasks.size()) return null;
		return mTasks.get(i);
	}
	
	public void addTask(Task task) throws Exception {
		mApiManager.createTask(task);
		mTasks.add(task);
	}
	
	public void deleteTask(int index) throws Exception {
		if(index >= 0 && index < mTasks.size()) {
			String taskId = mTasks.get(index).getTaskId();
			mApiManager.deleteTask(taskId);
			mTasks.remove(index);
		}
	}
	
	public void stopTask(int index) throws Exception {
		if(index >= 0 && index < mTasks.size()) {
			String taskId = mTasks.get(index).getTaskId();
			mApiManager.stopTask(taskId);
			mTasks.get(index).setStatus("Stopped");
		}
	}
	
	public void refreshTasksStatus() {
		for(Task task : mTasks) {
			try {
				String status = mApiManager.getTaskStatus(task.getTaskId());
				task.setStatus(status);
			} catch (Exception e) {
				task.setStatus("UNDEFINED");
			}
		}
	}
}
