package com.ccr.mvp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccr.mvp.model.CalculatedScore;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.Recruiter;

public interface CalculatedScoreRepository extends JpaRepository<CalculatedScore, Long>{

	List<CalculatedScore> findByCandidate(Candidate candidate);

	List<CalculatedScore> findByRecruiter(Recruiter recruiter);



	List<CalculatedScore> findByRecruiterRecruiterId(Long recruiterId);
	
    List<CalculatedScore> findByRecruiterAndJoiningDate(Recruiter recruiter, Date joiningDate);

	List<CalculatedScore> findByRecruiterAndJoiningDateAndJoiningStatusIsNull(Recruiter recruiter, Date parsedDate);
	


	

}
