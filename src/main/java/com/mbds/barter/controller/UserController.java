package com.mbds.barter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mbds.barter.model.Category;
import com.mbds.barter.model.User;
import com.mbds.barter.service.RoleService;
import com.mbds.barter.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;	
	
	@GetMapping("/users")
    public String getUsers(Model model, HttpSession session) {
        try {
			List<User> users = userService.getAllUser(session);
			model.addAttribute("users", users);
            return "users/users";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
    }
	
	@GetMapping("/users/detail/{id}")
	public String detailUsers(@PathVariable("id") int id, Model model, HttpSession session) {
		try {
			User userToView = userService.getUserById(session, id);
			model.addAttribute("user", userToView);
			return "users/detail-user";
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
	
	@GetMapping("/profil")
	public String myProfil(Model model, HttpSession session) {
		try {
			User userToView = userService.getProfile(session);
			model.addAttribute("user", userToView);
			return "users/detail-user";
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable("id") int id, Model model, HttpSession session) {
		try {
			userService.deleteUser(session, id);
			return "redirect:/users";
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
	
	@GetMapping("/users/add")
	public String showAddUsers(Model model, HttpSession session) {
		try {
			model.addAttribute("user", new User());
			model.addAttribute("roles", roleService.getAllRole(session));
			return "users/add-user";
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
	
	@PostMapping("/users/add")
	public String addUsers(@ModelAttribute User user, Model model, HttpSession session) {
		try {
			User userAdded = userService.addUser(session, user);
			return "redirect:/users";
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
}
