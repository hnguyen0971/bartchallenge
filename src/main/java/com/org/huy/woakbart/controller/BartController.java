package com.org.huy.woakbart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.org.huy.woakbart.service.BartService;

/**
 * Controller class to handle the index page.
 * 
 * @author hnguyen10
 */
@Controller
public class BartController {
	
	@Autowired
	private BartService bartService;
	
	@RequestMapping(path="/", method=RequestMethod.GET)
	public String goHome(Model model){
		// We need to load the select box first before the page loads
        model.addAttribute("bartStations", bartService.getAllStations());
		return "index";
	}

}