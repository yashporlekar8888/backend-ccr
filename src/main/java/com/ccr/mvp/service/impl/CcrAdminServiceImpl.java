package com.ccr.mvp.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ccr.mvp.controller.dao.CompanyRecruiterDTO;
import com.ccr.mvp.model.Company;
import com.ccr.mvp.model.Question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ccr.mvp.model.Role;
import com.ccr.mvp.model.User;
import org.springframework.stereotype.Service;


import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.model.User;
import com.ccr.mvp.repository.CompanyRepository;
import com.ccr.mvp.repository.RecruiterRepository;
import com.ccr.mvp.repository.UserRepository;
import com.ccr.mvp.service.CcrAdminService;

@Service
public class CcrAdminServiceImpl implements CcrAdminService {

	@Autowired
	CompanyRepository companyRepository;

	  @Autowired
	    private RecruiterRepository recruiterRepository;
	  @Autowired
	    private UserRepository userRepository;

	
	@Override
	public ResponseEntity<?> getAllCcrAdminList() {
		Role role = Role.ROLE_CCRADMIN;
		List<User> ccrAdminList= userRepository.findAllByRole(role);
		
		return ResponseEntity.ok(ccrAdminList);
	}
	
	@Override
	public List<Recruiter> getCompanyRecruiter() {

		User user = new User();

		List<Recruiter> companyrecruiterDetails = recruiterRepository.findAll();

		return companyrecruiterDetails;

	}

//	@Override
//	public List<CompanyRecruiterDTO> getCompanyRecruiter() {
//	    List<Recruiter> recruiters = recruiterRepository.findAll();
//
//	    List<CompanyRecruiterDTO> companyRecruiterDTOList = new ArrayList<>();
//
//	    for (Recruiter recruiter : recruiters) {
//	        CompanyRecruiterDTO dto = new CompanyRecruiterDTO();
//
//	        User user = recruiter.getUser();
//	        Company company = recruiter.getCompany();
//
//	        dto.setUserName(user.getUserName());
//	        dto.setPhoneNumber(user.getPhoneNumber());
//	        dto.setEmail(user.getEmail());
//
//	        dto.setCompanyId(company.getCompanyId());
//	        dto.setRecruiterId(recruiter.getRecruiterId());
//	        dto.setCompanyName(company.getCompanyName());
//	        dto.setCompanyAddress(company.getCompanyAddress());
//	        dto.setCompanyPhoneNumber(company.getCompanyPhoneNumber());
//	        dto.setCompanyTan(company.getCompanyTan());
//
//	        companyRecruiterDTOList.add(dto);
//	    }
//
//	    return companyRecruiterDTOList;
//	}
	
/*
	  @Override
	    public List<CompanyRecruiterDTO> getRegisteredCompaniesWithRecruiters() {
	        List<Company> companies = companyRepository.findAll();
	        List<CompanyRecruiterDTO> companyRecruiterList = new ArrayList<>();

	        for (Company company : companies) {
	            CompanyRecruiterDTO companyRecruiterDTO = new CompanyRecruiterDTO();
	            companyRecruiterDTO.setCompanyId(company.getCompanyId());
	            companyRecruiterDTO.setCompanyName(company.getCompanyName());
	            companyRecruiterDTO.setCompanyTan(company.getCompanyTan());
	            companyRecruiterDTO.setCompanyPhoneNumber(company.getCompanyPhoneNumber());
	            companyRecruiterDTO.setCompanyAddress(company.getCompanyAddress());
	            // Retrieve recruiters associated with the company
	            
	            List<Recruiter> recruiters = recruiterRepository.findByCompany(company)
	            		.stream()
	            		.filter(recruiter->recruiter.getAddedBy()==0)
	            		.collect(Collectors.toList());
	            companyRecruiterDTO.setRecruiters(recruiters);
	            
	            companyRecruiterList.add(companyRecruiterDTO);
	        }

	        return companyRecruiterList;
	    }
*/



	  public ResponseEntity<User> updateCompanyByAdmin(@RequestBody User user,
			  @RequestParam String companyName,@RequestParam Long companyTan,
			  @RequestParam String companyAddress,@RequestParam Long companyPhoneNumber) {
	      Optional<User> userOptional = userRepository.findById(user.getUserId());
	      if (userOptional.isPresent()) {
	          User updatedUser = userOptional.get();
	          updatedUser.setUserName(user.getUserName()); 
	          updatedUser.setEmail(user.getEmail());
	          updatedUser.setPhoneNumber(user.getPhoneNumber());
	          Recruiter userInRecruiter=updatedUser.getRecruiter();
	          Company companyOfRecruiter=userInRecruiter.getCompany();
	          companyOfRecruiter.setCompanyName(companyName);
	          companyOfRecruiter.setCompanyTan(companyTan);
	          companyOfRecruiter.setCompanyAddress(companyAddress);
	          companyOfRecruiter.setCompanyPhoneNumber(companyPhoneNumber);
	          
	          userRepository.save(updatedUser); 
	      }
	      return null; 
	  }



	  
		@Override
		public List<Recruiter> getCompanyRecruiter1() {

			User user = new User();

			List<Recruiter> companyrecruiterDetails = recruiterRepository.findAll().stream()
            		.filter(recruiter->recruiter.getAddedBy()==0)
            		.collect(Collectors.toList());

			return companyrecruiterDetails;

		}

}


