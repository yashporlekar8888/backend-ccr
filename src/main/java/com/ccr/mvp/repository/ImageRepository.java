package com.ccr.mvp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccr.mvp.model.Image;



public interface ImageRepository extends JpaRepository<Image, Long> {

}

