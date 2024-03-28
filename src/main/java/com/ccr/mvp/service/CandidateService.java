package com.ccr.mvp.service;



import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ccr.mvp.model.Answer;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.CategoryAverageScoreDTO;
import com.ccr.mvp.model.User;



public interface CandidateService {

	//Registeration candidate
	Candidate candidateRegistration(Candidate candidate);

	// list of candidates while searching them with aadhar
	ResponseEntity<?> candidateListByAadhar(Long candidate_aadhar);
	// open the profile of respective candidate after searching
	ResponseEntity<?> openCandidateprofileByAadhar(Long candidateAadhar);
	
	
//	ResponseEntity<?> getCandidateAverageScore(Candidate candidate);
	ResponseEntity<?> getCandidateAverageScore(User user);
//	ResponseEntity<?> getCandidateAverageScoreCategory(Candidate candidate);	
	ResponseEntity<?> getCandidateAverageScoreCategory(User user);
//	ResponseEntity<?> getCandidateAverageScore(Long candidateId);

	ResponseEntity<?> getAllCandidateList();
	
//	//forgot password email
//	ResponseEntity<String> sendOtpByEmail(Candidate candidate);
//	ResponseEntity<String> candchangepassforgot(Candidate candidate);
//	ResponseEntity<String> finalcandchangepass(Candidate candidate);
	//ResponseEntity<?> getAllanswer(Long candidateId);

	
	

	

}