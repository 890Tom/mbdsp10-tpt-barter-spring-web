package com.mbds.barter.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.mbds.barter.exception.BadRequestException;
import com.mbds.barter.exception.InternalServerException;
import com.mbds.barter.exception.InvalidTokenException;
import com.mbds.barter.model.Category;
import com.mbds.barter.model.Report;
import com.mbds.barter.response.AuthResponse;
import com.mbds.barter.response.PaginatedResponse;

import jakarta.servlet.http.HttpSession;

@Service
public class ReportService {
	@Autowired
    private RestTemplate restTemplate;
	
	
	@Value("${barter.backend.url}")
	private String baseUrl;
	
	private String getReportsPath() {
        return baseUrl + "reports/";
    }
	
	public PaginatedResponse<Report> getAllUsersReports(HttpSession session, String statut, int page, int limit) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        headers.set("Content-Type", "application/json");
	        HttpEntity<String> entity = new HttpEntity<>(headers);

	        StringBuilder reportsPath = new StringBuilder(this.getReportsPath()).append("admin/?type=user");
	        
	        if (statut != null && !statut.isEmpty()) {
	            reportsPath.append("&statut=").append(statut);
	        }
	        
	        reportsPath.append("&page=").append(page).append("&limit=").append(limit);

	        ResponseEntity<PaginatedResponse<Report>> response = restTemplate.exchange(
	            reportsPath.toString(),
	            HttpMethod.GET,
	            entity,
	            new ParameterizedTypeReference<PaginatedResponse<Report>>() {}
	        );

	        return response.getBody();
	    } catch (HttpClientErrorException.Unauthorized e) {
	        throw new InvalidTokenException("Invalid token provided.");
	    } catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
	            throw new BadRequestException("Invalid request");
	        }
	        throw new InternalServerException("An error occurred while processing the request");
	    } catch (HttpServerErrorException e) {
	        throw new InternalServerException("Internal server error, please try again later");
	    } catch (Exception e) {
	    	e.printStackTrace();
	        throw new InternalServerException("An unexpected error occurred");
	    }
	}
	
	public PaginatedResponse<Report> getAllObjectsReports(HttpSession session, String statut, int page, int limit) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        headers.set("Content-Type", "application/json");
	        HttpEntity<String> entity = new HttpEntity<>(headers);

	        StringBuilder reportsPath = new StringBuilder(this.getReportsPath()).append("admin/?type=post");
	        
	        if (statut != null && !statut.isEmpty()) {
	            reportsPath.append("&statut=").append(statut);
	        }
	        
	        reportsPath.append("&page=").append(page).append("&limit=").append(limit);

	        ResponseEntity<PaginatedResponse<Report>> response = restTemplate.exchange(
	            reportsPath.toString(),
	            HttpMethod.GET,
	            entity,
	            new ParameterizedTypeReference<PaginatedResponse<Report>>() {}
	        );

	        return response.getBody();
	    } catch (HttpClientErrorException.Unauthorized e) {
	        throw new InvalidTokenException("Invalid token provided.");
	    } catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
	            throw new BadRequestException("Invalid request");
	        }
	        throw new InternalServerException("An error occurred while processing the request");
	    } catch (HttpServerErrorException e) {
	        throw new InternalServerException("Internal server error, please try again later");
	    } catch (Exception e) {
	        throw new InternalServerException("An unexpected error occurred");
	    }
	}
	
	public Report getReportById(HttpSession session, String reportId) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        HttpEntity<String> entity = new HttpEntity<>(headers);

	        // Construire l'URL avec l'ID du report
	        String url = String.format("%s/%s", this.getReportsPath(), reportId);

	        // Effectuer la requête GET pour obtenir une seule report
	        ResponseEntity<Report> response = restTemplate.exchange(
	            url,
	            HttpMethod.GET,
	            entity,
	            Report.class
	        );

	        return response.getBody();
	    } catch (HttpClientErrorException.Unauthorized e) {
	        throw new InvalidTokenException("Invalid token provided.");
	    } catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
	            throw new BadRequestException("Invalid request");
	        }
	        throw new InternalServerException("An error occurred while processing the request");
	    } catch (HttpServerErrorException e) {
	        throw new InternalServerException("Internal server error, please try again later");
	    } catch (Exception e) {
	    	e.printStackTrace();
	        throw new InternalServerException("An unexpected error occurred");
	    }
	}
	
	public Report approveReport(HttpSession session, String reportId) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	    	Report updatedReport =this.getReportById(session, reportId);
	    	updatedReport.setStatut("accepted");
	    	
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        headers.set("Content-Type", "application/json");
	        HttpEntity<Report> entity = new HttpEntity<>(updatedReport, headers);

	        // Construire l'URL avec l'ID du signalement à mettre à jour
	        String url = this.getReportsPath() + reportId;

	        // Effectuer la requête PUT pour mettre à jour le signalement
	        ResponseEntity<Report> response = restTemplate.exchange(
	            url,
	            HttpMethod.PUT,
	            entity,
	            Report.class
	        );
	        
	        return response.getBody();
	    } catch (HttpClientErrorException.Unauthorized e) {
	        throw new InvalidTokenException("Invalid token provided.");
	    } catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
	            throw new BadRequestException("Invalid data provided");
	        } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	            throw new InternalServerException("Category not found");
	        }
	        throw new InternalServerException("An error occurred while processing the request");
	    } catch (HttpServerErrorException e) {
	        throw new InternalServerException("Internal server error, please try again later");
	    } catch (Exception e) {
	        throw new InternalServerException("An unexpected error occurred");
	    }
	}
	
	public Report rejectReport(HttpSession session, String reportId) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	    	Report updatedReport =this.getReportById(session, reportId);
	    	updatedReport.setStatut("rejected");
	    	
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        headers.set("Content-Type", "application/json");
	        HttpEntity<Report> entity = new HttpEntity<>(updatedReport, headers);

	        // Construire l'URL avec l'ID du signalement à mettre à jour
	        String url = this.getReportsPath() + reportId;

	        // Effectuer la requête PUT pour mettre à jour le signalement
	        ResponseEntity<Report> response = restTemplate.exchange(
	            url,
	            HttpMethod.PUT,
	            entity,
	            Report.class
	        );
	        
	        return response.getBody();
	    } catch (HttpClientErrorException.Unauthorized e) {
	        throw new InvalidTokenException("Invalid token provided.");
	    } catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
	            throw new BadRequestException("Invalid data provided");
	        } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	            throw new InternalServerException("Category not found");
	        }
	        throw new InternalServerException("An error occurred while processing the request");
	    } catch (HttpServerErrorException e) {
	        throw new InternalServerException("Internal server error, please try again later");
	    } catch (Exception e) {
	        throw new InternalServerException("An unexpected error occurred");
	    }
	}
}
