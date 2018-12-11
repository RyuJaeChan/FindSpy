package com.project.chat.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	@GetMapping("/")
	public String main(ModelMap modelMap, Principal principal) {
		System.out.println("Test pric : " + principal);
		
		modelMap.put("user", principal);
		
		return "main";
	}
	
	
}
