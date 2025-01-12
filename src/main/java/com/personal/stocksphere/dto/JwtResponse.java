package com.personal.stocksphere.dto;

import org.springframework.security.core.userdetails.UserDetails;

public class JwtResponse {
	private UserDetails user;
	public UserDetails getUser() {
		return user;
	}

	public void setUser(UserDetails user) {
		this.user = user;
	}

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public JwtResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JwtResponse(UserDetails user, String token) {
		super();
		this.user = user;
		this.token = token;
	}

	

}
