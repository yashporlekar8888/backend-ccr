package com.ccr.mvp.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ccr.mvp.model.User;

public interface UserService {


	UserDetailsService userDetailsService();

	User saveUser(User user);
}
