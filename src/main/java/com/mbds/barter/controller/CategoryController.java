package com.mbds.barter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mbds.barter.exception.InvalidTokenException;
import com.mbds.barter.model.Category;
import com.mbds.barter.model.Report;
import com.mbds.barter.service.CategoryService;
import com.mbds.barter.service.ReportService;

import jakarta.servlet.http.HttpSession;


@Controller
public class CategoryController {
	@Autowired
    private CategoryService categoryService;
	
	@Autowired
    private ReportService reportService;
	
	@GetMapping("/categories")
    public String getCategories(Model model, HttpSession session) {
        try {
			List<Category> categories = categoryService.getAllCategory(session);
			model.addAttribute("categories", categories);
            return "categories/categories";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
    }
	
	@GetMapping("/categories/add")
	public String showAddCategories(Model model, HttpSession session) {
		try {
			model.addAttribute("category", new Category());
			return "categories/add-categorie";
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
	
	@PostMapping("/categories/add")
	public String addCategories(@ModelAttribute Category category, Model model, HttpSession session) {
		try {
			Category categoryAdded = categoryService.addCategory(session, category);
			return "redirect:/categories";
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
	
	@GetMapping("/categories/update/{id}")
	public String showUpdateCategorie(@PathVariable("id") int id, Model model, HttpSession session) {
		try {
			Category categoryToUpdate = categoryService.getCategoryById(session, id);
			model.addAttribute("category", categoryToUpdate);
			return "categories/update-categorie";
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
	
	@PostMapping("/categories/update/{id}")
	public String updateCategorie(@PathVariable("id") int id, @ModelAttribute Category category, Model model, HttpSession session) {
		try {
			Category categoryToModify = categoryService.updateCategory(session, id, category);
			return "redirect:/categories";
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
	
	@GetMapping("/categories/detail/{id}")
	public String detailCategorie(@PathVariable("id") int id, Model model, HttpSession session) {
		try {
			Category categoryToView = categoryService.getCategoryById(session, id);
			model.addAttribute("category", categoryToView);
			return "categories/detail-categorie";
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
	
	@GetMapping("/reports")
    public String getReports(Model model, HttpSession session) {
        try {
        	
            List<Report> reports = reportService.getAllReports(session);
            model.addAttribute("reports", reports);
            System.out.println(reports.size());
            return "homepage";
        } catch (InvalidTokenException e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "homepage"; 
        } 
    }
}
