package com.ccr.mvp.service.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ccr.mvp.model.Category;
import com.ccr.mvp.model.Question;
import com.ccr.mvp.repository.CategoryRepository;
import com.ccr.mvp.repository.QuestionRepository;
import com.ccr.mvp.service.CategoryService;

import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private QuestionRepository questionRepository;

	ZoneId istZone = ZoneId.of("Asia/Kolkata");
	ZonedDateTime istDateTime = ZonedDateTime.now(istZone);
	// Convert the ZonedDateTime to a Date object
	Date D = Date.from(istDateTime.toInstant());

	@Override
	public Category createCategory(Category category) {

		String categoryName = category.getCategoryName();
		category.setCategoryName(categoryName);
		Double newCategoryWeightage = 0.0;
		category.setCategoryWeightage(newCategoryWeightage);
		category.setCreatedAt(D);

		categoryRepository.save(category);

		return category;
	}

	@Override
	public List<Category> getAllCategories() {
		List<Category> category = categoryRepository.findAll();
		return category;
	}

	@Override
	public Question addQuestionToCategory(Long categoryId, Question question) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

		if (optionalCategory.isPresent()) {
			Category category = optionalCategory.get();

			// Create and save the new question

			question.setQuestionContent(question.getQuestionContent());
			question.setCategory(category);
			question.setCreatedAt(D);
			questionRepository.save(question);

			// Update category and question weightage
			updateQuestionWeightageForCategory(category);

			return question;
		}
		return null;
	}

	// new code add question soham

//	 private void updateQuestionWeightageForCategory(Category categoryToUpdate) {
//	        List<Question> questionsInCategory = categoryToUpdate.getQuestions();
//	        int numberOfQuestions = questionsInCategory.size();
//
//	        if (numberOfQuestions > 0) {
//	            double totalWeightage = categoryToUpdate.getCategoryWeightage();
//	            double individualWeightage = totalWeightage / numberOfQuestions;
//	            double roundOffQuestionWeightage = Math.round(individualWeightage * 100.0) / 100.0;
//
//	            for (Question q : questionsInCategory) {
//	                q.setWeightage(roundOffQuestionWeightage);
//	            }
//	            questionRepository.saveAll(questionsInCategory);
//	        }
//	    }

	///////////////////////////////////////////////// newww chat gpt
	private void updateQuestionWeightageForCategory(Category categoryToUpdate) {
		List<Question> questionsInCategory = categoryToUpdate.getQuestions();
		int numberOfQuestions = questionsInCategory.size();

		if (numberOfQuestions > 0) {
			double totalWeightage = categoryToUpdate.getCategoryWeightage();
			double defaultWeightage = totalWeightage / numberOfQuestions;
			double remainingWeightage = totalWeightage;

			for (Question q : questionsInCategory) {
				// Check if the question has a custom weightage set
				if (q.getWeightage() != null) {
					remainingWeightage -= q.getWeightage();
				}
			}

			double roundOffDefaultWeightage = Math.round(defaultWeightage * 100.0) / 100.0;

			for (Question q : questionsInCategory) {
				// Check if the question has a custom weightage set
				if (q.getWeightage() == null) {
					if (remainingWeightage >= roundOffDefaultWeightage) {
						q.setWeightage(roundOffDefaultWeightage);
						remainingWeightage -= roundOffDefaultWeightage;
					} else {
						q.setWeightage(remainingWeightage);
					}
				}
			}
			questionRepository.saveAll(questionsInCategory);
		}
	}

	// yash code used while delete category
	private void updateCategoryAndQuestionWeightage() {

		List<Category> categories = categoryRepository.findAll();
		double totalWeightage = 100.0;
		double individualWeightage = totalWeightage / categories.size();
		double roundOffCategoryWeightage = Math.round(individualWeightage * 100.0) / 100.0;

		for (Category existingCategory : categories) {
			existingCategory.setCategoryWeightage(roundOffCategoryWeightage);
			categoryRepository.save(existingCategory);

			// Calculate and set question weightage based on the number of questions in the
			// category
			List<Question> questionsInCategory = existingCategory.getQuestions();
			double questionWeightage = individualWeightage / questionsInCategory.size();
			/// to calculate value of question weightage upto 2decimal places
			double roundOffQuestionWeightage = Math.round(questionWeightage * 100.0) / 100.0;

			for (Question q : questionsInCategory) {
				q.setWeightage(roundOffQuestionWeightage);
			}
			questionRepository.saveAll(questionsInCategory);
		}
	}

	@Override
	public ResponseEntity<Category> removeCategory(Long categoryId) {
		Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

		if (categoryOptional.isPresent()) {
			Category category = categoryOptional.get();
			categoryRepository.deleteById(categoryId);
			// updateCategoryAndQuestionWeightage();
			return ResponseEntity.ok(category);
		} else {

			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public ResponseEntity<Question> removeQuestion(Long questionId) {
		Optional<Question> questionOptional = questionRepository.findById(questionId);

		if (questionOptional.isPresent()) {
			Question question = questionOptional.get();
			Category category = question.getCategory();

			questionRepository.deleteById(questionId);

			// Update question weightage for the category of the removed question
			updateQuestionWeightageForCategory(category);

			return ResponseEntity.ok(question);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public List<Category> updateCategories(List<Category> updatedCategories) {

		double totalWeightage = updatedCategories.stream().mapToDouble(Category::getCategoryWeightage).sum();

		if (totalWeightage != 100.0) {
			throw new RuntimeException("The total category weightage must be equal to 100.");
		}

		// Loop through the list of updated categories and update the categoryWeightage
		// in the database
		for (Category updatedCategory : updatedCategories) {
			Long categoryId = updatedCategory.getCategoryId();
			Double updatedWeightage = updatedCategory.getCategoryWeightage();

			Category existingCategory = categoryRepository.findById(categoryId).orElse(null);

			if (existingCategory != null) {
				// Update the categoryWeightage
				existingCategory.setCategoryWeightage(updatedWeightage);
				categoryRepository.save(existingCategory);
			}
		}

		return updatedCategories;
	}

//	 @Override
//	    public ResponseEntity<Question> updateQuestion(Long questionId, String questionContent) {
//	        Optional<Question> questionOptional = questionRepository.findById(questionId);
//	        
//	        if (questionOptional.isPresent()) {
//	            Question question = questionOptional.get();
//	            question.setQuestionContent(questionContent);
//	            questionRepository.save(question);
//	            return ResponseEntity.ok(question);
//	        } else {
//	            return ResponseEntity.notFound().build();
//	        }
//	    }

	@Override
	public ResponseEntity<Question> updateQuestion(Long questionId, String questionContent, Double weightage) {
		Optional<Question> questionOptional = questionRepository.findById(questionId);

		if (questionOptional.isPresent()) {
			Question question = questionOptional.get();
			question.setQuestionContent(questionContent);
			question.setWeightage(weightage);
			questionRepository.save(question);
			return ResponseEntity.ok(question);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
