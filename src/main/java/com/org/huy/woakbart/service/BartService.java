package com.org.huy.woakbart.service;

import java.util.List;

import com.org.huy.woakbart.model.Destination;
import com.org.huy.woakbart.model.Station;
import com.org.huy.woakbart.model.WestOakDest;

/**
 * Bart Service
 * @author hnguyen10
 */
public interface BartService {
	public List<Station> getAllStations();

	public WestOakDest getWOakEta(String stn);

	public List<Destination> getTrainEta(String stn);
	
	public int getTrainCount();

}
