package com.mbds.barter.response;

import com.mbds.barter.model.User;

public class AuthResponse {
	User user;
	String token;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
