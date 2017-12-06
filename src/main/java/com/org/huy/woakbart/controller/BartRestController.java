package com.org.huy.woakbart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.huy.woakbart.model.Destination;
import com.org.huy.woakbart.model.Station;
import com.org.huy.woakbart.model.WestOakDest;
import com.org.huy.woakbart.service.BartService;

/**
 * RestController that handles all bart information.
 * @author hnguyen10
 */
@Configuration
@RestController
public class BartRestController 
{

	@Autowired
	private BartService bartService;   
    
    @RequestMapping(path="/westOaklandEta", method=RequestMethod.GET)
    public WestOakDest getWestOaklandEta(@RequestParam(value="stn", required = true) String stn) {
    		 return bartService.getWOakEta(stn);
      }
    
    @RequestMapping(path="/trainSchedule", method=RequestMethod.GET)
    public  List<Destination>  getTrainScheduleFor(@RequestParam(value="stn", required = true) String stn) {
		return bartService.getTrainEta(stn);
      }
    
    @RequestMapping(path="/stations", method=RequestMethod.GET)
    public List<Station> getStations() {
    		return bartService.getAllStations();
      }
    
    @RequestMapping(path="/trainCount", method=RequestMethod.GET)
    public int getTrainCount() {
    		return bartService.getTrainCount();
      }

}
