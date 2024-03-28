package com.ccr.mvp.model;

import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class CalculatedScore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long interviewId;
	private Double interviewScore;
	private String jobRole;
	private String hiringStatus;
	private String joiningStatus;
	private String notJoinedReason;

	@Temporal(TemporalType.DATE)
	private Date interviewDate;

	@Temporal(TemporalType.DATE)
	private Date joiningDate;

	@ManyToOne
	@JoinColumn(name = "candidateId")
	private Candidate candidate;

	@ManyToOne
	@JoinColumn(name = "recruiterId")
	private Recruiter recruiter;

	@OneToOne
	@JoinColumn(name = "commentId")
	private Comment comment;

}