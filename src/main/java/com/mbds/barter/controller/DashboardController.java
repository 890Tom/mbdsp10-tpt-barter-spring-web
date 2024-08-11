package com.mbds.barter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mbds.barter.model.Category;
import com.mbds.barter.model.CountDashboardInsight;
import com.mbds.barter.response.PaginatedResponse;
import com.mbds.barter.service.DashboardService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
	
	@Autowired
	DashboardService dashboardService;
	
	@GetMapping("/")
	public String viewDashboard(Model model, HttpSession session) {
		try {
			CountDashboardInsight countInsights = dashboardService.getInsights(session);
			model.addAttribute("countInsights", countInsights);
			System.out.println(countInsights.getPending());
            return "dashboard/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login"; 
        } 
	}
}
