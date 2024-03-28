package com.ccr.mvp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccr.mvp.model.Comment;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.repository.CandidateRepository;
import com.ccr.mvp.repository.CommentRepository;
import com.ccr.mvp.service.CommentService;

@CrossOrigin(origins = "*")
@RestController
public class CommentController {

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	CandidateRepository canidateRepository;

	@Autowired
	CommentService commentService;
	
	
//	//Comment in rating form
//	@PostMapping("/submitComment")
//<<<<<<< HEAD
//	public ResponseEntity<?> submitComment(@RequestBody Comment comment, @RequestParam Long candidateId,
//			@RequestParam Long recruiterId) {
//=======
//	@PreAuthorize("hasAnyRole('HRADMIN','RECRUITER')")
//	public ResponseEntity<?> submitComment(@RequestBody Comment comment, @RequestParam Long candidateId,@RequestParam Long recruiterId) {
//>>>>>>> 9feb7f99470ae4df7d84851e84ac566d47e40588
//		commentService.saveComment(candidateId, recruiterId, comment);
//		return ResponseEntity.ok("Comment submitted successfully");
//	}


///// get hr request for approval to hradmin
	@GetMapping("/Newcommentrequest")
	public List<Comment> Newcommentrequest(@RequestParam long recruiterId) {
		return commentService.Newcommentrequest(recruiterId);
	}


/////// comment approved by hr admin
	@PostMapping("/commentaccept")
	public ResponseEntity<?> commentaccept(@RequestParam long commentId) {
		return commentService.commentaccept(commentId);
	}

/////// given suggestion by hr admin to recruiter to change comment
	@PostMapping("/commentsuggestion")
	public ResponseEntity<?> commentsuggestion(@RequestParam long commentId, @RequestParam String suggestion) {
		return commentService.commentsuggestion(commentId, suggestion);
	}

/////// after giving suggestion the update comment request will go to the recruiter
	@GetMapping("/suggestionFromHradmin")
	public ResponseEntity<?> getsuggestion(@RequestParam long recruiterId) {
		return commentService.getsuggestion(recruiterId);
	}
	
/////// New comment after hr admin suggestion
	@PostMapping("/newComment")
	public ResponseEntity<?> newComment(@RequestParam long commentId, @RequestParam String comment) {
		return commentService.newComment(commentId,comment);
	}

	
}
