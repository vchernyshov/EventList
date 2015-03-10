package ua.insomnia.eventlist.rest;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import ua.insomnia.eventlist.Event;

public class Api {
	
	public static final String TAG = "API";
	
	private static final String BASE_URL = "http://eventlist.com.ua/api/";
	private static final String EVENTS = "events";
	
	public ArrayList<Event> getEvents(int reguestType, String date, Long id) {
		ArrayList<Event> list = new ArrayList<Event>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(createReguestString(reguestType, date, id));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			InputStream inputStream = httpResponse.getEntity().getContent();

			if (inputStream != null)
				list = convertJsonToArrayList(new JSONArray(Utils.convertStreamToString(inputStream)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public String getEventByPage(int pageSize, int page) {
		String respone = null;
		String URL = BASE_URL+EVENTS+"?pageSize="+pageSize+"&page="+page;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			InputStream inputStream = httpResponse.getEntity().getContent();

			if (inputStream != null)
				respone = Utils.convertStreamToString(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return respone;
	}
	
	
	//----------- using Respone class for return data -----------//
	
	public Respone getEventByPageR(int pageSize, int page) {
		String URL = BASE_URL+EVENTS+"?pageSize="+pageSize+"&page="+page;
		return new Respone(makeReguest(URL));
	}
	
	public Respone getEventByDateR(String date, int page) {
		String URL = BASE_URL+EVENTS+"/"+date+"/?page="+page;
		return new Respone(makeReguest(URL));
	}
	
	public Respone getEventByIdR(Long id) {
		String URL = BASE_URL+EVENTS+"/"+id+"/";	
		return new Respone(makeReguest(URL));
	}
	
	//----------- end -----------//
	
	private String makeReguest(String URL) {
		String respone = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			InputStream inputStream = httpResponse.getEntity().getContent();

			if (inputStream != null)
				respone = Utils.convertStreamToString(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respone;
	}
	private String createReguestString(int reguestType, String date, Long id) {
		StringBuilder builder = new StringBuilder();
		builder.append(BASE_URL);
		switch(reguestType) {
		case EventService.TYPE_ALL:
			builder.append(EVENTS);
			break;
		case EventService.TYPE_BY_DATE:
			builder.append(EVENTS);
			builder.append(date+"/");
			break;
		case EventService.TYPE_BY_ID:
			builder.append(EVENTS);
			builder.append(id.toString()+"/");
			break;
		}
		return builder.toString();
	}
	
	private ArrayList<Event> convertJsonToArrayList(JSONArray json) throws JSONException {
		ArrayList<Event> list = new ArrayList<Event>();
		for (int i = 0; i<json.length(); i++) {
			list.add(new Event(json.getJSONObject(i)));
		}
		return list;
	}
}
