package com.ccr.mvp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccr.mvp.model.Recruiter;

import com.ccr.mvp.model.Role;

import com.ccr.mvp.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	  Optional<User> findByEmail(String email);

		User findByUserId(Long userId);

		User findByemail(String email);

		List<User> findAllByRole(Role role);


	
}
