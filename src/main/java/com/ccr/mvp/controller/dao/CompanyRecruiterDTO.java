package com.ccr.mvp.controller.dao;

import java.util.List;

import com.ccr.mvp.model.Recruiter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class CompanyRecruiterDTO {
	
	
	 private Long companyId;
	    private String companyName;
	    private String  companyAddress;
	    private Long companyPhoneNumber;
	    private Long companyTan;
	    private List<Recruiter> recruiters;

	    
	   
}
