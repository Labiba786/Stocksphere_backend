package com.personal.stocksphere.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.personal.stocksphere.dto.UserDto;
import com.personal.stocksphere.entity.User;
import com.personal.stocksphere.exceptions.UserAlreadyExistException;
import com.personal.stocksphere.exceptions.UserDoesNotExistsException;
import com.personal.stocksphere.repository.UserRepository;
import com.personal.stocksphere.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository repo;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;

	@Override
	public User createUser(UserDto user) throws UserAlreadyExistException {
		// TODO Auto-generated method stub
		User resp = this.repo.findByUsername(user.getUsername());
		if(resp!=null) {
			throw new UserAlreadyExistException("User already Exists!!");
		}else {
			User u = new User();
			u.setFullName(user.getFullName());
			u.setEmail(user.getEmail());
			u.setUsername(user.getUsername());
			u.setPassword(passwordEncoder.encode(user.getPassword()));
			u.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
			
			User result = this.repo.save(u);
			return result;
		}
	}

	@Override
	public User getUser(String username) throws UserDoesNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateUser(Long id, User user) throws UserDoesNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

}
