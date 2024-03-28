package com.ccr.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long companyId;
	private String companyName;
	private String companyAddress;
	private Long companyPhoneNumber;
	private Long companyTan;
	private Boolean registrationApproval = false;


	
//	Prajwal Done this changes
//	@OneToMany(mappedBy = "userId")
//	@JsonIgnore
//	private Long userId;

	
	@OneToMany(mappedBy = "company")
	@JsonIgnore
	private List<Recruiter> recruiter;


	
}
