package com.ccr.mvp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.ccr.mvp.model.Category;
import com.ccr.mvp.model.Question;
import com.ccr.mvp.repository.CategoryRepository;

import com.ccr.mvp.exception.ResourceNotFoundException;
import com.ccr.mvp.model.Question;

import com.ccr.mvp.repository.QuestionRepository;
import com.ccr.mvp.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {
	@Autowired
	private QuestionRepository questionRepository;

	
//	@Override
//	public Question updateQuestionWeightage(Long id, Question question) {
//		Question updateQuestion = questionRepository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Question not exist with id: " + id));
//
//		updateQuestion.setQuestionContent(question.getQuestionContent());
//		updateQuestion.setQuestionWeightage(question.getQuestionWeightage());
//		updateQuestion = questionRepository.save(updateQuestion);
//
//		return updateQuestion;
//
//	}

}
