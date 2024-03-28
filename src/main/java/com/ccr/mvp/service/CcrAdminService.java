package com.ccr.mvp.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ccr.mvp.model.Recruiter;

import com.ccr.mvp.controller.dao.CompanyRecruiterDTO;
import com.ccr.mvp.model.Company;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.model.User;

public interface CcrAdminService {

	List<Recruiter> getCompanyRecruiter();

//	List<CompanyRecruiterDTO> getRegisteredCompaniesWithRecruiters();

	ResponseEntity<User> updateCompanyByAdmin(User user, String companyName, Long companyTan, String companyAddress,
			Long companyPhoneNumber);

	List<Recruiter> getCompanyRecruiter1();

	ResponseEntity<?> getAllCcrAdminList();
}
