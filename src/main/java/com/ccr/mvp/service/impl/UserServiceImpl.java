package com.ccr.mvp.service.impl;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ccr.mvp.model.User;
import com.ccr.mvp.repository.UserRepository;
import com.ccr.mvp.service.UserService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;

	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) {
				return userRepository.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			}
		};
	}

	public User saveUser(User newUser) {
		if (newUser.getUserId() == null) {
			newUser.setCreatedAt(LocalDateTime.now());
		}

		newUser.setUpdatedAt(LocalDateTime.now());
		return userRepository.save(newUser);
	}

}