package com.signalrhythm.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.signalrhythm.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}
