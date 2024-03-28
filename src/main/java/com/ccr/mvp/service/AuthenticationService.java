package com.ccr.mvp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ccr.mvp.dto.JwtAuthenticationResponse;
import com.ccr.mvp.dto.SignInRequest;
import com.ccr.mvp.dto.SignUpRequest;
import com.ccr.mvp.model.User;

public interface AuthenticationService {

	JwtAuthenticationResponse signin(SignInRequest request);

	ResponseEntity<?> candidateSignup(String userName, String candidateAadhar, String candidateDob, String phoneNumber,
			String email, String password, MultipartFile imageData);

	ResponseEntity<?> hrAdminSignup(SignUpRequest request);

	ResponseEntity<?> recruiterSignup(SignUpRequest request);

	ResponseEntity<?> ccrSignup(SignUpRequest request);

	ResponseEntity<String> sendOtpByEmail(User user);

	ResponseEntity<String> userOtpValidation(User user);

	ResponseEntity<String> userChangePassword(User user);

//	ResponseEntity<?> signout(String token);
}
