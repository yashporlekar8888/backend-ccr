package com.ccr.mvp.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ccr.mvp.model.PreviousDataOfCompany;
import com.ccr.mvp.model.User;
import com.ccr.mvp.repository.PreviousDataOfCompanyRepository;
import com.ccr.mvp.service.ExcelUploadService;
import com.ccr.mvp.service.PreviousDataService;

@Service
public class PreviousDataServiceImpl implements PreviousDataService{

	@Autowired
	PreviousDataOfCompanyRepository previousDataOfCompanyRepository;
	
	@Autowired
	ExcelUploadService excelUploadService;
	
	@Override
	public void savePreviousDataToDatabase(MultipartFile file) {
		if(excelUploadService.isValidExcelFile(file)){
            try {
                List<PreviousDataOfCompany> previousData = excelUploadService.getCustomersDataFromExcel(file.getInputStream());
                this.previousDataOfCompanyRepository.saveAll(previousData);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
		
	}

	@Override
	public List<PreviousDataOfCompany> getPreviousData() {
		return previousDataOfCompanyRepository.findAll();
	}

}
