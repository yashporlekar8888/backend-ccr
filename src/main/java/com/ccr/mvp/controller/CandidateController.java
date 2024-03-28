package com.ccr.mvp.controller;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccr.mvp.model.Answer;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.CategoryAverageScoreDTO;
import com.ccr.mvp.model.User;
import com.ccr.mvp.service.CandidateService;

@CrossOrigin(origins = "*")
@RestController
public class CandidateController {

	@Autowired
	private CandidateService candidateService;

	@PersistenceContext
	EntityManager entityManager;



	@GetMapping("/candidateListByAadhar")
	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER')")
	public ResponseEntity<?> candidateListByAadhar(@RequestParam Long candidate_aadhar) {
		return candidateService.candidateListByAadhar(candidate_aadhar);
	}

	@GetMapping("/openCandidateprofileByAadhar")
	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER')")
	public ResponseEntity<?> openCandidateprofileByAadhar(@RequestParam Long aadharNumber) {
		return candidateService.openCandidateprofileByAadhar(aadharNumber);
	}

//	// find average score
//	@PostMapping("/getCandidateAverageScore")
//	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER','CANDIDATE')")
//	public ResponseEntity<?> getCandidateAverageScore(@RequestBody Candidate candidate) {
//
//		return candidateService.getCandidateAverageScore(candidate);
//	}
	
	// find average score
	@PostMapping("/getCandidateAverageScore")
	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER','CANDIDATE')")
	public ResponseEntity<?> getCandidateAverageScore(@RequestBody User user) {

		return candidateService.getCandidateAverageScore(user);
	}

//	// find average score
//	@PostMapping("/getCandidateAverageScoreCategory")
//	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER','CANDIDATE')")
//	public ResponseEntity<?> getCandidateAverageScoreCategory(@RequestBody Candidate candidate) {
//
//		return candidateService.getCandidateAverageScoreCategory(candidate);
//	}
	
	// find average score
	@PostMapping("/getCandidateAverageScoreCategory")
	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER','CANDIDATE')")
	public ResponseEntity<?> getCandidateAverageScoreCategory(@RequestBody User user) {

		return candidateService.getCandidateAverageScoreCategory(user);
	}
	
	@GetMapping("/getAllCandidateList")
	@PreAuthorize("hasRole('CCRADMIN')")
	public ResponseEntity<?> getAllCandidateList(){
		return candidateService.getAllCandidateList();
	}
	
//	// Forgot password API
//	@PostMapping(value = "/forgot-password")
//	public ResponseEntity<String> sendOtpByEmail(@RequestBody Candidate candidate)
//			throws UnsupportedEncodingException,MessagingException {
//
//		return candidateService.sendOtpByEmail(candidate);
//	}
//
//	// OTP Validation
//	@PostMapping(value = "/candchangepassforgot")
//	public ResponseEntity<String> candchangepassforgot(@RequestBody Candidate candidate) {
//		return candidateService.candchangepassforgot(candidate);
//	}
//
//	// Change password (Using otp)
//	@PutMapping(value = "/finalcandchangepass")
//	public ResponseEntity<String> finalcandchangepass(@RequestBody Candidate candidate) {
//		return candidateService.finalcandchangepass(candidate);
//	}
	
	// bi-directional mapping sample code (Don'n delete)
/*	@GetMapping("/getAllanswer")
	public  ResponseEntity<?> getAllanswer(@RequestParam Long candidateId) {
		return candidateService.getAllanswer(candidateId);
	}*/
	
}