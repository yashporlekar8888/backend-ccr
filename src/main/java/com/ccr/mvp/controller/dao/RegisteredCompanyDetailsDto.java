package com.ccr.mvp.controller.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredCompanyDetailsDto {
	private Long companyId;
	private String companyName;
	private boolean approvalStatus;
}
