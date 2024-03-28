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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;


@Entity
@Data
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long questionId;
	private String questionContent;
	private Double weightage;
	@Temporal(TemporalType.DATE)
	private Date createdAt;
	

	LocalDateTime updatedAt;


	@OneToMany(mappedBy = "question")
	@JsonIgnore
	private List<Answer> answerValue;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	
	
	

}