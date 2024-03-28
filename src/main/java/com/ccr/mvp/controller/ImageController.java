package com.ccr.mvp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ccr.mvp.model.Image;
import com.ccr.mvp.service.impl.ImageService;

/////NOT USED JUST FOR EXAMPLE PURPOSE

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/images")
public class ImageController {

	@Autowired
	private ImageService imageService;

	@PostMapping
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
		Image image = new Image();
		image.setData(file.getBytes());
		imageService.saveImage(image);
		return ResponseEntity.ok("Image uploaded successfully");
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getImage(@PathVariable Long id) {
		Image image = imageService.getImage(id);
		if (image != null) {
			return ResponseEntity.ok(image.getData());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}