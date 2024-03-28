package com.ccr.mvp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;

import com.ccr.mvp.service.QuestionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.ccr.mvp.model.Question;

@CrossOrigin(origins = "*")
@RestController
public class QuestionController {
	@Autowired
	private QuestionService questionService;

	
//	@PutMapping("/updateWeightages/{id}")
//	public ResponseEntity<Question> updateQuestionWeightage(@PathVariable Long id, @RequestBody Question question) {
//		Question updateQuestion = questionService.updateQuestionWeightage(id, question);
//		return ResponseEntity.ok(updateQuestion);
//	}

}