package com.mbds.barter.service;

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
import com.mbds.barter.model.User;
import com.mbds.barter.request.AuthRequest;
import com.mbds.barter.response.AuthResponse;


@Service
public class AuthService {
	@Autowired
    private RestTemplate restTemplate;
	
	@Value("${barter.backend.url}")
	private String baseUrl;
	
	private String getAuthPath() {
        return baseUrl + "auth/";
    }
	
	
	public AuthResponse login(AuthRequest authRequest) {
		String endPoint = this.getAuthPath() + "login/";
		System.out.println(endPoint);
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<AuthRequest> request = new HttpEntity<>(authRequest, headers);
		
		try {
			ResponseEntity<AuthResponse> response = restTemplate.exchange(endPoint, HttpMethod.POST, request, AuthResponse.class);
			if(this.isAdmin(response.getBody().getUser())) {
				return response.getBody();
			}else {
				throw new BadRequestException("Invalid email or password");
			}
		} catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST ) {
                throw new BadRequestException("Invalid email or password");
            }
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED ) {
                throw new BadRequestException("Invalid email or password");
            }
            throw new InternalServerException("An error occurred while processing the request");
        } catch (HttpServerErrorException e) {
            throw new InternalServerException("Internal server error, please try again later");
        } catch (Exception e) {
            throw new InternalServerException("An unexpected error occurred");
        }
		
	}
	
	public boolean isAdmin(User user) {
		return user.getRoleId() == 1;
	}
}
