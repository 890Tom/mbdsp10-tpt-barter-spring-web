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
import com.mbds.barter.response.AuthResponse;
import com.mbds.barter.response.PaginatedResponse;

import jakarta.servlet.http.HttpSession;

@Service
public class CategoryService {

	@Autowired
    private RestTemplate restTemplate;
	
	
	
	@Value("${barter.backend.url}")
	private String baseUrl;
	
	private String getCategoriesPath() {
        return baseUrl + "categories/";
    }
	
	public PaginatedResponse<Category> getAllCategory(HttpSession session, int page, int limit,String title){
		AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");
	
		if(authResponse == null) {
			throw new InvalidTokenException("You are not logged in. Please log in first.");
		}
		
		try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-auth-token", authResponse.getToken());
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            String pathWithPagination = String.format("%sadmin/?page=%d&limit=%d", this.getCategoriesPath(), page, limit);
            if (title != null && !title.isEmpty()) {
                pathWithPagination += "&title=" + URLEncoder.encode(title, "UTF-8");
            }

            ResponseEntity<PaginatedResponse<Category>> response = restTemplate.exchange(
            	pathWithPagination,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<PaginatedResponse<Category>>() {}
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
	
	
	public Category getCategoryById(HttpSession session, int categoryId) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        HttpEntity<String> entity = new HttpEntity<>(headers);

	        // Construire l'URL avec l'ID de la catégorie
	        String url = String.format("%s/%d", this.getCategoriesPath(), categoryId);

	        // Effectuer la requête GET pour obtenir une seule catégorie
	        ResponseEntity<Category> response = restTemplate.exchange(
	            url,
	            HttpMethod.GET,
	            entity,
	            Category.class
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
	
	public Category addCategory(HttpSession session, Category newCategory) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        headers.set("Content-Type", "application/json");
	        HttpEntity<Category> entity = new HttpEntity<>(newCategory, headers);

	        // Effectuer la requête POST pour ajouter une nouvelle catégorie
	        ResponseEntity<Category> response = restTemplate.exchange(
	        		this.getCategoriesPath(), // Assurez-vous que le path est l'URL de l'endpoint POST pour ajouter une catégorie
	            HttpMethod.POST,
	            entity,
	            Category.class
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
	
	
	public void deleteCategory(HttpSession session, int categoryId) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        headers.set("Content-Type", "application/json");
	        HttpEntity<Void> entity = new HttpEntity<>(headers);

	        // Construire l'URL avec l'ID de la catégorie à supprimer
	        String url = this.getCategoriesPath() + categoryId;

	        // Effectuer la requête DELETE pour supprimer la catégorie
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
	
	
	public Category updateCategory(HttpSession session, int categoryId, Category updatedCategory) {
	    AuthResponse authResponse = (AuthResponse) session.getAttribute("authResponse");

	    if (authResponse == null) {
	        throw new InvalidTokenException("You are not logged in. Please log in first.");
	    }

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("x-auth-token", authResponse.getToken());
	        headers.set("Content-Type", "application/json");
	        HttpEntity<Category> entity = new HttpEntity<>(updatedCategory, headers);

	        // Construire l'URL avec l'ID de la catégorie à mettre à jour
	        String url = this.getCategoriesPath() + categoryId;

	        // Effectuer la requête PUT pour mettre à jour la catégorie
	        ResponseEntity<Category> response = restTemplate.exchange(
	            url,
	            HttpMethod.PUT,
	            entity,
	            Category.class
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
