package com.ccr.mvp.service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.ccr.mvp.controller.dao.CandidateDetailsDto;
import com.ccr.mvp.model.Candidate;
import org.springframework.data.domain.Page;

import com.ccr.mvp.model.Company;

import com.ccr.mvp.model.Question;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.model.User;
import org.springframework.data.domain.Page;
public interface RecruiterService {
	
	
	Recruiter hrAdminRegistration(Recruiter hrRecruiter);

	List<Question> getAllQuestion();
	
	ResponseEntity<?> getCandidateScoreRecruiter(Candidate candidate);

	List<Recruiter> getEmployeeHierarchy(Integer managerId);
	
	List<Recruiter> getEmployeeList(User user);

	Recruiter recruiterRegistration(Recruiter recruiter);

	ResponseEntity<?> updatePowers(Recruiter recruiter, long userId);

	ResponseEntity<?> changeApprover(User user, long userId);

	ResponseEntity<?> findRecruiterByEmail(User user);

	ResponseEntity<?> getUpdates(long userId);

	ResponseEntity<?> getNumberOfRecruiters();

	ResponseEntity<?> getNumberOfCandidates();

	ResponseEntity<?> getHrAdminDetails();

	ResponseEntity<?> getCandidateDetails();

	Page<ResponseEntity<CandidateDetailsDto>> findProductsWithPagination(int offset, int pageSize);

	Page<?> findAllWithPagination(Pageable paging);




	
	 
	

	
	
	

}
