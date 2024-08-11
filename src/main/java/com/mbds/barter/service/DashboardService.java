package com.mbds.barter.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.mbds.barter.model.CountDashboardInsight;
import com.mbds.barter.model.User;
import com.mbds.barter.response.AuthResponse;
import com.mbds.barter.response.PaginatedResponse;

import jakarta.servlet.http.HttpSession;

@Service
public class DashboardService {
	@Autowired
    private RestTemplate restTemplate;
	
	String path = "http://localhost:3000/api/dashboard/";
	
	public CountDashboardInsight getInsights(HttpSession session) {
		AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }
	    
	    try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-auth-token", authResponse.getToken());
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<CountDashboardInsight> response = restTemplate.exchange(
            	path + "count-insights",
                HttpMethod.GET,
                entity,
                CountDashboardInsight.class
            );

            return response.getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new InvalidTokenException("Invalid token provided.");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new BadRequestException("Invalid email or password");
            }
            throw new InternalServerException("An error occurred while processing the request");
        } catch (HttpServerErrorException e) {
            throw new InternalServerException("Internal server error, please try again later");
        } catch (Exception e) {
            throw new InternalServerException("An unexpected error occurred");
        }
	}
}
