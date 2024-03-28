package com.ccr.mvp.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ccr.mvp.model.Category;
import com.ccr.mvp.model.Question;

public interface CategoryService {

//	Category createCategory(Category category);
	Category createCategory(Category category);
	
	Question addQuestionToCategory(Long categoryId, Question question);

	List<Category> getAllCategories();

	ResponseEntity<Category> removeCategory(Long categoryId);

	ResponseEntity<Question> removeQuestion(Long questionId);

	List<Category> updateCategories(List<Category> categories);

//	ResponseEntity<Question> updateQuestion(Long questionId, String questionContent);

	ResponseEntity<Question> updateQuestion(Long questionId, String questionContent, Double weightage);

	

	

	

	


	
	

}
