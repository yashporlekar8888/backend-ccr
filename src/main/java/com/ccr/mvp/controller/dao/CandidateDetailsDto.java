package com.ccr.mvp.controller.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDetailsDto {

	private long userId;
	private String candidateName;
	private Long candidateAadhar;
	private String candidateEmail;
	private Long phoneNumber;
	private Long candidateId;

}
