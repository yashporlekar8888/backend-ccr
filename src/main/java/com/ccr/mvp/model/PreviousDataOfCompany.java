package com.ccr.mvp.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PreviousDataOfCompany {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long previousDataId;
	private String candidateName;
	private Date interviewDate;
	private String jobRole; 
	private String hiringStatus;
	private String joiningStatus;
	private String candidateEmailId;
	private long candidatePhoneNumber;
	
} 
