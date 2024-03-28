package com.ccr.mvp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccr.mvp.service.CcrAdminService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class SuperAdminController {
	
	@Autowired
	CcrAdminService ccrAdminService;
	
	@GetMapping("/getAllCcrAdminList")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public ResponseEntity<?> getAllCcrAdminList(){
		return ccrAdminService.getAllCcrAdminList();
	}
}
