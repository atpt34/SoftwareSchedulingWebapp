package com.tretinichenko.oleksii.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

	@RequestMapping(value = {"/", "home", "index", "welcome"},
			method = RequestMethod.GET)
	public String indexPage(){
		return "index";
	}
	
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(){
		return "/403";
	}
	
	
	
	// maybe login and logout goes here
}
