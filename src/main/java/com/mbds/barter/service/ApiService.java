package com.mbds.barter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {
	private final RestTemplate restTemplate;

    public ApiService() {
        this.restTemplate = new RestTemplate();
    }

    public String getApiResponse() {
        String url = "https://api.example.com/data";
        return restTemplate.getForObject(url, String.class);
    }
}
