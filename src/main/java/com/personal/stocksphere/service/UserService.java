package com.personal.stocksphere.service;

import com.personal.stocksphere.dto.UserDto;
import com.personal.stocksphere.entity.User;
import com.personal.stocksphere.exceptions.UserAlreadyExistException;
import com.personal.stocksphere.exceptions.UserDoesNotExistsException;

public interface UserService {
	
	public User createUser(UserDto user) throws UserAlreadyExistException;
	
	public User getUser(String username) throws UserDoesNotExistsException;
	
	public User updateUser(Long id, User user) throws UserDoesNotExistsException;

}
