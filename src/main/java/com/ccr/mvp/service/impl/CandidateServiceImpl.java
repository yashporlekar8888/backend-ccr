package com.ccr.mvp.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ccr.mvp.controller.dao.CandidateInfo;
import com.ccr.mvp.model.Answer;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.Category;
import com.ccr.mvp.model.CategoryAverageScoreDTO;
import com.ccr.mvp.model.Question;
import com.ccr.mvp.model.User;
import com.ccr.mvp.repository.AnswerRepository;
import com.ccr.mvp.repository.CalculatedScoreRepository;
import com.ccr.mvp.repository.CandidateRepository;
import com.ccr.mvp.repository.CategoryRepository;
import com.ccr.mvp.repository.QuestionRepository;
import com.ccr.mvp.repository.UserRepository;
import com.ccr.mvp.service.CandidateService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class CandidateServiceImpl implements CandidateService {
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	AnswerRepository answerRepository;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	CalculatedScoreRepository calculatedScoreRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	// @Override
	public Candidate candidateRegistration(Candidate candidate) {

		candidateRepository.save(candidate);
		return candidate;
		// return ResponseEntity.status(HttpStatus.CREATED).body("Candidate Registered
		// sucessfully");
	}


	@Override
	public ResponseEntity<?> candidateListByAadhar(Long candidate_aadhar) {
		try {

			Session session = entityManager.unwrap(Session.class);

			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Object[]> cr = cb.createQuery(Object[].class);
			Root<Candidate> root = cr.from(Candidate.class);

			Candidate candidate = candidateRepository.findCandidateByCandidateAadhar(candidate_aadhar);
			User user = candidate.getUser();
			User userData = userRepository.findByUserId(user.getUserId());

			String candidateEmail = userData.getEmail();
			String candidateName = userData.getUserName();
			session.close();
			// Create a DTO or response object to hold the selected data
			CandidateInfo candidateInfo = new CandidateInfo();
			candidateInfo.setCandidateEmail(candidateEmail);
			candidateInfo.setCandidateName(candidateName);
			candidateInfo.setCandidateUserId(user.getUserId());
			return new ResponseEntity<>(candidateInfo, HttpStatus.OK);

		} catch (Exception E) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		}
	}

	@Override
	public ResponseEntity<?> openCandidateprofileByAadhar(Long candidate_aadhar) {

		Session session = entityManager.unwrap(Session.class);
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Candidate> cr = cb.createQuery(Candidate.class);
		Root<Candidate> root = cr.from(Candidate.class);
		cr.select(root).where(cb.equal(root.get("candidateAadhar"), candidate_aadhar));
		Query<Candidate> query = session.createQuery(cr);

		Candidate result = query.getSingleResult();

		session.close();

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// Get Candidate's total Avg Score
	@Override
	public ResponseEntity<?> getCandidateAverageScore(User user) {
		try {

			/*
			 * candidate = candidateRepository.findById(candidate.getCandidateId())
			 * .orElseThrow(() -> new Exception("Candidate not found with id: "));
			 * 
			 * double avgscore = candidate.getCandidateAvgScore(); return
			 * ResponseEntity.ok(avgscore);
			 */
			Candidate candidate = new Candidate();
			candidate= candidateRepository.findByUser(user);
//			long recruiterId=recruiter.getRecruiterId();
			
			candidate = candidateRepository.findById(candidate.getCandidateId())
					.orElseThrow(() -> new Exception("Candidate not found with id: "));

			List<Answer> answers = answerRepository.findByCandidate(candidate);
			Map<Category, Double> averageScoresByCategory = new HashMap<>();
			List<CategoryAverageScoreDTO> result = new ArrayList<>();

			double AvgScore = 0.0;
			for (Answer ans : answers) {
				List<Answer> answersInCategory = answers.stream().filter(
						answer -> answer.getCategory() != null && answer.getCategory().equals(ans.getCategory()))
						.collect(Collectors.toList());
				double totalWeightedScore1 = 0.0;

				double categoryScore = ans.getCategory().getCategoryWeightage();
				double weightForNa = 0.0;

				int totalRecords1 = answersInCategory.size();
				Category category = ans.getCategory();

				List<Question> questionsInCategory = questionRepository.findByCategory(category);

				int totalQuestions1 = questionsInCategory.size();

				for (Answer answerInCat : answersInCategory) {
					if ("Yes".equals(answerInCat.getAnswerResponse())) {
						totalWeightedScore1 += getQuestionWeightage(answerInCat.getQuestion());
					} else if ("No".equals(answerInCat.getAnswerResponse())) {
						continue;
					} else if ("NA".equals(answerInCat.getAnswerResponse())) {
						weightForNa += getQuestionWeightage(answerInCat.getQuestion());

					}
				}


				//previous code
				//double z = totalRecords1 / totalQuestions1;
				
				//solution given by sonarqube
				double z = (double) totalRecords1 / totalQuestions1;


				// Calculate the average score
				double a = categoryScore * z;
				a -= weightForNa;

				// double a = categoryScore / totalQuestions1 * totalRecords1;
				double CateAvgScore = Math.round((100 * totalWeightedScore1) / a);

				averageScoresByCategory.put(ans.getCategory(), CateAvgScore);

			}

			// Loop through the map entries and add them to the list
			for (Map.Entry<Category, Double> entry : averageScoresByCategory.entrySet()) {

				Double averageScorecate = entry.getValue();
				AvgScore = AvgScore + averageScorecate;

			}

			int CategorySize = (int) categoryRepository.count();

			double AvgScore1 = Math.round(AvgScore / CategorySize);

			return ResponseEntity.ok(AvgScore1);

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		}
	}

//	Get avg score for a candidate category wise
	@Override
	public ResponseEntity<?> getCandidateAverageScoreCategory(User user) {
		try {
			
			Candidate candidate = new Candidate();
			candidate= candidateRepository.findByUser(user);
			
			candidate = candidateRepository.findById(candidate.getCandidateId())
					.orElseThrow(() -> new Exception("Candidate not found with id: "));

			List<Answer> answers = answerRepository.findByCandidate(candidate);
			Map<Category, Double> averageScoresByCategory = new HashMap<>();
			List<CategoryAverageScoreDTO> result = new ArrayList<>();

			for (Answer ans : answers) {
				List<Answer> answersInCategory = answers.stream().filter(
						answer -> answer.getCategory() != null && answer.getCategory().equals(ans.getCategory()))
						.collect(Collectors.toList());
				double totalWeightedScore1 = 0.0;
				double totalScore = 100.0;
				double categoryScore = ans.getCategory().getCategoryWeightage();
				double weightForNa = 0.0;
				double questionWeitage = ans.getQuestion().getWeightage();
				int totalRecords1 = answersInCategory.size();
				Category category = ans.getCategory();

				List<Question> questionsInCategory = questionRepository.findByCategory(category);

				int totalQuestions1 = questionsInCategory.size();

				for (Answer answerInCat : answersInCategory) {
					if ("Yes".equals(answerInCat.getAnswerResponse())) {
						totalWeightedScore1 += getQuestionWeightage(answerInCat.getQuestion());
					} else if ("No".equals(answerInCat.getAnswerResponse())) {
						continue;
					} else if ("NA".equals(answerInCat.getAnswerResponse())) {
						weightForNa += getQuestionWeightage(answerInCat.getQuestion());
						// totalRecords1--;
					}
				}

//				double z = totalRecords1 / totalQuestions1;
				//sonarqube suggestion
				double z = (double) totalRecords1 / totalQuestions1;

				
				// Calculate the average score
				double a = categoryScore * z;
				a -= weightForNa;

				// double a = categoryScore / totalQuestions1 * totalRecords1;
				double CateAvgScore = Math.round((100 * totalWeightedScore1) / a);

				averageScoresByCategory.put(ans.getCategory(), CateAvgScore);
				result.add(new CategoryAverageScoreDTO(ans.getCategory().getCategoryId(),
						ans.getCategory().getCategoryName(), CateAvgScore));
			}
			List<CategoryAverageScoreDTO> responseDTOList = new ArrayList<>();
			// Loop through the map entries and add them to the list
			for (Map.Entry<Category, Double> entry : averageScoresByCategory.entrySet()) {
				long categoryId = entry.getKey().getCategoryId();
				String categoryName = entry.getKey().getCategoryName();
				Double averageScorecate = entry.getValue();
				responseDTOList.add(new CategoryAverageScoreDTO(categoryId, categoryName, averageScorecate));
			}
			return ResponseEntity.ok(responseDTOList);

		} catch (Exception e) {
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

	public Double getCategoryWeightage(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
		return category.getCategoryWeightage();
	}


	@Override
	public ResponseEntity<?> getAllCandidateList() {
		List<Candidate> allCandidateList = candidateRepository.findAll();
		return ResponseEntity.ok(allCandidateList);
	}

	// OTP Forgot password
//
//	private Date calculateExpirationTime() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.MINUTE, 3); // Add 1 minutes to the current time
//		return calendar.getTime();
//	}
//
//	public ResponseEntity<String> sendOtpByEmail(Candidate candidate) {
////			System.out.println(candidate.getCandidateEmail());
//
//		Session session = entityManager.unwrap(Session.class);
//
//		CriteriaBuilder cb = session.getCriteriaBuilder();
//		CriteriaQuery<Candidate> cr = cb.createQuery(Candidate.class);
//
//		Root<Candidate> root = cr.from(Candidate.class);
//		// cr.select(root).where(cb.equal(root.get("candidateEmail"),
//		// candidate.getCandidateEmail()));
//
//		Query query = session.createQuery(cr);
//
//		Candidate retrievedCandidate = (Candidate) query.getSingleResult();
//
//		if (retrievedCandidate != null) {
//			int otp = generateOtp();
//			// int storingotp=candidate.getCandidate_otp();
//			// System.out.println(storingotp);
//			Date expirationTime = calculateExpirationTime();
//
//			retrievedCandidate.setCandidateOtp(otp);
//			retrievedCandidate.setOtpExpiration(expirationTime);
//
//			candidateRepository.save(retrievedCandidate);
//			/*
//			 * String i = retrievedCandidate.getCandidateEmail();
//			 * 
//			 * try { sendOtpEmail(i, otp); } catch (UnsupportedEncodingException e) { //
//			 * TODO Auto-generated catch block e.printStackTrace(); } catch
//			 * (MessagingException e) { // TODO Auto-generated catch block
//			 * e.printStackTrace(); }
//			 */
//			session.close();
//
//			return ResponseEntity.ok("OTP sent successfully");
//
//		}
//
//		return null;
//
//	}
//
//	private int generateOtp() {
//		int min = 10000;
//		int max = 99999;
//		int token = (int) (Math.random() * (max - min + 1) + min);
//
//		return token;
//	}
//
//	private void sendOtpEmail(String email, int otp) throws UnsupportedEncodingException, MessagingException {
//		MimeMessage message = javaMailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message);
//		helper.setFrom("yashporlekar8888@gmail.com", "CCR");
//		helper.setTo(email);
//		String subject = "Here's the link to reset your password";
//		String content = "<p>Hello ,</p>" + "<p>You have requested to reset your password.</p>"
//				+ "<p>Here is your OTP: " + otp + "<br>" + "<p>Ignore this email if you do remember your password, "
//				+ "or you have not made the request.</p>";
//		message.setSubject(subject);
//		helper.setText(content, true);
//		javaMailSender.send(message);
//	}
//
//	public ResponseEntity<String> candchangepassforgot(Candidate candidate) {
//		Session session = entityManager.unwrap(Session.class);
//		try {
//			CriteriaBuilder cb = session.getCriteriaBuilder();
//			CriteriaQuery<Candidate> cr = cb.createQuery(Candidate.class);
//			Root<Candidate> root = cr.from(Candidate.class);
//			cr.select(root).where(cb.equal(root.get("candidateOtp"), candidate.getCandidateOtp()));
//			Query query = session.createQuery(cr);
//
//			Candidate result = (Candidate) query.getSingleResult();
//
//			if (result != null) {
//				Date currentTimestamp = new Date(); // Current time
//				if (currentTimestamp.before(result.getOtpExpiration())) {
//					// OTP is still valid, allow the password change
//					// Perform password change logic here using the 'result' object
//
//					session.close();
//					return ResponseEntity.status(HttpStatus.OK).body("You Have Entered Correct OTP....");
//				} else {
//					// OTP has expired
//					session.close();
//					return ResponseEntity.status(HttpStatus.NOT_FOUND)
//							.body("OTP has expired. Please request a new OTP.");
//				}
//			}
//
////				session.close();
////				return ResponseEntity.status(HttpStatus.OK).body("You Have Entered Correct OTP....");
//		} catch (Exception e) {
//			session.close();
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please Enter Correct OTP....");
//		}
//		return null;
//
//	}
//
//	public ResponseEntity<String> finalcandchangepass(Candidate candidate) {
//
//		Session session = entityManager.unwrap(Session.class);
//		try {
//			CriteriaBuilder cb = session.getCriteriaBuilder();
//			CriteriaQuery<Candidate> cr = cb.createQuery(Candidate.class);
//
//			Root<Candidate> root = cr.from(Candidate.class);
//			// cr.select(root).where(cb.equal(root.get("candidateEmail"),
//			// candidate.getCandidateEmail()));
//
//			Query query = session.createQuery(cr);
//
//			Candidate retrievedCandidate = (Candidate) query.getSingleResult();
//
//			if (retrievedCandidate != null) {
//				// retrievedCandidate.setCandidatePassword(candidate.getCandidatePassword());
//				candidateRepository.save(retrievedCandidate);
//
//				session.close();
//
//				return ResponseEntity.status(HttpStatus.CREATED).body("Password Changed Sucessfully");
//			}
//
//		} catch (Exception e) {
//			session.close();
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
//		}
//		return null;
//
//	}
	/*	@Override
		public ResponseEntity<?> getAllanswer(Long candidateId) {
		 Optional<Candidate> candidateOptional = candidateRepository.findById(candidateId);
		 List<Answer>answers= new ArrayList<>();
		        if (candidateOptional.isPresent()) {
		            Candidate candidate = candidateOptional.get();
		            answers= candidate.getAnswer() ;
		            return ResponseEntity.status(HttpStatus.CREATED).body(answers);
		        } else {
		        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		        }

		return null;

	}*/


}