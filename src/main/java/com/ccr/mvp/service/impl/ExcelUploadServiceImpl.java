package com.ccr.mvp.service.impl;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ccr.mvp.model.PreviousDataOfCompany;
import com.ccr.mvp.model.User;
import com.ccr.mvp.service.ExcelUploadService;

@Service
public class ExcelUploadServiceImpl implements ExcelUploadService {

	@Override
	public boolean isValidExcelFile(MultipartFile file) {
		return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
	}

	@Override
	public List<PreviousDataOfCompany> getCustomersDataFromExcel(InputStream inputStream) {
		List<PreviousDataOfCompany> previousDataOfCompanys = new ArrayList<>();
	       try {
	           XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	           XSSFSheet sheet = workbook.getSheet("Sheet1");
	           int rowIndex =0;
	           for (Row row : sheet){
	               if (rowIndex ==0){
	                   rowIndex++;
	                   continue;
	               }
	               Iterator<Cell> cellIterator = row.iterator();
	               int cellIndex = 0;
	               PreviousDataOfCompany previousDataOfCompany = new PreviousDataOfCompany();
	               while (cellIterator.hasNext()){
	                   Cell cell = cellIterator.next();
	                   switch (cellIndex){
	                       case 0 -> previousDataOfCompany.setCandidateName(cell.getStringCellValue());
	                       case 1 -> previousDataOfCompany.setJobRole(cell.getStringCellValue());
	                       case 2 -> previousDataOfCompany.setHiringStatus(cell.getStringCellValue());
	                       case 3 -> previousDataOfCompany.setJoiningStatus(cell.getStringCellValue());
	                       case 4 -> previousDataOfCompany.setInterviewDate((Date) cell.getDateCellValue());
	                       case 5 -> previousDataOfCompany.setCandidateEmailId(cell.getStringCellValue());
	                       case 6 -> previousDataOfCompany.setCandidatePhoneNumber((long) cell.getNumericCellValue());
	                       default -> {
	                       }
	                   }
	                   cellIndex++;
	               }
	               previousDataOfCompanys.add(previousDataOfCompany);
	           }
	       } catch (IOException e) {
	           e.getStackTrace();
	       }
	       return previousDataOfCompanys;
	}

}
