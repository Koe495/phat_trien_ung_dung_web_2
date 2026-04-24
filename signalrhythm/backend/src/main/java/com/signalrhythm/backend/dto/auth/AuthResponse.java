package com.signalrhythm.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
	private Integer userId;
	private String username;
	private String email;
}

