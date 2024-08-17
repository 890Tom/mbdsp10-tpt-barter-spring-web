package com.mbds.barter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mbds.barter.exception.InvalidTokenException;
import com.mbds.barter.model.Category;
import com.mbds.barter.model.User;
import com.mbds.barter.response.PaginatedResponse;
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
    public String getUsers(
    		@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer roleId,
            Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
        	PaginatedResponse<User> users = userService.getAllUser(session, page, limit, email, roleId);
			model.addAttribute("users", users.getData());
			model.addAttribute("pagination", users.getMeta());
			model.addAttribute("email", email);
			model.addAttribute("roleId", roleId);
			model.addAttribute("roles", roleService.getAllRole(session));
            return "users/users";
        }catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
    }
	
	@GetMapping("/users/detail/{id}")
	public String detailUsers(@PathVariable("id") int id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			User userToView = userService.getUserById(session, id);
			model.addAttribute("user", userToView);
			model.addAttribute("roles", roleService.getAllRole(session));
			return "users/detail-user";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
	}
	
	@GetMapping("/profil")
	public String myProfil(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			User userToView = userService.getProfile(session);
			model.addAttribute("user", userToView);
			model.addAttribute("roles", roleService.getAllRole(session));
			return "users/detail-user";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable("id") int id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			userService.deleteUser(session, id);
			return "redirect:/users";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
	}
	
	@GetMapping("/users/add")
	public String showAddUsers(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("user", new User());
			model.addAttribute("roles", roleService.getAllRole(session));
			return "users/add-user";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
	}
	
	@PostMapping("/users/add")
	public String addUsers(@ModelAttribute User user, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			User userAdded = userService.addUser(session, user);
			return "redirect:/users";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users/add"; 
        } 
	}
	
	@GetMapping("/users/update/{id}")
	public String showUpdateUser(@PathVariable("id") int id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			User userToUpdate = userService.getUserById(session, id);
			model.addAttribute("user", userToUpdate);
			model.addAttribute("roles", roleService.getAllRole(session));
			return "users/update-user";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
	}
	
	@PostMapping("/users/update/{id}")
	public String updateUser(@PathVariable("id") int id, @ModelAttribute User user, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			User userToModify = userService.updateUser(session, id, user);
			return "redirect:/users/detail/"+id;
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users/update/"+id; 
        } 
	}
}
