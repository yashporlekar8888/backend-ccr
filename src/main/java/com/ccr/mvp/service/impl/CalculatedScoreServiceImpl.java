package com.ccr.mvp.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ccr.mvp.model.CalculatedScore;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.model.User;
import com.ccr.mvp.repository.CalculatedScoreRepository;
import com.ccr.mvp.repository.CandidateRepository;
import com.ccr.mvp.repository.CommentRepository;
import com.ccr.mvp.repository.RecruiterRepository;
import com.ccr.mvp.repository.UserRepository;
import com.ccr.mvp.service.CalculatedScoreService;

@Service
public class CalculatedScoreServiceImpl implements CalculatedScoreService {

	@Autowired
	CalculatedScoreRepository calculatedScoreRepository;
	@Autowired
	CandidateRepository candidateRepository;
	@Autowired
	CommentRepository commentRepository;

	@Autowired
	RecruiterRepository recruiterRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public ResponseEntity<?> getHistoryCandidate(User user) {
		try {
			Candidate candidate = new Candidate();
			candidate = candidateRepository.findByUser(user);
			candidate = candidateRepository.findById(candidate.getCandidateId())
					.orElseThrow(() -> new Exception("User not found with id: "));

			List<CalculatedScore> details = calculatedScoreRepository.findByCandidate(candidate);

			return ResponseEntity.ok(details);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		}

	}

	@Override
	public ResponseEntity<?> getRecruiterInterviews(User user) {
		try {
			Recruiter recruiter = new Recruiter();
			recruiter = recruiterRepository.findByUser(user);
			recruiter = recruiterRepository.findById(recruiter.getRecruiterId())
					.orElseThrow(() -> new Exception("User not found with id: "));

			List<Recruiter> addedByList = new ArrayList<>();
			addedByList = recruiterRepository.findByAddedBy(user.getUserId());

			List<CalculatedScore> details = new ArrayList<>();
			for (Recruiter rec : addedByList) {
				details.addAll(calculatedScoreRepository.findByRecruiter(rec));
			}

			details.addAll(calculatedScoreRepository.findByRecruiter(recruiter));




			return ResponseEntity.ok(details);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		}

	}

//	@Override
//	public ResponseEntity<?> getJobRolesByRecruiter(User user) {
//	    try {
//	        Recruiter recruiter = recruiterRepository.findByUser(user);
//
//	        if (recruiter == null) {
//	            throw new Exception("Recruiter not found for the given user");
//	        }
//
//	        Long recruiterId = recruiter.getRecruiterId();
//
//	        List<CalculatedScore> jobRoles = calculatedScoreRepository.findByRecruiterRecruiterId(recruiterId);
//
//	        int jobRolesCount = jobRoles.size();
//
//	        Map<String, Integer> jobRolesCountMap = new HashMap<>();
//	        for (CalculatedScore score : jobRoles) {
//	            String jobRole = score.getJobRole();
//	            jobRolesCountMap.put(jobRole, jobRolesCountMap.getOrDefault(jobRole, 0) + 1);
//	        }
//
//	        Map<String, Object> response = new HashMap<>();
//	        response.put("jobRoles", jobRoles);
//	        response.put("jobRolesCount", jobRolesCount);
//	        response.put("jobRolesCountMap", jobRolesCountMap);
//
//	        return ResponseEntity.ok(response);
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something went wrong: " + e.getMessage());
//	    }
//	}

	@Override
	public ResponseEntity<?> hiredNotHiredForm(CalculatedScore calculatedScore1) {
		try {
			CalculatedScore calculatedScore = calculatedScoreRepository.findById(calculatedScore1.getInterviewId())
					.orElseThrow(() -> new Exception("User not found with id: "));

			calculatedScore.setHiringStatus(calculatedScore1.getHiringStatus());

			if ("NOT HIRED".equals(calculatedScore.getHiringStatus())) {
				calculatedScore.setJoiningDate(null);
				calculatedScore.setJoiningStatus("NA");
				calculatedScore.setNotJoinedReason("NA");
			}
			if ("HIRED".equals(calculatedScore.getHiringStatus())) {

				calculatedScore.setJoiningDate(calculatedScore1.getJoiningDate());
				calculatedScore.setJoiningStatus("Not Assigned Yet");
			}
			calculatedScoreRepository.save(calculatedScore);

			return ResponseEntity.ok(calculatedScore);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
		}
	}

