package com.ccr.mvp.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ccr.mvp.model.Comment;
import com.ccr.mvp.model.Recruiter;

public interface CommentService {
//	ResponseEntity<?> saveComment(Long candidateId, Long recruiterId, Comment comment);

	List<Comment> Newcommentrequest(long recruiterId);

	ResponseEntity<?> commentaccept( long commentId);

	ResponseEntity<?> commentsuggestion( long commentId, String suggestion);

	ResponseEntity<?> getsuggestion(long recruiterId);

	ResponseEntity<?> newComment(long commentId, String comment);

	

}
