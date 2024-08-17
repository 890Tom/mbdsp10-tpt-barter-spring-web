package com.mbds.barter.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.mbds.barter.model.Role;
import com.mbds.barter.model.User;
import com.mbds.barter.response.AuthResponse;

import jakarta.servlet.http.HttpSession;

@Service
public class RoleService {
	@Autowired
    private RestTemplate restTemplate;
	
	@Value("${barter.backend.url}")
	private String baseUrl;
	
	private String getRolesPath() {
        return baseUrl + "roles/";
    }
	
	public List<Role> getAllRole(HttpSession session){
		AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");
	
		if(authResponse == null) {
			throw new InvalidTokenException("You are not logged in. Please log in first.");
		}
		
		try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-auth-token", authResponse.getToken());
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Role[]> response = restTemplate.exchange(
            	this.getRolesPath(),
                HttpMethod.GET,
                entity,
                Role[].class
            );

            return Arrays.asList(response.getBody());
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
