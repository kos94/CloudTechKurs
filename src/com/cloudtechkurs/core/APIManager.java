package com.cloudtechkurs.core;

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
	public static final String TASK_NAME_KEY = "taskName";
	public static final String RESULT_NAME_KEY = "resultName";
	public static final String REPOSITORY_KEY = "repository";
	public static final String SOFTWARE_ID_KEY = "software_id";
	public static final String INSTANCE_KEY = "instance";
	public static final String RUN_COMMAND_KEY = "run_command";
	public static final String ACCOUNT_TYPE_KEY = "account_type";
	public static final String TASK_ID_KEY = "id";
	public static final String STATUS_KEY = "taskStatus";
	
	private static final String DEFAULT_API_KEY = "D8TUseKdgFdOCsME";
	
	private String mKey = DEFAULT_API_KEY;
	
	public void createTask(Task task) throws Exception {
					
		String url = "https://api.flyelephant.net/v1/tasks/initiateTask";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(TASK_NAME_KEY, task.getTaskName());
		params.put(RESULT_NAME_KEY, task.getResultName());
		params.put(REPOSITORY_KEY, task.getRepository());
		params.put(SOFTWARE_ID_KEY, task.getSoftwareType().getId());
		params.put(INSTANCE_KEY, task.getInstanceType().getId());
		params.put(RUN_COMMAND_KEY, task.getRunCommand());
		params.put(ACCOUNT_TYPE_KEY, task.getAccountType());
		
		HttpResponse response = sendPost(url, params);
		response.assertOk();
		
		JSONObject obj = new JSONObject(response.mText);
		String taskId = obj.get("id").toString();
		task.setTaskId(taskId);
	}
	
	public String getTaskStatus(String taskId) throws Exception {
		String url = "https://api.flyelephant.net/v1/tasks/" + taskId + "/getStatus";
		
		//TODO handle fail somewhere
		HttpResponse response = sendGet(url);
		response.assertOk();
		
		JSONObject obj = new JSONObject(response.mText);
		return obj.getString(STATUS_KEY);
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
