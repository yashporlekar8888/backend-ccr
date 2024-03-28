package com.ccr.mvp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ccr.mvp.controller.dao.RegisteredCompanyDetailsDto;
import com.ccr.mvp.model.Company;
import com.ccr.mvp.repository.CompanyRepository;
import com.ccr.mvp.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
	@Autowired
	CompanyRepository companyRepository;

	@Override
	public Company companyRegistration(Company company) {
		companyRepository.save(company);
		return company;
	}

	@Override
	public void approveRegistration(Long companyId) {
		companyRepository.findById(companyId).ifPresent(company -> {
			company.setRegistrationApproval(true);
			companyRepository.save(company);
		});
	}

	@Override
	public void rejectRegistration(Long companyId) {
		companyRepository.findById(companyId).ifPresent(company -> {
			company.setRegistrationApproval(false);
			companyRepository.save(company);
		});
	}

//	@Override
//	public ResponseEntity<RegisteredCompanyDetailsDto> getNumberOfRegisteredCompanies() {
//		List<Company> companyDetails = companyRepository.findAll();
//		RegisteredCompanyDetailsDto registeredCompanyDetailsDto = new RegisteredCompanyDetailsDto();
//		double count = 0;
//		for(Company companyDetail : companyDetails) {
//			if(companyDetail.getRegistrationApproval() == true) {
//				count++;
//				registeredCompanyDetailsDto.setCompanyId(companyDetail.getCompanyId());
//				registeredCompanyDetailsDto.setCompanyName(companyDetail.getCompanyName());
//				registeredCompanyDetailsDto.setApprovalStatus(true);
//			}
//		}
//		return ResponseEntity.ok(registeredCompanyDetailsDto);
//	}

	@Override
	public ResponseEntity<?> getNumberOfRegisteredCompanies() {
		List<Company> companyDetails = companyRepository.findAll();
		List<RegisteredCompanyDetailsDto> registeredCompanies = new ArrayList<>();

		/*for (Company companyDetail : companyDetails) {
			if (companyDetail.getRegistrationApproval()) {
				RegisteredCompanyDetailsDto registeredCompanyDetailsDto = new RegisteredCompanyDetailsDto(
						companyDetail.getCompanyId(), companyDetail.getCompanyName(), true);

				registeredCompanies.add(registeredCompanyDetailsDto);
			}
		}*/

		return ResponseEntity.ok(companyDetails);
	}

}
