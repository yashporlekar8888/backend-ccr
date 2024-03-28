package com.ccr.mvp.service.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ccr.mvp.model.Answer;
import com.ccr.mvp.model.CalculatedScore;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.Category;
import com.ccr.mvp.model.Comment;
import com.ccr.mvp.model.Question;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.model.User;
import com.ccr.mvp.repository.AnswerRepository;
import com.ccr.mvp.repository.CalculatedScoreRepository;
import com.ccr.mvp.repository.CandidateRepository;
import com.ccr.mvp.repository.CommentRepository;
import com.ccr.mvp.repository.QuestionRepository;
import com.ccr.mvp.repository.RecruiterRepository;
import com.ccr.mvp.repository.UserRepository;
import com.ccr.mvp.service.AnswerService;

@Service
public class AnswerServiceImpl implements AnswerService {

	@Autowired
	private CandidateRepository candidateRepository;
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	AnswerRepository answerRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	CalculatedScoreRepository calculatedScoreRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RecruiterRepository recruiterRepository;

	@Override
	public ResponseEntity<?> submitAnswers(Long candidateId, Long userId, String jobRole, List<Answer> answers, String comment) {
		try {

			Recruiter recruiter = new Recruiter();
			Comment comment1 = new Comment();
			User user = new User();
			
			user.setUserId(userId);
			recruiter= recruiterRepository.findByUser(user);
			long recruiterId=recruiter.getRecruiterId();
			
			// Get the IST time zone
			ZoneId istZone = ZoneId.of("Asia/Kolkata");
			ZonedDateTime istDateTime = ZonedDateTime.now(istZone);
			// Convert the ZonedDateTime to a Date object
			Date interviewDate = Date.from(istDateTime.toInstant());

			Candidate candidate = candidateRepository.findById(candidateId)
					.orElseThrow(() -> new Exception("User not found with id: " + candidateId));
			

			
			for (Answer answer : answers) {
				Long questionId = answer.getQuestion().getQuestionId();
				Question question = questionRepository.findById(questionId)
						.orElseThrow(() -> new Exception("Question not found with id: " + questionId));
				Category categoryId = question.getCategory();

				answer.setQuestion(question);
				recruiter.setRecruiterId(recruiterId);
				answer.setRecruiter(recruiter);
				answer.setCandidate(candidate);
				answer.setCategory(categoryId);
			}
			answerRepository.saveAll(answers);


			comment1.setCommentContent(comment);
			comment1.setCommentApprove(false);
			comment1.setCommentSuggestion(false);
//			comment1.setCreatedAt(interviewDate);
			recruiter.setRecruiterId(recruiterId);
			comment1.setRecruiter(recruiter);
			comment1.setCandidate(candidate);
			commentRepository.save(comment1);
			
			

			int totalQuestions = questionRepository.findAll().size();
			int totalRecords = answers.size();
			double totalWeightage = 100.0;
			double totalWeightForYes = 0.0;
			double weightForNa = 0.0;
			for (Answer answer : answers) {
				if ("Yes".equals(answer.getAnswerResponse())) {
					totalWeightForYes += getQuestionWeightage(answer.getQuestion());
				} else if ("No".equals(answer.getAnswerResponse())) {
					continue;
				} else if ("NA".equals(answer.getAnswerResponse())) {
					weightForNa = getQuestionWeightage(answer.getQuestion());
					totalWeightage = totalWeightage - weightForNa;
					totalRecords--;
				}
			}
			double indivisualInterviewScore = totalRecords == 0 ? 0.0
					: (double) Math.round((100 * totalWeightForYes) / totalWeightage);

			CalculatedScore calculatedScore = new CalculatedScore();
			calculatedScore.setCandidate(candidate);
			recruiter.setRecruiterId(recruiterId);
			calculatedScore.setRecruiter(recruiter);
			calculatedScore.setInterviewScore(indivisualInterviewScore);
			calculatedScore.setJobRole(jobRole);
			calculatedScore.setInterviewDate(interviewDate);
			calculatedScore.setComment(comment1);
			calculatedScore.getInterviewId();

		



			calculatedScoreRepository.save(calculatedScore);
//			double b=	calculatedScore.getInterviewId();
//
//			calculatedScore.setInterviewScore(b);
//			calculatedScoreRepository.save(calculatedScore);



			return ResponseEntity.ok(calculatedScore);
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		}

	}

	// Common method for getting the weightage of a question by questionId (Long)
	private double getQuestionWeightage(Question question) {
		List<Question> questions = questionRepository.findByQuestionId(question.getQuestionId());
		double questionWeightage = 0.0;
		for (Question questionObj : questions) {
			questionWeightage = questionObj.getWeightage();
		}
		return questionWeightage;
	}

}
