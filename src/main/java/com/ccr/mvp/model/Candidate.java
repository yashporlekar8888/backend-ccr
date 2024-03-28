package com.ccr.mvp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Candidate {

	@Id
	@Column(name = "candidateId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long candidateId;
	private Long candidateAadhar;
	private String candidateDob;
	private Integer candidateOtp;
	private Date otpExpiration;
	private Double candidateAvgScore;
	
	
	@OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

	@OneToMany(mappedBy = "candidate")
	@JsonIgnore
	private List<Comment> comment;

	@OneToMany(mappedBy = "candidate")
	@JsonIgnore
	private List<Answer> answer;

	@OneToMany(mappedBy = "candidate")
	@JsonIgnore
	private List<CalculatedScore> calculatedScore;

	@OneToOne
	@JoinColumn(name = "userId")
	private User user;

	public void addAverageScore(CalculatedScore score) {
		if (calculatedScore == null) {
			calculatedScore = new ArrayList<>();
		}
		calculatedScore.add(score);
	}

}
