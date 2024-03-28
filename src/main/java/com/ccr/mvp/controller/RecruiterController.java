package com.ccr.mvp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccr.mvp.controller.dao.CandidateDetailsDto;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.Question;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.model.User;
import com.ccr.mvp.service.RecruiterService;

@CrossOrigin(origins = "*")
@RestController
public class RecruiterController {

	@Autowired
	RecruiterService recruiterService;

	@GetMapping("/getAllQuestion")
	public List<Question> getAllQuestion() {
		return recruiterService.getAllQuestion();
	}

	@PostMapping("/getCandidateScoreRecruiter")
	@PreAuthorize("hasAnyRole('HR_ADMIN','RECRUITER')")
	public ResponseEntity<?> getCandidateScoreRecruiter(@RequestBody Candidate candidate) {

		return recruiterService.getCandidateScoreRecruiter(candidate);
	}

	// Employee hierachy
	@GetMapping("/manager/{managerId}")
	@PreAuthorize("hasAnyRole('HR_ADMIN','RECRUITER')")
	public List<Recruiter> getEmployeeHierarchy(@PathVariable Integer managerId) {

		return recruiterService.getEmployeeHierarchy(managerId);
	}

	// Employee list only added by
	@PostMapping("/recruiter")
	public List<Recruiter> getEmployeeList(@RequestBody User user) {

		return recruiterService.getEmployeeList(user);
	}
	
	// get employee by email
	@PostMapping("/findRecruiterByEmail")
	@PreAuthorize("hasAnyRole('HRADMIN')")
	public ResponseEntity<?> findRecruiterByEmail(@RequestBody User user) {

		return recruiterService.findRecruiterByEmail(user);
	}
	//Update recruiter's Approver
	@PutMapping("/changeApprover")
	@PreAuthorize("hasAnyRole('HRADMIN')")
	public ResponseEntity<?> changeApprover(@RequestBody User user,@RequestParam long userId) {

		return recruiterService.changeApprover(user,userId);
	}
	//Get updates
	@GetMapping("/getUpdates")
	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER')")
	public ResponseEntity<?> getUpdates(@RequestParam long userId) {

		return recruiterService.getUpdates(userId);
	}
	
	@GetMapping("/getProductsWithPagination")
	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER')")
    private Page<ResponseEntity<CandidateDetailsDto>> getProductsWithPagination(@RequestParam int offset, @RequestParam int pageSize) {
        return recruiterService.findProductsWithPagination(offset, pageSize);
    }

	
	@GetMapping("/getCandidateListWithPagination")
	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER')")
	public Page<?> getCandidateList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
		Pageable paging = PageRequest.of(page, size);
		return null;
//		return recruiterService.findAllWithPagination(paging);
	}
	
}
