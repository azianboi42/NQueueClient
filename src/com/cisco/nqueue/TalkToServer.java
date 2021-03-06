package com.cisco.nqueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class TalkToServer{

	private String server_address; //http server address
	private int client_id;
	private int restaurant_id;
	private String phone;
	
	HttpClient httpClient = new DefaultHttpClient();
	
	public TalkToServer(String server_address, int client_id, 
			            int restaurant_id, String phone){
		this.server_address = server_address;
		this.client_id = client_id;
		this.restaurant_id = restaurant_id;
		this.phone = phone;
	}
	
	public TalkToServer(String server_address, 
			            int restaurant_id, String phone){
		this.server_address = server_address;
		this.restaurant_id = restaurant_id;
		this.phone = phone;
		this.client_id = -1;	
	}
	
	
	public TalkToServer(){
		
		this.client_id = -1;
	}
	
	
	public void setClientId(int clientId){
		this.client_id = clientId;
	}
	public void setRestaurantId(int restaurant_id){
		this.restaurant_id = restaurant_id;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}
	public void setWebServer(String serverAddr){
		this.server_address = serverAddr;
	}
	
	public void set(String server_address, int restaurant_id, String phone){
		this.server_address = server_address;
		
		this.restaurant_id = restaurant_id;
		this.phone = phone;
	}
	/**
	 * check in to the specified restaurant
	 * @return
	 */
	ArrayList<String> check_in( ) {
		//HttpClient httpClient = new DefaultHttpClient();
		//HttpPost httppost = new HttpPost(server_address+"/check_in.php");
		 Log.i("real address", server_address+"/check_in.php");
			List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
			//postData.add(new BasicNameValuePair("client_id", client_id));
			postData.add(new BasicNameValuePair("restaurant_id",String.valueOf(restaurant_id)));
			postData.add(new BasicNameValuePair("phone_number",phone));
			ArrayList<String> result = getServerInfo("check_in.php", postData);
			//Log.i("+++check_in+++", "get(0) is "+result.get(0).trim());
			int result_id = Integer.parseInt(result.get(0).trim());
			Log.i("+++check_in+++", "result_id: "+result_id);
			if (result_id >= 0)   // check in successfully
				{client_id = result_id;
				}
			return result;
	}
	
	ArrayList<String> check_out( ){
		    Log.i("real address", server_address+"/check_out.php");
			List<NameValuePair> postData = new ArrayList<NameValuePair>(2);
			postData.add(new BasicNameValuePair("client_id", String.valueOf(client_id)));
			postData.add(new BasicNameValuePair("restaurant_id", String.valueOf(restaurant_id)));
			postData.add(new BasicNameValuePair("force", "false"));
			
			return getServerInfo("check_out.php", postData);			
	}
	
	ArrayList<String> quit( ){
		Log.i("real address", server_address+"/check_out.php");
			List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
			postData.add(new BasicNameValuePair("client_id", String.valueOf(client_id)));
			postData.add(new BasicNameValuePair("restaurant_id", String.valueOf(restaurant_id)));
			postData.add(new BasicNameValuePair("force", "true"));
			return getServerInfo("check_out.php", postData);
			}
	
	ArrayList<String> queryRank( ){
		//HttpPost httppost = new HttpPost(server_address+"/update_rank.php");
		Log.i("real address", server_address+"/update_rank.php");
			List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
			postData.add(new BasicNameValuePair("client_id", String.valueOf(client_id)));
			postData.add(new BasicNameValuePair("restaurant_id",String.valueOf(restaurant_id)));
			
			return getServerInfo("update_rank.php", postData);
	}
	
	ArrayList<String> getServerInfo(String method, List<NameValuePair> postData){
		HttpPost httppost = new HttpPost(server_address+"/"+method);
		HttpResponse response = null;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(postData));
			response = httpClient.execute(httppost);
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		HttpEntity entity = response.getEntity();
		ArrayList<String> result = new ArrayList<String>();
		if (entity != null ){
			InputStream instream = null;
			try {
				instream = entity.getContent();
				result = convertStreamToString(instream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(int i = 0; i < result.size(); i++){
				Log.i("result from server", result.get(i));
			}
			//Log.i("Read from server", result);
		}
		
		
		return result;
	}
	
	public ArrayList<String> convertStreamToString(InputStream inputStream) throws IOException {
		ArrayList<String> str_list = new ArrayList<String>();
		if (inputStream != null) {
			//StringBuilder sb = new StringBuilder();
			String line;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					str_list.add(line);
					//sb.append(line).append("\n");
				}
			} finally {
				inputStream.close();
			}
		}
		return str_list;
		}

}