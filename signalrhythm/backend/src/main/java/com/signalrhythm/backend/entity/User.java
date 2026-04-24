package com.signalrhythm.backend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "username", nullable = false, unique = true, length = 50)
	private String username;

	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

	@Column(name = "email", length = 100)
	private String email;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "user")
	private List<PlaySession> playSessions = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Score> scores = new ArrayList<>();
}
