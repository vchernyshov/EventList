package ua.insomnia.eventlist.rest;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class Api {
	
	public static final String TAG = "API";
	
	private static final String BASE_URL = "http://eventlist.com.ua/api/";
	private static final String EVENTS = "events";
	
	public static Response getEventByPageR(int pageSize, int page) {
		Log.d(TAG, "getting events by page " + page + " with page size " + pageSize);
		String URL = BASE_URL+EVENTS+"?pageSize="+pageSize+"&page="+page;
		return new Response(makeReguest(URL));
	}
	
	public static Response getEventByDateR(String date, int page) {
		Log.d(TAG, "getting events by date " + date + " at page " + page);
		String URL = BASE_URL+EVENTS+"/"+date+"/?page="+page;
		return new Response(makeReguest(URL));
	}
	
	public static Response getEventByIdR(Long id) {
		Log.d(TAG, "getting event by id " + id);
		String URL = BASE_URL+EVENTS+"/"+id+"/";	
		return new Response(makeReguest(URL));
	}
	
	private static String makeReguest(String URL) {
		String response = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			InputStream inputStream = httpResponse.getEntity().getContent();

			if (inputStream != null)
				response = Utils.convertStreamToString(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
