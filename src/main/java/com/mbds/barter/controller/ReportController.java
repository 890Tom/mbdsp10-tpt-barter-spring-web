package com.mbds.barter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mbds.barter.exception.InvalidTokenException;
import com.mbds.barter.model.Category;
import com.mbds.barter.model.Report;
import com.mbds.barter.service.ReportService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReportController {
	@Autowired
    private ReportService reportService;
	
	@GetMapping("/reports/users")
    public String getUsersReports(Model model, HttpSession session, @RequestParam(value = "statut", required = false) String statut) {
        try {
            List<Report> reports = reportService.getAllUsersReports(session, statut);
            model.addAttribute("reports", reports);
            model.addAttribute("selectedStatus", statut);
            return "reports/reports-user";
        } catch (InvalidTokenException e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/login"; 
        } 
    }
	
	@GetMapping("/reports/objects")
    public String getObjectsReports(Model model, HttpSession session, @RequestParam(value = "statut", required = false) String statut) {
        try {
            List<Report> reports = reportService.getAllObjectsReports(session, statut);
            model.addAttribute("reports", reports);
            model.addAttribute("selectedStatus", statut);
            return "reports/reports-object";
        } catch (InvalidTokenException e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/login"; 
        } 
    }
	
	@GetMapping("/reports/detail/{id}")
	public String detailReport(@PathVariable("id") String id, Model model, HttpSession session) {
		try {
			Report reportToView = reportService.getReportById(session, id);
			model.addAttribute("report", reportToView);
			return "reports/detail-report";
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "redirect:/login"; 
        } 
	}
	
	@GetMapping("/reports/approve/{id}")
	public String approveReport(@PathVariable("id") String id, Model model, HttpSession session) {
		try {
			Report reportToView = reportService.approveReport(session, id);
			return "redirect:/reports/detail/" + reportToView.getId();
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "redirect:/login"; 
        } 
	}
	
	@GetMapping("/reports/reject/{id}")
	public String rejectReport(@PathVariable("id") String id, Model model, HttpSession session) {
		try {
			Report reportToView = reportService.rejectReport(session, id);
			return "redirect:/reports/detail/" + reportToView.getId();
		} catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
            return "redirect:/login"; 
        } 
	}
}
