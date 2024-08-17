package com.mbds.barter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mbds.barter.exception.InvalidTokenException;
import com.mbds.barter.model.Category;
import com.mbds.barter.model.Report;
import com.mbds.barter.response.PaginatedResponse;
import com.mbds.barter.service.ReportService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReportController {
	@Autowired
    private ReportService reportService;
	
	@GetMapping("/reports/users")
    public String getUsersReports(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit,Model model, HttpSession session, @RequestParam(value = "statut", required = false) String statut, RedirectAttributes redirectAttributes) {
        try {
        	PaginatedResponse<Report> reports = reportService.getAllUsersReports(session, statut, page, limit);
            model.addAttribute("reports", reports.getData());
            model.addAttribute("pagination", reports.getMeta());
            model.addAttribute("selectedStatus", statut);
            return "reports/reports-user";
        } catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
    }
	
	@GetMapping("/reports/objects")
    public String getObjectsReports(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit, Model model, HttpSession session, @RequestParam(value = "statut", required = false) String statut, RedirectAttributes redirectAttributes) {
        try {
        	PaginatedResponse<Report> reports = reportService.getAllObjectsReports(session, statut, page, limit);
            model.addAttribute("reports", reports.getData());
            model.addAttribute("pagination", reports.getMeta());
            model.addAttribute("selectedStatus", statut);
            return "reports/reports-object";
        } catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
    }
	
	@GetMapping("/reports/detail/{id}")
	public String detailReport(@PathVariable("id") String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			Report reportToView = reportService.getReportById(session, id);
			model.addAttribute("report", reportToView);
			return "reports/detail-report";
		} catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
	}
	
	@GetMapping("/reports/approve/{id}")
	public String approveReport(@PathVariable("id") String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			Report reportToView = reportService.approveReport(session, id);
			return "redirect:/reports/detail/" + reportToView.getId();
		}
		catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
	}
	
	@GetMapping("/reports/reject/{id}")
	public String rejectReport(@PathVariable("id") String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			Report reportToView = reportService.rejectReport(session, id);
			return "redirect:/reports/detail/" + reportToView.getId();
		}catch (InvalidTokenException e) {
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error"; 
        } 
	}
}
