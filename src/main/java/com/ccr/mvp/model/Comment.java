package com.ccr.mvp.model;

import java.time.LocalDateTime;
import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;


@Entity
@Data
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;
	private String commentContent;
	private String suggestionContent;
	private boolean commentApprove;
	private boolean commentSuggestion;
	@Temporal(TemporalType.DATE)
	private Date createdAt;

	LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name = "candidateId")
	private Candidate candidate;

	@ManyToOne
	@JoinColumn(name = "recruiterId")
	private Recruiter recruiter;
	
	@OneToOne(mappedBy = "comment")
	@JsonIgnore
	private CalculatedScore calculatedScore;
	
}