package com.ccr.mvp.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.User;


public interface CandidateRepository  extends JpaRepository<Candidate, Long>{
	
	ResponseEntity<Candidate> findByCandidateAadhar(Long candidateAadhar);
	
	Candidate findCandidateByCandidateAadhar(Long candidateAadhar);
	
	Candidate findByUser(User user);
	


}
