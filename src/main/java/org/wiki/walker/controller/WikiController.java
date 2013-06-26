package org.wiki.walker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.Controller;

import org.wiki.walker.WikiWalker;

@Controller
public class WikiController {

	@Autowired
	private WikiWalker walker;
	
//	public ModelAndView handleRequest(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
// 
//		Map<String, String> model = new HashMap<String, String>();
//		model.put("message", "what up");
//		
//        return new ModelAndView("index", model);
//	}
	
	@RequestMapping("/index")
	public ModelAndView landingPage(){
		
		Map<String, String> model = new HashMap<String, String>();
		model.put("message", "what up");
		
        return new ModelAndView("index", model);	
	}
	
	@RequestMapping("/hello")
	public ModelAndView nextPage(){
		
		Map<String, String> model = new HashMap<String, String>();
		model.put("message", "what up");
		
        return new ModelAndView("hello", model);	
	}
	
	public void setWalker(WikiWalker walker){
		this.walker = walker;
	}
}
