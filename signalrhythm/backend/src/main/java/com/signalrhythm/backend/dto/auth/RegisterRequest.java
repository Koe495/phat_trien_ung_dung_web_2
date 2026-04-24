package com.signalrhythm.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
	@NotBlank
	@Size(max = 50)
	private String username;

	@NotBlank
	@Size(min = 6, max = 100)
	private String password;

	@Email
	@Size(max = 100)
	private String email;
}

