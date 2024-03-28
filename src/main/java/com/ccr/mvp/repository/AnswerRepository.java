package com.ccr.mvp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccr.mvp.model.Answer;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.Category;
import com.ccr.mvp.model.Recruiter;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
	
	List<Answer> findByCandidate(Candidate candidate);
	
	List<Answer> findByCategory(Category category);
}
