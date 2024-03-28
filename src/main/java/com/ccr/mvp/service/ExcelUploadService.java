package com.ccr.mvp.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ccr.mvp.model.PreviousDataOfCompany;
import com.ccr.mvp.model.User;

public interface ExcelUploadService {

	 boolean isValidExcelFile(MultipartFile file);

	List<PreviousDataOfCompany> getCustomersDataFromExcel(InputStream inputStream);
	 
	 
}
