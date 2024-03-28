package com.ccr.mvp.dto;

import com.ccr.mvp.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {
	String token;
	String email;
	Long userId;
	String userName;
	Role role;
}
