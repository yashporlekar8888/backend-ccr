package com.ccr.mvp.dto;

import org.springframework.web.multipart.MultipartFile;

import com.ccr.mvp.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

	String userName;
	String email;
	String password;
	Long phoneNumber;
	Role role;


	// For candidate
	Long candidateAadhar;
	String candidateDob;

	// For HR Admin & Company
	String companyName;
	String companyAddress;
	Long companyPhoneNumber;
	Long companyTan;

	// For Recruiter
	Long userId;
	boolean addedPower;
	boolean approvePower;
}