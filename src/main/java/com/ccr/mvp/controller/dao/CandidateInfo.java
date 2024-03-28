package com.ccr.mvp.controller.dao;

import com.ccr.mvp.model.User;

import lombok.Data;

//This dao class is required while searching candidate and get candidate Name and email to identify.
@Data
public class CandidateInfo {
	private String candidateName;
	private String candidateEmail;
	private Long candidateUserId;

	public CandidateInfo() {
	}
}
