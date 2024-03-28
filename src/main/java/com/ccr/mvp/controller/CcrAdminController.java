package com.ccr.mvp.controller;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccr.mvp.controller.dao.CompanyRecruiterDTO;
import com.ccr.mvp.controller.dao.RegisteredCompanyDetailsDto;
import com.ccr.mvp.model.Company;
import com.ccr.mvp.model.Question;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.model.User;
import com.ccr.mvp.service.CcrAdminService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.service.CcrAdminService;
import com.ccr.mvp.service.CompanyService;
import com.ccr.mvp.service.RecruiterService;

@CrossOrigin(origins = "*")
@RestController
public class CcrAdminController {

	@Autowired
	private CcrAdminService ccrAdminService;
	@Autowired
	private CompanyService companyService;
	
	@Autowired 
	private RecruiterService recruiterService;

//	@PostMapping("/registeredCompanies")
//	@PreAuthorize("hasAnyRole('CCRADMIN')")
//	public List<CompanyRecruiterDTO> getRegisteredCompaniesWithRecruiters() {
//
//		return ccrAdminService.getRegisteredCompaniesWithRecruiters();
//	}

	@PutMapping("updateCompanyByAdmin")
	@PreAuthorize("hasRole('CCRADMIN')")
	public ResponseEntity<User> updateCompanyByAdmin(@RequestBody User user, @RequestParam String companyName,
			@RequestParam Long companyTan, @RequestParam String companyAddress, @RequestParam Long companyPhoneNumber) {
		return ccrAdminService.updateCompanyByAdmin(user, companyName, companyTan, companyAddress, companyPhoneNumber);
	}

	// Yash API
	@GetMapping("/getCompanyRecruiter1")
	@PreAuthorize("hasRole('ROLE_CCRADMIN')")
	public List<Recruiter> getCompanyRecruiter1() {
		return ccrAdminService.getCompanyRecruiter1();
	}

	@GetMapping("/getCompanyRecruiter")
	@PreAuthorize("hasRole('ROLE_CCRADMIN')")
	public List<Recruiter> getCompanyRecruiter() {
		return ccrAdminService.getCompanyRecruiter();
	}

	@PutMapping("/approveRegistration")
	@PreAuthorize("hasRole('ROLE_CCRADMIN')")
	public ResponseEntity<String> approveRegistration(@RequestParam Long companyId) {
		companyService.approveRegistration(companyId);
		return ResponseEntity.ok("Registration Approved");
	}

	@PutMapping("/rejectRegistration")
	@PreAuthorize("hasRole('ROLE_CCRADMIN')")
	public ResponseEntity<String> recjectRegistration(@RequestParam Long companyId) {
		companyService.rejectRegistration(companyId);
		return ResponseEntity.ok("Registration Rejected");
	}
	
	@GetMapping("/getNumberOfRegisteredCompanies")
	@PreAuthorize("hasRole('ROLE_CCRADMIN')")
	public ResponseEntity<?> getNumberOfRegisteredCompanies(){
		return companyService.getNumberOfRegisteredCompanies();
		
	}
	
	@GetMapping("/getNumberOfRecruiters")
	@PreAuthorize("hasRole('ROLE_CCRADMIN')")
	public ResponseEntity<?> getNumberOfRecruiters(){
		return recruiterService.getNumberOfRecruiters();
	}
	
	/*@GetMapping("/getNumberOfHrAdmins")
	@PreAuthorize("hasRole('ROLE_CCRADMIN')")
	public ResponseEntity<?> getNumberOfHrAdmins(){
		return recruiterService.getNumberOfHrAdmins();
	}*/
	
	@GetMapping("/getNumberOfCandidates")
	@PreAuthorize("hasAnyRole('ROLE_CCRADMIN')")
	public ResponseEntity<?> getNumberOfCandidates(){
		return recruiterService.getNumberOfCandidates();
	}
	
	@GetMapping("/getHrAdminDetails")
	@PreAuthorize("hasRole('ROLE_CCRADMIN')")
	public ResponseEntity<?> getHrAdminDetails(){
		return recruiterService.getHrAdminDetails();
	}
	
	@GetMapping("/getCandidateDetails")
	@PreAuthorize("hasAnyRole('ROLE_CCRADMIN','ROLE_HRADMIN','ROLE_RECRUITER')")
	public ResponseEntity<?> getCandidateDetails(){
		return recruiterService.getCandidateDetails();
	}

}
