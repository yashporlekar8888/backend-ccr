package com.ccr.mvp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ccr.mvp.model.CalculatedScore;
import com.ccr.mvp.model.User;
import com.ccr.mvp.repository.CalculatedScoreRepository;
import com.ccr.mvp.service.CalculatedScoreService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


@CrossOrigin(origins = "*")
@RestController
public class CalculatedScoreController {
	
	@Autowired
	private CalculatedScoreService calculatedScoreService;
	@Autowired
	CalculatedScoreRepository calculatedScoreRepository;

	@PostMapping("/getHistoryCandidate")
	@PreAuthorize("hasAnyRole('ROLE_HRADMIN','ROLE_RECRUITER','ROLE_CCRADMIN')")
	public ResponseEntity<?> getHistoryCandidate(@RequestBody User user) {
		return calculatedScoreService.getHistoryCandidate(user);
	}

	@PostMapping("/getRecruiterInterviews")
	@PreAuthorize("hasAnyRole('ROLE_HRADMIN','ROLE_RECRUITER','ROLE_CCRADMIN')")
	public ResponseEntity<?> getRecruiterInterviews(@RequestBody User user) {
		return calculatedScoreService.getRecruiterInterviews(user);
	}
//	@PostMapping("/getJobRolesByRecruiter")
//	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER','CCRADMIN')")
//	public ResponseEntity<?> getJobRolesByRecruiter(@RequestBody User user) {
//		return calculatedScoreService.getJobRolesByRecruiter(user);
//	}
//	
	@PostMapping("/getInterviewList")
	public ResponseEntity<?> getInterviewList(@RequestBody User user) {
		return calculatedScoreService.getInterviewList(user);
	}
	
	@PostMapping("/hiredNotHiredForm")
	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER','CCRADMIN')")
	public ResponseEntity<?>hiredNotHiredForm(@RequestBody CalculatedScore calculatedScore){
		return calculatedScoreService.hiredNotHiredForm(calculatedScore);
	}
	@PostMapping("/todaysJoiningList")
	public ResponseEntity<?> todaysJoiningList(@RequestBody User user) {
		return calculatedScoreService.todaysJoiningList(user);
	}
	
	

	@PostMapping("/getInReviewCandidates")
	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER')")
	public ResponseEntity<?> getInReviewCandidates(@RequestBody User user) {
		return calculatedScoreService.getInReviewCandidates(user);
	}
	@PostMapping("/joinedNotJoinedStatus")
	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER','CCRADMIN')")
	public ResponseEntity<?>joinedNotJoinedStatus(@RequestBody CalculatedScore calculatedScore){
		return calculatedScoreService.joinedNotJoinedStatus(calculatedScore);
	}
	@PutMapping("/changeJoiningDate")
	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER','CCRADMIN')")
	public ResponseEntity<?>changeJoiningDate(@RequestBody CalculatedScore calculatedScore){
		return calculatedScoreService.changeJoiningDate(calculatedScore);
	}
	
	

	
	
}
