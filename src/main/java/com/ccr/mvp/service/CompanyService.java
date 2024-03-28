package com.ccr.mvp.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ccr.mvp.controller.dao.RegisteredCompanyDetailsDto;
import com.ccr.mvp.model.Company;

public interface CompanyService {

	Company companyRegistration(Company company);
	
	void approveRegistration(Long companyId);

	void rejectRegistration(Long companyId);

	ResponseEntity<?> getNumberOfRegisteredCompanies();

}
