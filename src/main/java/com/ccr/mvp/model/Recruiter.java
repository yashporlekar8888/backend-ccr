package com.ccr.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Recruiter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recruiterId;
	private Long addedBy;
	private Long approver;
	private boolean addedPower;
	private boolean approvePower;
	
	
	@OneToMany(mappedBy = "recruiter")
	@JsonIgnore
	private List<Comment> comment;
	
	@OneToMany(mappedBy = "recruiter")
	@JsonIgnore
	private List<Answer> answer;
	
	@OneToMany(mappedBy = "recruiter")
	@JsonIgnore
	private List<CalculatedScore>calculatedScore;

	@ManyToOne
	@JoinColumn(name = "companyId")
	private Company company;
	
	
	@OneToOne
	@JoinColumn(name = "userId")
	private User user;

	

}