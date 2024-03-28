package com.ccr.mvp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ccr.mvp.model.CalculatedScore;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.Comment;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.repository.CalculatedScoreRepository;
import com.ccr.mvp.repository.CandidateRepository;
import com.ccr.mvp.repository.CommentRepository;
import com.ccr.mvp.service.CommentService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	CalculatedScoreRepository calculatedScoreRepository;
	
	@PersistenceContext
	EntityManager entityManager;

//	@Override
//	public ResponseEntity<?> saveComment(Long candidateId, Long recruiterId, Comment comment) {
//		try {
//			Recruiter recruiter = new Recruiter();
//			Candidate candidate = candidateRepository.findById(candidateId)
//					.orElseThrow(() -> new Exception("User not found with id: " + candidateId));
//
//			comment.setCommentContent(comment.getCommentContent());
//			comment.setCommentApprove(false);
//			comment.setCommentSuggestion(false);
//			recruiter.setRecruiterId(recruiterId);
//			comment.setRecruiter(recruiter);
//			comment.setCandidate(candidate);
//			commentRepository.save(comment);
//			
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
//		}
//		
//		return null;
//	}

///////// Comments approval candidate
	/*
	 * public ResponseEntity<?> commentsapprove(long candidateId, long recruiterId,
	 * String comment) { Comment comment1 = new Comment();
	 * comment1.setCommentContent(comment); comment1.setCommentApprove(false);
	 * 
	 * Candidate candidate = new Candidate(); candidate.setCandidateId(candidateId);
	 * comment1.setCandidate(candidate);
	 * 
	 * Recruiter recruiter = new Recruiter(); recruiter.setRecruiterId(recruiterId);
	 * comment1.setRecruiter(recruiter);
	 * 
	 * commentRepository.save(comment1); return null; }
	 */

	public List<Comment> Newcommentrequest(long recruiterId) {

		CriteriaBuilder cb1 = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recruiter> cr1 = cb1.createQuery(Recruiter.class);
		Root<Recruiter> root1 = cr1.from(Recruiter.class);

		cr1.select(root1).where(cb1.equal(root1.get("approver"), recruiterId));
		Query query1 = entityManager.createQuery(cr1);

		List<Recruiter> results1 = query1.getResultList();

		List<Comment> allComments = new ArrayList<>();
		for (Recruiter recruiter : results1) {
			List<Comment> recruiterComments = getRecruiterComments(recruiter.getRecruiterId());
			allComments.addAll(recruiterComments);
		}

		return allComments;

	}

	public List<Comment> getRecruiterComments(long recruiterId) {
		CriteriaBuilder cb2 = entityManager.getCriteriaBuilder();
		CriteriaQuery<Comment> cr2 = cb2.createQuery(Comment.class);
		Root<Comment> root2 = cr2.from(Comment.class);

		cr2.select(root2).where(cb2.equal(root2.get("recruiter").get("recruiterId"), recruiterId),
				cb2.equal(root2.get("commentApprove"), false),cb2.equal(root2.get("commentSuggestion"), false));
		Query query2 = entityManager.createQuery(cr2);

		return query2.getResultList();
	}

//public ResponseEntity<?> getcommentrequest(Comment comment) {
//	Session session = entityManager.unwrap(Session.class);
//	CriteriaBuilder cb = session.getCriteriaBuilder();
//	CriteriaQuery<Comment> cr = cb.createQuery(Comment.class);
//	Root<Comment> root = cr.from(Comment.class);
//
//	cr.select(root).where(cb.equal(root.get("comment_id"), comment.getComment_id()));
//	Query query = session.createQuery(cr);
//	Comment results = null;
//	results = (Comment) query.getSingleResult();
//	String a = results.getComment();
//	session.close();
//	return ResponseEntity.status(HttpStatus.OK).body(a);
//
//}

	public ResponseEntity<?> commentaccept(long commentId) {
		Session session = entityManager.unwrap(Session.class);
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Comment> cr = cb.createQuery(Comment.class);
		Root<Comment> root = cr.from(Comment.class);

		Join<Comment, Candidate> candidateJoin = root.join("candidate");
		Join<Comment, Recruiter> hrJoin = root.join("recruiter");

		cr.select(root).where(cb.and(cb.equal(root.get("commentId"), commentId)));

		Query query = session.createQuery(cr);
		Comment result = null;
		try {
			result = (Comment) query.getSingleResult();
			result.setCommentApprove(true);
			commentRepository.save(result);

			return ResponseEntity.status(HttpStatus.OK).body("Comment is approved by HR admin.");
		} catch (NoResultException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found.");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request.");
		}
	}

	public ResponseEntity<?> commentsuggestion(long commentId, String suggestion) {
		Session session = entityManager.unwrap(Session.class);
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Comment> cr = cb.createQuery(Comment.class);
		Root<Comment> root = cr.from(Comment.class);

		cr.select(root).where(cb.and(cb.equal(root.get("commentId"), commentId)));

		Query query = session.createQuery(cr);
		Comment result = null;
		try {
			result = (Comment) query.getSingleResult();
			result.setSuggestionContent(suggestion);
			result.setCommentSuggestion(true);
			commentRepository.save(result);

			return ResponseEntity.status(HttpStatus.OK).body("Suggestion Given Sucessfully");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request.");
		}

	}

	public ResponseEntity<?> getsuggestion( long recruiterId) {
		Session session = entityManager.unwrap(Session.class);
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Comment> cr = cb.createQuery(Comment.class);
		Root<Comment> root = cr.from(Comment.class);

		cr.select(root)
				.where(cb.and(cb.equal(root.get("recruiter").get("recruiterId"), recruiterId),
						cb.equal(root.get("commentSuggestion"), true)));
						

		Query query = session.createQuery(cr);
		List<Comment> result = null;
		try {
			result =  query.getResultList();
		

			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request.");
		}

	}

	@Override
	public ResponseEntity<?> newComment(long commentId, String comment) {
		Session session = entityManager.unwrap(Session.class);
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Comment> cr = cb.createQuery(Comment.class);
		Root<Comment> root = cr.from(Comment.class);

		cr.select(root).where(cb.and(cb.equal(root.get("commentId"), commentId)));

		Query query = session.createQuery(cr);
		Comment result = null;
		try {
			result = (Comment) query.getSingleResult();
			result.setCommentContent(comment);
			result.setCommentSuggestion(false);
			commentRepository.save(result);

			return ResponseEntity.status(HttpStatus.OK).body("New comment added");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request.");
		}
	}

	

}
