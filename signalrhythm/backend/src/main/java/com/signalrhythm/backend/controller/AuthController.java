package com.signalrhythm.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.signalrhythm.backend.dto.auth.AuthResponse;
import com.signalrhythm.backend.dto.auth.LoginRequest;
import com.signalrhythm.backend.dto.auth.RegisterRequest;
import com.signalrhythm.backend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
		return authService.register(req);
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest req) {
		return authService.login(req);
	}
}

