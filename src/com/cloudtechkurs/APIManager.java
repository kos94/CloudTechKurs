package com.cloudtechkurs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

class HttpResponse {
	public final String mText;
	public final int mCode;
	
	public HttpResponse(String text, int responseCode) {
		mText = text;
		mCode = responseCode;
	}
	
	public void assertOk() throws Exception {
		if(mCode != 200) {
			throw new Exception("Response failed with code " + mCode);
		}
	}
}

public class APIManager {
	private static final String DEFAULT_API_KEY = "D8TUseKdgFdOCsME";
	
	private String mKey = DEFAULT_API_KEY;

	public static void main(String[] args) throws Exception {
		
		APIManager apiManager = new APIManager();
		
		Task task = apiManager.createTask("NewName", "NewResultName", 
				Task.DEFAULT_REPOSITORY, 
				Task.DEFAULT_SW_TYPE, 
				Task.DEFAULT_INSTANCE_TYPE,
				Task.DEFAULT_RUN_COMMAND);
		
		System.out.println(apiManager.getTaskStatus(task.getTaskId()));
		
		apiManager.stopTask(task.getTaskId());
		
		apiManager.deleteTask(task.getTaskId());
		
		
//		apiManager
//		System.out.println("Testing 1 - Send Http GET request");
//		http.sendGet();
		
//		System.out.println("\nTesting 2 - Send Http POST request");
		
//		testDelete("1373");
//		String id = testCreateTask();


		// test refresh task
//		testRefreshTask("137");
		
//		testStop(id);
	}
	
	public Task createTask(String taskName, String resultName, String repository, 
			SoftwareType swType, InstanceType instanceType, String runCommand) 
		throws Exception {
		
		Task task = new Task(taskName, resultName, repository, swType, instanceType, runCommand);
						
		String url = "https://api.flyelephant.net/v1/tasks/initiateTask";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("taskName", task.getTaskName()); //TestTask
		params.put("resultName", task.getResultName());
		params.put("repository", task.getRepository());
		params.put("software_id", task.getSoftwareType().getId());
		params.put("instance", task.getInstanceType().getId());
		params.put("run_command", task.getRunCommand());
		params.put("account_type", task.getAccountType());
		
		//TODO handle fail somewhere
		HttpResponse response = sendPost(url, params);
		response.assertOk();
		
		JSONObject obj = new JSONObject(response);
		String taskId = obj.get("id").toString();
		task.setTaskId(taskId);
		
		return task;
	}
	
	public String getTaskStatus(String taskId) throws Exception {
		String url = "https://api.flyelephant.net/v1/tasks/" + taskId + "/getStatus";
		
		//TODO handle fail somewhere
		HttpResponse response = sendGet(url);
		response.assertOk();
		
		JSONObject obj = new JSONObject(response.mText);
		return obj.getString("taskStatus");
	}
	
	public void stopTask(String taskId) throws Exception {
		String url = "https://api.flyelephant.net/v1/tasks/" + taskId + "/stop";
		
		//TODO handle fail somewhere
		HttpResponse response = sendPost(url, null);
		response.assertOk();
	}
	
	public void deleteTask(String taskId) throws Exception {
		String url = "https://api.flyelephant.net/v1/tasks/" + taskId + "/delete";
		
		//TODO handle fail somewhere
		HttpResponse response = sendDelete(url);
		response.assertOk();
	}
	
	// HTTP GET request
	private HttpResponse sendGet(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("api-key", mKey);

		int responseCode = con.getResponseCode();
		String response = readInput(con.getInputStream());
		
		System.out.println("GET: " + response);
		
		return new HttpResponse(response, responseCode);
	}
	
	// HTTP POST request
	private HttpResponse sendPost(String url, Map<String, String> arguments) throws Exception {
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		
		con.setRequestMethod("POST");
		con.setRequestProperty("api-key", mKey);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
		//send post arguments if specified
		if(arguments != null) {
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			
			StringJoiner sj = new StringJoiner("&");
			for(Map.Entry<String,String> entry : arguments.entrySet())
			    sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" 
			    		+ URLEncoder.encode(entry.getValue(), "UTF-8"));
			
			wr.writeBytes(sj.toString());
			wr.flush();
			wr.close();
		}
		
		int responseCode = con.getResponseCode();
		String response = readInput(con.getInputStream());
		
		System.out.println("POST: " + response);
		
		return new HttpResponse(response, responseCode);
	}
	
	//HTTP DELETE request
	private HttpResponse sendDelete(String url) throws Exception {
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		
		con.setRequestMethod("DELETE");
		con.setRequestProperty("api-key", mKey);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
		int responseCode = con.getResponseCode();
		String response = readInput(con.getInputStream());

		return new HttpResponse(response, responseCode);
	}
	
	private String readInput(InputStream inputStream) throws IOException {
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(inputStream));
		String inputLine;
		StringBuffer content = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		
		return content.toString();
	}
}
