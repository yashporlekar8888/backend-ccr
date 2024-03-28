package com.ccr.mvp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data

public class CategoryAverageScoreDTO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;
	private String categoryName;
	private double averageScore;

	public CategoryAverageScoreDTO(Long categoryId, String categoryName, double averageScore) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.averageScore = averageScore;
	}

}
