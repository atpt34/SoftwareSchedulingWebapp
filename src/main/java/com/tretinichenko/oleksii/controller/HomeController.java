package com.tretinichenko.oleksii.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

	@RequestMapping(value = {"/", "home", "index", "welcome"},
			method = RequestMethod.GET)
	public String indexPage(){
		return "index";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(){
		return "/login";
	}
	
	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logout(){
		return "/logoutSuccessful";
	}
	
	@RequestMapping(value = "/accountInfo", method = RequestMethod.GET)
	public String accountInfo(Model model, Principal principal){
		String username = principal.getName();
		System.out.println("user: " + username);
		model.addAttribute("username", username);
		return "/accountInfo";
	}
	
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal){
		if (principal != null) {
			model.addAttribute("message", "Hi " + principal.getName()
			+ "<br> You don't have permission to access this page!");
		} else {
			model.addAttribute("message", 
					"You don't have permission to access this page!");
		}
		return "/403";
	}
	
	
	
	// maybe login and logout goes here
}
