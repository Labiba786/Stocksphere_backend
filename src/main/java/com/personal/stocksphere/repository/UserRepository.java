package com.personal.stocksphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.personal.stocksphere.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	public User findByUsername(String username);

}
