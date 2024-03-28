package com.ccr.mvp.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ccr.mvp.model.PreviousDataOfCompany;
import com.ccr.mvp.model.User;

public interface PreviousDataService {

	void savePreviousDataToDatabase(MultipartFile file);
	
	List<PreviousDataOfCompany> getPreviousData();

}
