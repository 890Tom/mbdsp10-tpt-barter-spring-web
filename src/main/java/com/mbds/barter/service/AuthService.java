package com.mbds.barter.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.mbds.barter.request.AuthRequest;
import com.mbds.barter.response.AuthResponse;


@Service
public class AuthService {
	@Autowired
    private RestTemplate restTemplate;
	
	String path = "http://localhost:3000/api/auth/";
	
	public AuthResponse login(AuthRequest authRequest) {
		String endPoint = path + "sign-in/";
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<AuthRequest> request = new HttpEntity<>(authRequest, headers);
		
		try {
			ResponseEntity<AuthResponse> response = restTemplate.exchange(endPoint, HttpMethod.POST, request, AuthResponse.class);
			return response.getBody();
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
