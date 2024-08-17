package com.mbds.barter.service;

import java.net.URLEncoder;
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
import com.mbds.barter.model.User;
import com.mbds.barter.response.AuthResponse;
import com.mbds.barter.response.PaginatedResponse;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {
	@Autowired
    private RestTemplate restTemplate;
	
	
	@Value("${barter.backend.url}")
	private String baseUrl;
	
	private String getUserPath() {
        return baseUrl + "users/";
    }
	
	private String getUserPathCreate() {
        return baseUrl + "auth/register/";
    }
	
	public PaginatedResponse<User> getAllUser(HttpSession session, int page, int limit, String email, Integer roleId){
		AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");
	
		if(authResponse == null) {
			throw new InvalidTokenException("You are not logged in. Please log in first.");
		}
		
		try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-auth-token", authResponse.getToken());
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            String pathWithPagination = String.format("%sadmin/?page=%d&limit=%d", this.getUserPath(), page, limit);
            if (email != null && !email.isEmpty()) {
                pathWithPagination += "&email=" + URLEncoder.encode(email, "UTF-8");
            }
            if (roleId != null) {
                pathWithPagination += "&roleId=" + roleId;
            }
            ResponseEntity<PaginatedResponse<User>> response = restTemplate.exchange(
            	pathWithPagination,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<PaginatedResponse<User>>() {}
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
	
	public User getUserById(HttpSession session, int userId) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        HttpEntity<String> entity = new HttpEntity<>(headers);

	        // Construire l'URL avec l'ID de l'user
	        String url = String.format("%s/%d", this.getUserPath(), userId);

	        // Effectuer la requête GET pour obtenir un user
	        ResponseEntity<User> response = restTemplate.exchange(
	            url,
	            HttpMethod.GET,
	            entity,
	            User.class
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
	
	public User getProfile(HttpSession session) {
		AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }
	    
	    return authResponse.getUser();
	}
	
	public void deleteUser(HttpSession session, int userId) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        headers.set("Content-Type", "application/json");
	        HttpEntity<Void> entity = new HttpEntity<>(headers);

	        // Construire l'URL avec l'ID de l'user à supprimer
	        String url = this.getUserPath() + userId;

	        // Effectuer la requête DELETE pour supprimer l'user
	        restTemplate.exchange(
	            url,
	            HttpMethod.DELETE,
	            entity,
	            Void.class
	        );
	    } catch (HttpClientErrorException.Unauthorized e) {
	        throw new InvalidTokenException("Invalid token provided.");
	    } catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	            throw new InternalServerException("Category not found");
	        }
	        throw new InternalServerException("An error occurred while processing the request");
	    } catch (HttpServerErrorException e) {
	        throw new InternalServerException("Internal server error, please try again later");
	    } catch (Exception e) {
	        throw new InternalServerException("An unexpected error occurred");
	    }
	}
	
	public User addUser(HttpSession session, User newUser) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        headers.set("Content-Type", "application/json");
	        HttpEntity<User> entity = new HttpEntity<>(newUser, headers);

	        ResponseEntity<User> response = restTemplate.exchange(
	        	this.getUserPathCreate(),
	            HttpMethod.POST,
	            entity,
	            User.class
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
	
	public User updateUser(HttpSession session, int userId, User updatedUser) {
		AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }
	    
	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        headers.set("Content-Type", "application/json");
	        HttpEntity<User> entity = new HttpEntity<>(updatedUser, headers);

	        // Construire l'URL avec l'ID de la catégorie à mettre à jour
	        String url = this.getUserPath() + userId;

	        // Effectuer la requête PUT pour mettre à jour la catégorie
	        ResponseEntity<User> response = restTemplate.exchange(
	            url,
	            HttpMethod.PUT,
	            entity,
	            User.class
	        );
	        
	        return response.getBody();
	    } catch (HttpClientErrorException.Unauthorized e) {
	        throw new InvalidTokenException("Invalid token provided.");
	    } catch (HttpClientErrorException e) {
	        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
	            throw new BadRequestException("Invalid data provided");
	        } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	            throw new InternalServerException("User not found");
	        }
	        throw new InternalServerException("An error occurred while processing the request");
	    } catch (HttpServerErrorException e) {
	        throw new InternalServerException("Internal server error, please try again later");
	    } catch (Exception e) {
	        throw new InternalServerException("An unexpected error occurred");
	    }
	}
}
