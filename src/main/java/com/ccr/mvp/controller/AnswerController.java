package com.ccr.mvp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccr.mvp.model.Answer;
import com.ccr.mvp.model.Comment;
import com.ccr.mvp.service.AnswerService;

@CrossOrigin(origins = "*")
@RestController
public class AnswerController {
	
	@Autowired
	private AnswerService answerService;
	
	@PostMapping("/submitAnswers")
	public ResponseEntity<?> submitAnswers(@RequestParam Long candidateId,@RequestParam Long userId, 
			@RequestParam String jobRole,@RequestBody List<Answer> answers,String comment)
			throws Exception {
		System.out.println("hello sonarqube");

		return answerService.submitAnswers(candidateId,userId,jobRole,answers,comment);
	}
}
