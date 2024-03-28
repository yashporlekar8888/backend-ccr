package com.ccr.mvp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.Comment;
import com.ccr.mvp.model.Recruiter;

public interface CommentRepository extends JpaRepository<Comment,Long> {


}
