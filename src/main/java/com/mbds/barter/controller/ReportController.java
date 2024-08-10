package com.mbds.barter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public String getUsersReports(Model model, HttpSession session) {
        try {
            List<Report> reports = reportService.getAllUsersReports(session);
            model.addAttribute("reports", reports);
            System.out.println(reports.get(0).getId());
            return "reports/reports-user";
        } catch (InvalidTokenException e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/login"; 
        } 
    }
	
	@GetMapping("/reports/objects")
    public String getObjectsReports(Model model, HttpSession session) {
        try {
            List<Report> reports = reportService.getAllObjectsReports(session);
            model.addAttribute("reports", reports);
            System.out.println(reports.get(0).getUserMakeReport());
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
}
