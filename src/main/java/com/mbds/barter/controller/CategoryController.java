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
import com.mbds.barter.model.Report;
import com.mbds.barter.response.PaginatedResponse;
import com.mbds.barter.service.CategoryService;
import com.mbds.barter.service.ReportService;

import jakarta.servlet.http.HttpSession;


@Controller
public class CategoryController {
	@Autowired
    private CategoryService categoryService;
	
	
	
	@GetMapping("/categories")
    public String getCategories(
    		@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String title,
            Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
			PaginatedResponse<Category> categories = categoryService.getAllCategory(session, page, limit, title);
			model.addAttribute("categories", categories.getData());
			model.addAttribute("pagination", categories.getMeta());
			model.addAttribute("title", title);
            return "categories/categories";
        }catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
    }
	
	@GetMapping("/categories/add")
	public String showAddCategories(Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("category", new Category());
			return "categories/add-categorie";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
	}
	
	@PostMapping("/categories/add")
	public String addCategories(@ModelAttribute Category category, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			Category categoryAdded = categoryService.addCategory(session, category);
			return "redirect:/categories";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/categories/add"; 
        } 
	}
	
	@GetMapping("/categories/update/{id}")
	public String showUpdateCategorie(@PathVariable("id") int id, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
		try {
			Category categoryToUpdate = categoryService.getCategoryById(session, id);
			model.addAttribute("category", categoryToUpdate);
			return "categories/update-categorie";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
	}
	
	@PostMapping("/categories/update/{id}")
	public String updateCategorie(@PathVariable("id") int id, @ModelAttribute Category category, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			Category categoryToModify = categoryService.updateCategory(session, id, category);
			return "redirect:/categories";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/categories/update/"+id; 
        } 
	}
	
	@GetMapping("/categories/detail/{id}")
	public String detailCategorie(@PathVariable("id") int id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			Category categoryToView = categoryService.getCategoryById(session, id);
			model.addAttribute("category", categoryToView);
			return "categories/detail-categorie";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategorie(@PathVariable("id") int id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			categoryService.deleteCategory(session, id);
			return "redirect:/categories";
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
	
	
}