	@Override
	public ResponseEntity<?> getInterviewList(User user) {
		try {
			Recruiter recruiter = new Recruiter();
			recruiter = recruiterRepository.findByUser(user);
			recruiter = recruiterRepository.findById(recruiter.getRecruiterId())
					.orElseThrow(() -> new Exception("User not found with id: "));
			List<CalculatedScore> interviewList = calculatedScoreRepository.findByRecruiter(recruiter);

			return ResponseEntity.ok(interviewList);
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public ResponseEntity<?> getInReviewCandidates(User user) {
		try {
			Recruiter recruiter = recruiterRepository.findByUser(user);
			recruiter = recruiterRepository.findById(recruiter.getRecruiterId())
					.orElseThrow(() -> new Exception("User not found with id: "));

			List<CalculatedScore> allCandidates = calculatedScoreRepository.findByRecruiter(recruiter);

			List<CalculatedScore> inReviewCandidates = allCandidates.stream()
					.filter(candidate -> "IN REVIEW".equals(candidate.getHiringStatus())).collect(Collectors.toList());

			return ResponseEntity.ok(inReviewCandidates);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
		}
	}

	@Override
	public ResponseEntity<List<CalculatedScore>> todaysJoiningList(User user) {
	    try {
	        Recruiter recruiter = recruiterRepository.findByUser(user);
	        recruiter = recruiterRepository.findById(recruiter.getRecruiterId())
	                .orElseThrow(() -> new Exception("User not found with id: "));

	        Date today = new Date();
	        List<CalculatedScore> todaysJoiningList = calculatedScoreRepository.findByRecruiterAndJoiningDateAndJoiningStatusIsNull(recruiter, today);

	        return ResponseEntity.ok(todaysJoiningList);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
//	@Override
//	public ResponseEntity<List<CalculatedScore>> todaysJoiningList(User user) {
//	    try {
//	        Recruiter recruiter = recruiterRepository.findByUser(user);
//	        recruiter = recruiterRepository.findById(recruiter.getRecruiterId())
//	                .orElseThrow(() -> new Exception("User not found with id: "));
//
//	        Date today = new Date();
//
//	        List<CalculatedScore> todaysJoiningList = calculatedScoreRepository.findByRecruiterAndJoiningDate(recruiter, today);
//
//	        return ResponseEntity.ok(todaysJoiningList);
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	    }
//	}

	@Override
	public ResponseEntity<?> joinedNotJoinedStatus(CalculatedScore calculatedScore1) {
		  try {
		        CalculatedScore calculatedScore = calculatedScoreRepository.findById(calculatedScore1.getInterviewId())
		                .orElseThrow(() -> new Exception("User not found with id: "));

		        calculatedScore.setJoiningStatus(calculatedScore1.getJoiningStatus());



		       if ("NOT JOINED".equals(calculatedScore.getJoiningStatus())) {

			            calculatedScore.setNotJoinedReason(calculatedScore1.getNotJoinedReason());

			        }
		        calculatedScoreRepository.save(calculatedScore);

		        return ResponseEntity.ok(calculatedScore);
		    } catch (Exception e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
		    }
	}


	@Override
	public ResponseEntity<?> changeJoiningDate(CalculatedScore calculatedScore1) {
		 try {
		        CalculatedScore calculatedScore = calculatedScoreRepository.findById(calculatedScore1.getInterviewId())
		                .orElseThrow(() -> new Exception("User not found with id: "));


		        calculatedScore.setJoiningDate(calculatedScore1.getJoiningDate());
		        calculatedScoreRepository.save(calculatedScore);


		 }
		 catch (Exception e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
		    }
		 
		return null;
	}

	
	


}