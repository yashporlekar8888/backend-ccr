package com.ccr.mvp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ccr.mvp.model.PreviousDataOfCompany;
import com.ccr.mvp.model.User;
import com.ccr.mvp.service.PreviousDataService;

@CrossOrigin(origins = "*")
@RestController
public class HrAdminController {

	@Autowired
	private PreviousDataService previousDataService;

	@PostMapping("/uploadPreviousData")
	@PreAuthorize("hasRole('HRADMIN')")
	public ResponseEntity<?> uploadPreviousData(@RequestParam("file") MultipartFile file)  {
		this.previousDataService.savePreviousDataToDatabase(file);
		return ResponseEntity.ok(Map.of("Message", " Customers data uploaded and saved to database successfully"));
	}

	@GetMapping("/getPreviousDataOfCompany")
	@PreAuthorize("hasRole('HRADMIN')")
	public ResponseEntity<List<PreviousDataOfCompany>> getPreviousDataOfCompany() {
		return new ResponseEntity<>(previousDataService.getPreviousData(), HttpStatus.FOUND);
	}
}
