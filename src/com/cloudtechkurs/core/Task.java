package com.cloudtechkurs.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.json.JSONObject;

@XmlAccessorType(XmlAccessType.FIELD)
public class Task {
	
	public static final String DEFAULT_TASK_NAME = "TestTask";
	public static final String DEFAULT_RESULT_NAME = "TestResult";
	public static final String DEFAULT_REPOSITORY = 
			"https://github.com/FlyElephantTeam/gcc-example.git";
	public static final SoftwareType DEFAULT_SW_TYPE = SoftwareType.GCC;
	public static final InstanceType DEFAULT_INSTANCE_TYPE = InstanceType.A0;
	public static final String DEFAULT_RUN_COMMAND = "make;./test";
	
	private String mTaskId = "-1";
	
	private String mTaskName;
	private String mResultName;
	private String mRepository;
	private SoftwareType mSoftware;
	private InstanceType mInstance;
	private String mRunCommand;
	
	private String mStatus = "Unknown";
	
	public Task() {
		this(DEFAULT_TASK_NAME, DEFAULT_RESULT_NAME, DEFAULT_REPOSITORY,
				DEFAULT_SW_TYPE, DEFAULT_INSTANCE_TYPE, DEFAULT_RUN_COMMAND);
	}
	
	public Task(String taskName, String resultName, String repository, SoftwareType swType, 
			InstanceType instanceType, String runCommand) {
		
		mTaskName = taskName;
		mResultName = resultName;
		mRepository = repository;
		mSoftware = swType;
		mInstance = instanceType;
		mRunCommand = runCommand;
	}
	
	public Task(JSONObject json) {
		mTaskId = json.getString(APIManager.TASK_ID_KEY);
		mTaskName = json.getString(APIManager.TASK_NAME_KEY);
		mResultName = json.getString(APIManager.RESULT_NAME_KEY);
		mRepository = json.getString(APIManager.REPOSITORY_KEY);
		int iSoftware = json.getInt(APIManager.SOFTWARE_ID_KEY);
		mSoftware = SoftwareType.values()[iSoftware];
		int iInstance = json.getInt(APIManager.INSTANCE_KEY);
		mInstance = InstanceType.values()[iInstance];
		mRunCommand = json.getString(APIManager.RUN_COMMAND_KEY);
		mStatus = json.getString(APIManager.STATUS_KEY);
	}
	
	public String getTaskName() {
		return mTaskName;
	}
	
	public String getResultName() {
		return mResultName;
	}
	
	public String getRepository() {
		return mRepository;
	}
	
	public SoftwareType getSoftwareType() {
		return mSoftware;
	}
	
	public InstanceType getInstanceType() {
		return mInstance;
	}
	
	public String getRunCommand() {
		return mRunCommand;
	}
	
	public String getAccountType() {
		return "2";
	}
	
	public String getTaskId() {
		return mTaskId;
	}
	
	public String getStatus() {
		return mStatus;
	}
	
	public void setTaskName(String taskName) {
		mTaskName = taskName;
	}
	
	public void setResultName(String name) {
		mResultName = name;
	}
	
	public void setRepository(String repo) {
		mRepository = repo;
	}
	
	public void setSoftwareType(SoftwareType type) {
		mSoftware = type;
	}
	
	public void setInstanceType(InstanceType type) {
		mInstance = type;
	}
	
	public void setRunCommand(String command) {
		mRunCommand = command;
	}
	
	public void setTaskId(String id) {
		mTaskId = id;
	}
	
	public void setStatus(String status) {
		mStatus = status;
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put(APIManager.TASK_ID_KEY, mTaskId);
		obj.put(APIManager.TASK_NAME_KEY, mTaskName);
		obj.put(APIManager.RESULT_NAME_KEY, mResultName);
		obj.put(APIManager.REPOSITORY_KEY, mRepository);
		obj.put(APIManager.SOFTWARE_ID_KEY, mSoftware.ordinal());
		obj.put(APIManager.INSTANCE_KEY, mInstance.ordinal());
		obj.put(APIManager.RUN_COMMAND_KEY, mRunCommand);
		obj.put(APIManager.STATUS_KEY, mStatus);
		return obj;
	}
}