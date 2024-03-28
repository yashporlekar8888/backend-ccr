package com.ccr.mvp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccr.mvp.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	
}
