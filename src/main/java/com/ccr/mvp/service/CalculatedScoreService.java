package com.ccr.mvp.service;


import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.ccr.mvp.model.CalculatedScore;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.model.User;

public interface CalculatedScoreService {

	ResponseEntity<?> getHistoryCandidate(User user);

	ResponseEntity<?> getRecruiterInterviews(User user);

//	ResponseEntity<?> getJobRolesByRecruiter(User user);
	
	ResponseEntity<?> getInterviewList(User user);

	ResponseEntity<?> hiredNotHiredForm(CalculatedScore calculatedScore);

	ResponseEntity<?> getInReviewCandidates(User user);

	ResponseEntity<?> todaysJoiningList(User user);

	ResponseEntity<?> joinedNotJoinedStatus(CalculatedScore calculatedScore);

	ResponseEntity<?> changeJoiningDate(CalculatedScore calculatedScore);
	
   




}
