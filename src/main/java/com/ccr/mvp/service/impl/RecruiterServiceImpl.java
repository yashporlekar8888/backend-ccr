package com.ccr.mvp.service.impl;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ccr.mvp.controller.dao.CandidateDetailsDto;
import com.ccr.mvp.model.Answer;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.Category;
import com.ccr.mvp.model.CategoryAverageScoreDTO;
import com.ccr.mvp.model.Question;
import com.ccr.mvp.model.ReceiverUpdates;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.model.Role;
import com.ccr.mvp.model.Updates;
import com.ccr.mvp.model.User;
import com.ccr.mvp.repository.AnswerRepository;
import com.ccr.mvp.repository.CalculatedScoreRepository;
import com.ccr.mvp.repository.CandidateRepository;
import com.ccr.mvp.repository.CategoryRepository;
import com.ccr.mvp.repository.CompanyRepository;
import com.ccr.mvp.repository.QuestionRepository;
import com.ccr.mvp.repository.ReceiverUpdatesRepository;
import com.ccr.mvp.repository.RecruiterRepository;
import com.ccr.mvp.repository.UpdatesRepository;
import com.ccr.mvp.repository.UserRepository;
import com.ccr.mvp.service.RecruiterService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class RecruiterServiceImpl implements RecruiterService {

	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	CandidateRepository candidateRepository;
	@Autowired
	AnswerRepository answerRepository;
	@Autowired
	CalculatedScoreRepository calculatedScoreRepository;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	RecruiterRepository recruiterRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UpdatesRepository updatesRepository;

	@Autowired
	ReceiverUpdatesRepository receiverUpdatesRepository;

	@Override
	public List<Question> getAllQuestion() {
		return questionRepository.findAll();
	}

	@Override
	public ResponseEntity<?> getCandidateScoreRecruiter(Candidate candidate) {

		try {
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

				double z = totalRecords1 / totalQuestions1;

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

			candidate.setCandidateAvgScore(AvgScore1);
			candidateRepository.save(candidate);

			return null;
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		}

	}

	private double getQuestionWeightage(Question question) {
		List<Question> questions = questionRepository.findByQuestionId(question.getQuestionId());
		double questionWeightage = 0.0;
		for (Question questionObj : questions) {
			questionWeightage = questionObj.getWeightage();
		}
		return questionWeightage;
	}

	public List<Recruiter> getEmployeeHierarchy(Integer managerId) {
		List<Recruiter> allEmployees = recruiterRepository.findAll();
		List<Recruiter> hierarchy = new ArrayList<>();

		// Find the manager based on managerId

		Recruiter manager = allEmployees.stream().filter(hr -> hr.getRecruiterId().equals(managerId)).findFirst()
				.orElse(null);
		System.out.println(manager);

		if (manager != null) {
			buildEmployeeHierarchy(manager, allEmployees, hierarchy);
		}

		return hierarchy;
	}

	private void buildEmployeeHierarchy(Recruiter employee, List<Recruiter> allEmployees, List<Recruiter> hierarchy) {
		List<Recruiter> subordinates = allEmployees.stream()
				.filter(hr -> hr.getAddedBy().equals(employee.getRecruiterId())).collect(Collectors.toList());

		for (Recruiter subordinate : subordinates) {
			hierarchy.add(subordinate);
		}

		for (Recruiter subordinate : subordinates) {
			buildEmployeeHierarchy(subordinate, allEmployees, hierarchy);
		}
	}

	@Override
	public Recruiter hrAdminRegistration(Recruiter hrRecruiter) {
		recruiterRepository.save(hrRecruiter);
		return hrRecruiter;
	}

	@Override
	public Recruiter recruiterRegistration(Recruiter recruiter) {
		recruiterRepository.save(recruiter);
		return recruiter;
	}

	@Override
	public ResponseEntity<?> updatePowers(Recruiter recruiter, long userId) {

		try {

			User user = new User();
			Recruiter recruiter1 = new Recruiter();
			Updates updates = new Updates();
			ReceiverUpdates receiverUpdates = new ReceiverUpdates();

			user = userRepository.findByemail(recruiter.getUser().getEmail());
			recruiter1 = recruiterRepository.findByUser(user);
			Role role = recruiter1.getUser().getRole();

			if (role.equals(Role.ROLE_HRADMIN)) {
				if (recruiter.isAddedPower() == true && recruiter.isApprovePower() == false)

				{
					recruiter1.setApprover(userId);
					user.setRole(Role.ROLE_RECRUITER);
					recruiter1.setUser(user);
					recruiter1.setAddedPower(recruiter.isAddedPower());
					recruiter1.setApprovePower(recruiter.isApprovePower());
					recruiterRepository.save(recruiter1);

					List<Recruiter> recruitersList = recruiterRepository.findByApprover(user.getUserId());
					for (Recruiter rec : recruitersList) {
						rec.setApprover(userId);
						recruiterRepository.save(rec);
					}
					updates.setNotification("Powers changed one true");
					updates.setSenderUserId(userId);
					updatesRepository.save(updates);
					receiverUpdates.setUpdates(updates);
					receiverUpdates.setReceiverUserId(recruiter1.getUser().getUserId());
					receiverUpdatesRepository.save(receiverUpdates);

					return ResponseEntity.status(HttpStatus.CREATED).body(recruiter1);
				} else if (recruiter.isAddedPower() == false && recruiter.isApprovePower() == false) {
					recruiter1.setApprover(userId);
					user.setRole(Role.ROLE_RECRUITER);
					recruiter1.setUser(user);
					recruiter1.setAddedPower(recruiter.isAddedPower());
					recruiter1.setApprovePower(recruiter.isApprovePower());
					recruiterRepository.save(recruiter1);
					List<Recruiter> recruitersList = recruiterRepository.findByAddedBy(user.getUserId());
					for (Recruiter rec : recruitersList) {
						rec.setAddedBy(userId);
						rec.setApprover(userId);
						recruiterRepository.save(rec);
					}

					updates.setNotification("Powers changed both false");
					updates.setSenderUserId(userId);
					updatesRepository.save(updates);
					receiverUpdates.setUpdates(updates);
					receiverUpdates.setReceiverUserId(recruiter1.getUser().getUserId());
					receiverUpdatesRepository.save(receiverUpdates);

					return ResponseEntity.status(HttpStatus.CREATED).body(recruiter1);
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("wrong powers");
				}

			} else if (role.equals(Role.ROLE_RECRUITER)) {
				if (recruiter.isAddedPower() == true && recruiter.isApprovePower() == true) {
					recruiter1.setApprover(recruiter1.getUser().getUserId());
					user.setRole(Role.ROLE_HRADMIN);
					recruiter1.setUser(user);
					recruiter1.setAddedPower(recruiter.isAddedPower());
					recruiter1.setApprovePower(recruiter.isApprovePower());
					recruiterRepository.save(recruiter1);
					return ResponseEntity.status(HttpStatus.CREATED).body(recruiter1);
				} else if (recruiter.isAddedPower() == true && recruiter.isApprovePower() == false) {
					recruiter1.setAddedPower(recruiter.isAddedPower());
					recruiter1.setApprovePower(recruiter.isApprovePower());
					recruiterRepository.save(recruiter1);
					return ResponseEntity.status(HttpStatus.CREATED).body(recruiter1);
				} else if (recruiter.isAddedPower() == false && recruiter.isApprovePower() == false) {
					recruiter1.setAddedPower(recruiter.isAddedPower());
					recruiter1.setApprovePower(recruiter.isApprovePower());
					recruiterRepository.save(recruiter1);
					List<Recruiter> recruitersList = recruiterRepository.findByAddedBy(user.getUserId());
					for (Recruiter rec : recruitersList) {
						rec.setAddedBy(userId);
						rec.setApprover(userId);
						recruiterRepository.save(rec);
					}
					return ResponseEntity.status(HttpStatus.CREATED).body(recruiter1);
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("wrong powers");
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't update powers");
			}

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		}

	}

	@Override
	public ResponseEntity<?> findRecruiterByEmail(User user) {
		try {

			Recruiter recruiter = new Recruiter();
			user = userRepository.findByemail(user.getEmail());
			recruiter = recruiterRepository.findByUser(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(recruiter);

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		}

	}

	@Override
	public ResponseEntity<?> changeApprover(User user, long userId) {

		try {
			Recruiter recruiter = new Recruiter();

			user = userRepository.findByemail(user.getEmail());
			recruiter = recruiterRepository.findByUser(user);
			recruiter.setApprover(userId);
			recruiterRepository.save(recruiter);
			return ResponseEntity.status(HttpStatus.CREATED).body("Approver changed");
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		}

	}

	@Override
	public ResponseEntity<?> getUpdates(long userId) {
		try {
			List<Updates> updates = new ArrayList<>();
			updates = updatesRepository.findNotificationsByReceiverUserId(userId);
			return ResponseEntity.status(HttpStatus.CREATED).body(updates);

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		}
	}

	@Override
	public List<Recruiter> getEmployeeList(User user) {

		// User user = new User();
		Recruiter recruiter = new Recruiter();
		recruiter = recruiterRepository.findByUser(user);
		List<Recruiter> allEmployees = recruiterRepository.findAll();
		List<Recruiter> subordinates = allEmployees.stream()
				.filter(recruiter1 -> recruiter1.getAddedBy().equals(user.getUserId())).collect(Collectors.toList());
		return subordinates;
	}

	@Override
	public ResponseEntity<?> getNumberOfRecruiters() {
		Role role = Role.ROLE_RECRUITER;
		List<User> recruiterList = userRepository.findAllByRole(role);
		//long numberOfRecruiters = recruiterList.size();
		//Recruiter recruiter = new Recruiter();

		return ResponseEntity.ok(recruiterList);
	}

	/*@Override
	public ResponseEntity<?> getNumberOfHrAdmins() {
		Role role = Role.ROLE_HRADMIN;
		List<User> hrAdminList = userRepository.findAllByRole(role);
		long numberOfHrAdmins = hrAdminList.size();

		for (User hrAdmins : hrAdminList) {
			Recruiter recruiter = new Recruiter();
			recruiter = recruiterRepository.findByUser(hrAdmins);

			List<Recruiter> hrAdmin = new ArrayList<>();
			if (recruiter.getAddedBy() == 0) {
				hrAdmin.add(recruiter);
			}
		}

		return ResponseEntity.ok(numberOfHrAdmins);
	}*/

	@Override
	public ResponseEntity<?> getNumberOfCandidates() {
		Role role = Role.ROLE_CANDIDATE;
		List<User> candidateList = userRepository.findAllByRole(role);
		//long numberOfCandidates = candidateList.size();
		return ResponseEntity.ok(candidateList);
	}

//	public ResponseEntity<?> getHrAdminDetails() {
//
//		Role role = Role.ROLE_HRADMIN;
//		Company company = new Company();
//		List<Recruiter> companyrecruiterDetails = new ArrayList<>();
//		List<Company> companyDetails = companyRepository.findAll();
//		for (Company companyDetail : companyDetails) {
//			if (companyDetail.getRegistrationApproval()) {
//				companyrecruiterDetails = recruiterRepository.findAll().stream()
//						.filter(recruiter -> recruiter.getAddedBy() == 0).collect(Collectors.toList());
//			}
//		}
//		return ResponseEntity.ok(companyrecruiterDetails);
//	}

	public ResponseEntity<?> getHrAdminDetails() {

		List<Recruiter> companyrecruiterDetails = recruiterRepository.findAll().stream().filter(
				recruiter -> recruiter.getAddedBy() == 0)
				.collect(Collectors.toList());
		return ResponseEntity.ok(companyrecruiterDetails);
	}

	@Override
	public ResponseEntity<?> getCandidateDetails() {
//		CandidateDetailsDto candidateDetailsDto = new CandidateDetailsDto();
		Role role = Role.ROLE_CANDIDATE;
		List<User> candidateList = userRepository.findAllByRole(role);
		List<CandidateDetailsDto> candidateListDto = new ArrayList<>();
		for (User candidate : candidateList) {
			CandidateDetailsDto candidateDetailsDto = new CandidateDetailsDto(candidate.getUserId() ,candidate.getUserName(),
					candidate.getCandidate().getCandidateAadhar(), candidate.getUsername(), candidate.getPhoneNumber(),candidate.getCandidate().getCandidateId());
			candidateListDto.add(candidateDetailsDto);
		}
		
		return ResponseEntity.ok(candidateListDto);
	}
	

	@Override
	public Page<ResponseEntity<CandidateDetailsDto>> findProductsWithPagination(int offset, int pageSize) {
		Role role = Role.ROLE_CANDIDATE;
        Page<User> candidateList = userRepository.findAll(PageRequest.of(offset, pageSize));
        List<CandidateDetailsDto> candidateListDto = new ArrayList<>();
		for (User candidate : candidateList) {
			CandidateDetailsDto candidateDetailsDto = new CandidateDetailsDto(candidate.getUserId() ,candidate.getUserName(),
					candidate.getCandidate().getCandidateAadhar(), candidate.getUsername(), candidate.getPhoneNumber(),candidate.getCandidate().getCandidateId());
			candidateListDto.add(candidateDetailsDto);
		}
        return (Page<ResponseEntity<CandidateDetailsDto>>) ResponseEntity.ok(candidateListDto);
	}

	@Override
	public Page<?> findAllWithPagination(Pageable pageable) {
		return null;
//		return candidateRepository.findAll(pageable);
	}
}