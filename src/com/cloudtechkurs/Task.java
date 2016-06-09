package com.cloudtechkurs;

public class Task {
	
	public static final String DEFAULT_TASK_NAME = "TestTask";
	public static final String DEFAULT_RESULT_NAME = "TestResult";
	public static final String DEFAULT_REPOSITORY = 
			"https://github.com/FlyElephantTeam/gcc-example.git";
	public static final SoftwareType DEFAULT_SW_TYPE = SoftwareType.GCC;
	public static final InstanceType DEFAULT_INSTANCE_TYPE = InstanceType.A0;
	public static final String DEFAULT_RUN_COMMAND = "make;./test";
	
	private String mTaskName;
	private String mResultName;
	private String mRepository;
	private SoftwareType mSoftware;
	private InstanceType mInstance;
	private String mRunCommand;
	
	private String mTaskId;
	private String mStatus = "Doesn't exist"; //TODO not sure, maybe delete
	
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
	
	public void setTaskId(String id) {
		mTaskId = id;
	}
}
