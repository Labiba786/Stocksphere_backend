package com.personal.stocksphere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.stocksphere.config.JwtUtils;
import com.personal.stocksphere.dto.JwtRequest;
import com.personal.stocksphere.dto.UserDto;
import com.personal.stocksphere.entity.User;
import com.personal.stocksphere.exceptions.UserAlreadyExistException;
import com.personal.stocksphere.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Autowired 
	private AuthenticationManager authenticationManager;
	
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	
	@PostMapping("/login")
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest request, HttpServletRequest httpRequest, HttpServletResponse response) {
	
	    try {
	        this.authenticate(request.getUsername(), request.getPassword());
	        UserDetails user = this.userDetailsService.loadUserByUsername(request.getUsername());
	        String token = this.jwtUtils.generateToken(user);
	
	        // Determine if the request is secure (HTTPS)
	        boolean isSecure = httpRequest.isSecure();
	
	        // Create a cookie for the token
	        Cookie cookie = new Cookie("token", token);
	        cookie.setHttpOnly(true);
	        cookie.setSecure(isSecure);
	        cookie.setPath("/");
	
	        // Add the SameSite attribute
	        String sameSiteAttribute = "SameSite=None";
	        if (isSecure) {
	            response.addHeader("Set-Cookie", cookie.getName() + "=" + cookie.getValue() + "; " + sameSiteAttribute + "; Secure; HttpOnly; Path=" + cookie.getPath());
	        } else {
	            response.addHeader("Set-Cookie", cookie.getName() + "=" + cookie.getValue() + "; " + sameSiteAttribute + "; HttpOnly; Path=" + cookie.getPath());
	        }
	
	        return new ResponseEntity<>(user, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	    }
	}

	
	
	private void authenticate(String username, String password) throws Exception {
		
		
		try {
			authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(username, password));
		}catch(DisabledException e) {
			throw new Exception("User Disabled"+e.getMessage());
		}catch(BadCredentialsException e) {
			throw new Exception("Bad credentials"+e.getMessage());
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addNewUser(@RequestBody UserDto user){
		try {
			User result = this.service.createUser(user);
			return new ResponseEntity<User>(result, HttpStatus.CREATED);
		} catch (UserAlreadyExistException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
}
