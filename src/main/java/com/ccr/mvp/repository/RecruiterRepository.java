package com.ccr.mvp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccr.mvp.model.Company;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.model.User;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {

	Recruiter findByUser(User user);

	List<Recruiter> findByApprover(Long recruiterId);

	List<Recruiter> findByAddedBy(Long userId);

	List<Recruiter> findByCompany(Company company);

}
