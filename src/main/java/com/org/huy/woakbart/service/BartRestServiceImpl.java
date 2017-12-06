package com.org.huy.woakbart.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.org.huy.woakbart.model.Destination;
import com.org.huy.woakbart.model.Station;
import com.org.huy.woakbart.model.WestOakDest;

/**
 * Uses rest service to talk to the bart api.
 * @author hnguyen10
 */
@Service("bartService")
@Configuration
public class BartRestServiceImpl implements BartService {
	
	// Configurable variables
    @Value("${bart.api.key}")
    private String key;
    
    @Value("${bart.api.uri}")
    private String uri;

    
    @Autowired
    private RestTemplate restTemplate;
    
	public List<Station> getAllStations() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri + "stn.aspx")
                .queryParam("cmd", "stns")
                .queryParam("key", key)
                .queryParam("json", "y");
        
        String json = restTemplate.getForObject(builder.build().encode().toUriString(), String.class);
        List<Station> stations = new ArrayList<>();
        try {
			JSONObject obj = new JSONObject(json);
			obj = obj.getJSONObject("root").getJSONObject("stations");
			JSONArray arr = obj.getJSONArray("station");
			for (int i = 0; i < arr.length(); i++)
			{
				stations.add(new Station(arr.getJSONObject(i).getString("name"), arr.getJSONObject(i).getString("abbr")));
			}	
			// removing west oakland because the user is already in west oakland
			stations.remove(new Station("West Oakland", "WOAK"));
		} catch (JSONException e) {
			throw new IllegalArgumentException("Error parsing json!", e);
		}
        if (CollectionUtils.isEmpty(stations)) {
			throw new IllegalStateException("Empty list of stations!");
        }
        return stations;
	
	}
	
	public int getTrainCount() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri + "bsa.aspx")
                .queryParam("cmd", "count")
                .queryParam("key", key)
                .queryParam("json", "y");
        
        String json = restTemplate.getForObject(builder.build().encode().toUriString(), String.class);
        try {
			JSONObject obj = new JSONObject(json);
			obj = obj.getJSONObject("root");
			return obj.getInt("traincount");
		} catch (JSONException e) {
			throw new IllegalArgumentException("Error parsing json!", e);
		}	
	}

	public WestOakDest getWOakEta(String stn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri + "sched.aspx")
                .queryParam("cmd", "depart")
                .queryParam("orig", stn)
                .queryParam("dest", "WOAK")
                .queryParam("key", key)
                .queryParam("json", "y");
     
        String json = restTemplate.getForObject(builder.build().encode().toUriString(), String.class);
        try {
			JSONObject obj = new JSONObject(json);
			obj = obj.getJSONObject("root").getJSONObject("schedule");
			
			SimpleDateFormat longFormat = new SimpleDateFormat("MMM d, yyyy h:mm a");
			SimpleDateFormat shortFormat = new SimpleDateFormat("MM/dd/yyy h:mm a");
		    String time = obj.getString("time");
		    String date = obj.getString("date");
			Date currentTime = null;
		    try {
		    		currentTime = longFormat.parse(date+ " "+ time);
			} catch (ParseException e) {
				// Throwing this exception will return a 500 error
				throw new IllegalArgumentException("Error parsing current time!", e);
			}
			
		    // after testing edge case from the bart api (ex. trying to look up the schedule when bart is closed)
			// I realized that the before and after that bart returns is inaccurate so I have code that will loop through the
		    // different bart schedules to determine the best eta for west oakland
			obj = obj.getJSONObject("request");
			JSONArray arr = obj.getJSONArray("trip");
			for (int i = 0; i < arr.length(); i++)
			{
			    time = arr.getJSONObject(i).getString("@origTimeMin");
			    date = arr.getJSONObject(i).getString("@origTimeDate");
			   
			    try {
					Date tripTime = shortFormat.parse(date+ " "+ time);
					if (currentTime.before(tripTime)) {
					    time = arr.getJSONObject(i).getString("@destTimeMin");
					    date = arr.getJSONObject(i).getString("@destTimeDate");
						return new WestOakDest(time, date,longFormat.format(currentTime));
					}
				} catch (ParseException e) {
					throw new IllegalArgumentException("Error parsing current time!", e);
				}
		
			}
		} catch (JSONException e) {
			throw new IllegalArgumentException("Error parsing json!", e);
		}	
        // returning null if a schedule can't be found. The front end will handle this
        return null;
	}

	public List<Destination> getTrainEta(String stn) {
    	UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri + "etd.aspx")
                .queryParam("cmd", "etd")
                .queryParam("key", key)
                .queryParam("orig", stn)
                .queryParam("json", "y");
        Map<Integer, List<Destination> > destinations = new HashMap<>();
        String json = restTemplate.getForObject(builder.build().encode().toUriString(), String.class);
        try {
			JSONObject obj = new JSONObject(json);
			obj = obj.getJSONObject("root").getJSONArray("station").getJSONObject(0);
			JSONArray arr = obj.getJSONArray("etd");
			for (int i = 0; i < arr.length(); i++)
			{	
				JSONArray minuteArray = arr.getJSONObject(i).getJSONArray("estimate");
				List<String> minutes = new ArrayList<>(minuteArray.length());
				for (int j = 0; j < minuteArray.length(); j++)
				{
					minutes.add(minuteArray.getJSONObject(j).getString("minutes"));
				}
		        int plat = minuteArray.getJSONObject(0).getInt("platform");
		        // Using the builder pattern to make an immutable object
		        Destination destination = new Destination.Builder().destination(arr.getJSONObject(i).getString("destination"))
		        		.abbreviation(arr.getJSONObject(i).getString("abbreviation"))
		        		.platform(plat)
		        		.color(minuteArray.getJSONObject(0).getString("color"))
		        		.hexColor(minuteArray.getJSONObject(0).getString("hexcolor"))
		        		.minutes(minutes).build();
		        
		        List<Destination> listDest = destinations.getOrDefault(plat, new ArrayList<>());
		        listDest.add(destination);
		        destinations.put(plat, listDest);
			}
		} catch (JSONException e) {
			throw new IllegalArgumentException("Error parsing json!", e);

		}
        List<Destination> orderedDestinations = new ArrayList<>();
        for (List<Destination> dests : destinations.values()) {
        		orderedDestinations.addAll(dests);
        }
        return orderedDestinations;
		
	}
}
