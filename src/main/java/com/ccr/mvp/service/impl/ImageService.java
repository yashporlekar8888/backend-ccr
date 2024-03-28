package com.ccr.mvp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccr.mvp.model.Image;
import com.ccr.mvp.repository.ImageRepository;

@Service
public class ImageService {
	
	///// NOT USED JUST FOR EXAMPLE PURPOSE

	@Autowired
	private ImageRepository imageRepository;

	public Image saveImage(Image image) {
		return imageRepository.save(image);
	}

	public Image getImage(Long id) {
		return imageRepository.findById(id).orElse(null);
	}
}
