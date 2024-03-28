package com.ccr.mvp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccr.mvp.model.Category;
import com.ccr.mvp.model.Question;
import com.ccr.mvp.service.CategoryService;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin(origins = "*")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

//	@PostMapping("/createCategory")
//	public Category createCategory(@RequestBody Category category) {
//		return categoryService.createCategory(category);
//	}
	@PostMapping("/createCategory")
	@PreAuthorize("hasRole('CCRADMIN')")
	public Category createCategory(@RequestBody Category category) {
		return categoryService.createCategory(category);
	}

	@PostMapping("/{categoryId}/addQuestion")
	@PreAuthorize("hasRole('CCRADMIN')")
    public ResponseEntity<String> addQuestionToCategory(@PathVariable Long categoryId,
            @RequestBody Question question) {
        Question addedQuestion = categoryService.addQuestionToCategory(categoryId, question);
        if (addedQuestion != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Question added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }
    }


	@GetMapping("/getAllCategories")
	@PreAuthorize("hasRole('CCRADMIN')")
	public List<Category> getAllCategories() {
		return categoryService.getAllCategories();
	}

	@DeleteMapping("removeCategory")
	@PreAuthorize("hasRole('CCRADMIN')")
	public ResponseEntity<Category> removeCategory(@RequestParam Long categoryId) {
		return categoryService.removeCategory(categoryId);
	}

	@DeleteMapping("removeQuestion")
	@PreAuthorize("hasRole('CCRADMIN')")
	public ResponseEntity<Question> removeQuestion(@RequestParam Long questionId) {

		return categoryService.removeQuestion(questionId);

	}

	@PutMapping("/updateCategories")
	@PreAuthorize("hasRole('CCRADMIN')")
	public ResponseEntity<List<Category>> updateCategories(@RequestBody List<Category> categories) {
		List<Category> updatedCategories = categoryService.updateCategories(categories);
		return ResponseEntity.ok(updatedCategories);
	}

//	@PutMapping("updateCategoryName")
//	public ResponseEntity<Category> updateCategoryName(@RequestParam Long categoryId,
//			@RequestParam String categoryName) {
//
//		return categoryService.updateCategoryName(categoryId, categoryName);
//
//	}

//	@PutMapping("updateQuestion")
//	public ResponseEntity<Question> updateQuestion(@RequestParam Long questionId,
//			@RequestParam String questionContent) {
//
//		return categoryService.updateQuestion(questionId, questionContent);
//
//	}

	@PutMapping("updateQuestion")
	@PreAuthorize("hasRole('CCRADMIN')")
	public ResponseEntity<Question> updateQuestion(@RequestParam Long questionId, @RequestParam String questionContent,
			@RequestParam Double weightage) {
		return categoryService.updateQuestion(questionId, questionContent, weightage);
	}

}
