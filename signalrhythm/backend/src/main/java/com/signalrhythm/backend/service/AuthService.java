package com.signalrhythm.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.signalrhythm.backend.dto.auth.AuthResponse;
import com.signalrhythm.backend.dto.auth.LoginRequest;
import com.signalrhythm.backend.dto.auth.RegisterRequest;
import com.signalrhythm.backend.entity.User;
import com.signalrhythm.backend.exception.BadRequestException;
import com.signalrhythm.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public AuthResponse register(RegisterRequest req) {
		if (userRepository.existsByUsername(req.getUsername())) {
			throw new BadRequestException("Username already exists");
		}
		if (req.getEmail() != null && !req.getEmail().isBlank() && userRepository.existsByEmail(req.getEmail())) {
			throw new BadRequestException("Email already exists");
		}

		User u = new User();
		u.setUsername(req.getUsername());
		u.setEmail(req.getEmail());
		u.setPasswordHash(passwordEncoder.encode(req.getPassword()));

		User saved = userRepository.save(u);
		return new AuthResponse(saved.getUserId(), saved.getUsername(), saved.getEmail());
	}

	@Transactional(readOnly = true)
	public AuthResponse login(LoginRequest req) {
		User u = userRepository.findByUsername(req.getUsername())
				.orElseThrow(() -> new BadRequestException("Invalid username or password"));
		if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
			throw new BadRequestException("Invalid username or password");
		}
		return new AuthResponse(u.getUserId(), u.getUsername(), u.getEmail());
	}
}

