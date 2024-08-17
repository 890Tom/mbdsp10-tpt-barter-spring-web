package com.mbds.barter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mbds.barter.exception.InvalidTokenException;
import com.mbds.barter.model.Category;
import com.mbds.barter.model.CountDashboardInsight;
import com.mbds.barter.model.DateCountInsight;
import com.mbds.barter.response.PaginatedResponse;
import com.mbds.barter.service.DashboardService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
	
	@Autowired
	DashboardService dashboardService;
	
	@GetMapping("/")
	public String viewDashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			CountDashboardInsight countInsights = dashboardService.getInsights(session);
			List<DateCountInsight> daysInsight = dashboardService.get14DaysExchange(session);
			List<DateCountInsight> declinedDaysInsight = dashboardService.get14DaysDeclinedExchange(session);
			model.addAttribute("countInsights", countInsights);
			model.addAttribute("daysInsight", daysInsight);
			model.addAttribute("declinedDaysInsight", declinedDaysInsight);
			
            return "dashboard/dashboard";
        }catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error";
        } 
	}
}
