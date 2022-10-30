package com.chris.authentication.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.chris.authentication.models.LoginUser;
import com.chris.authentication.models.User;
import com.chris.authentication.services.UserService;

@Controller
public class HomeController {
    
	@Autowired
	private UserService userServ;
    
    
    @GetMapping("/")
    public String index(Model model) {
    
        // Bind empty User and LoginUser objects to the JSP
        // to capture the form input
        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "loginandregister.jsp";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") User newUser, 
            BindingResult result, Model model, HttpSession session) {
    	
    	User registeredUser = userServ.register(newUser, result);
        
    	if(result.hasErrors()) {
    		model.addAttribute("newLogin", new LoginUser());
    		return "loginandregister.jsp";
    	}
        
        
    	session.setAttribute("user", registeredUser);
    
        return "redirect:/success";
    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, 
            BindingResult result, Model model, HttpSession session) {
            
    	User loginUser = userServ.login(newLogin, result);
    	
        if(result.hasErrors()) {
            model.addAttribute("newUser", new User());
            return "loginandregister.jsp";
        }
            
        
        session.setAttribute("user", loginUser);
    
        return "redirect:/success";
    
    }   
    
    @GetMapping("/success")
    public String success(HttpSession session, Model model) {
    	if(session.getAttribute("user") == null) {
    		return "redirect:/";
    	}    	
    	model.addAttribute("user", session.getAttribute("user"));
    	
    	return "success.jsp";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	 	
    	
//    	session.removeAttribute("userId");
    	session.invalidate();
    	return "redirect:/";
    }
}
