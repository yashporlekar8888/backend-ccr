package com.ccr.mvp.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ccr.mvp.model.Answer;
import com.ccr.mvp.model.Comment;

public interface AnswerService {
	
	ResponseEntity<?> submitAnswers(Long candidateId,Long userId, String jobRole, List<Answer> answers,String comment);
}
