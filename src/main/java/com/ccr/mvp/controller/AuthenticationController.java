package com.ccr.mvp.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ccr.mvp.dto.JwtAuthenticationResponse;
import com.ccr.mvp.dto.SignInRequest;
import com.ccr.mvp.dto.SignUpRequest;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.User;
import com.ccr.mvp.service.AuthenticationService;
import com.ccr.mvp.service.JwtService;
import com.ccr.mvp.service.impl.TokenService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthenticationController {

	@Autowired
	private final AuthenticationService authenticationService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private TokenService tokenService;

//	@PostMapping("/candidateSignup")
//	public ResponseEntity<?> candidateSignup(
//	     @RequestBody SignUpRequest request) {
//	    return authenticationService.candidateSignup(request);
//	}

	@PostMapping("/candidateSignup")
	public ResponseEntity<?> candidateSignup(@RequestPart("userName") String userName,
			@RequestPart("candidateAadhar") String candidateAadhar, @RequestPart("candidateDob") String candidateDob,
			@RequestPart("phoneNumber") String phoneNumber, @RequestPart("email") String email,
			@RequestPart("password") String password, @RequestPart("imageData") MultipartFile imageData) {
		return authenticationService.candidateSignup(userName, candidateAadhar, candidateDob, phoneNumber, email,
				password, imageData);
	}

	@PostMapping("/hrAdminSignup")
	public ResponseEntity<?> hrAdminSignup(@RequestBody SignUpRequest request) {
		return authenticationService.hrAdminSignup(request);
	}

	@PostMapping("/recruiterSignup")
	public ResponseEntity<?> recruiterSignup(@RequestBody SignUpRequest request) {
		return authenticationService.recruiterSignup(request);
	}

	@PostMapping("/ccrAdminSignup")
//    @PreAuthorize("hasRole('SUPERADMIN')")
	public ResponseEntity<?> ccrSignup(@RequestBody SignUpRequest request) {
		return authenticationService.ccrSignup(request);
	}

	@PostMapping("/signin")
	public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
		return authenticationService.signin(request);
	}

	// Forgot password API
	@PostMapping("/forgot-password")
	public ResponseEntity<String> sendOtpByEmail(@RequestBody User user)
			throws UnsupportedEncodingException, MessagingException {

		return authenticationService.sendOtpByEmail(user);
	}

	// OTP Validation
	@PostMapping(value = "/userOtpValidation")
	public ResponseEntity<String> userOtpValidation(@RequestBody User user) {
		return authenticationService.userOtpValidation(user);
	}

	// Change password (Using otp)
	@PutMapping(value = "/userChangePassword")
	public ResponseEntity<String> userChangePassword(@RequestBody User user) {
		return authenticationService.userChangePassword(user);
	}

	@PostMapping("/signout")
	public ResponseEntity<String> signout(@RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7); // Remove "Bearer " prefix

		tokenService.invalidateToken(jwtToken);

		return ResponseEntity.ok("User successfully signed out.");
	}

}
