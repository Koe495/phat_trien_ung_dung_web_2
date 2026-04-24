package com.signalrhythm.backend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "play_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaySession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "session_id")
	private Integer sessionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "beatmap_id")
	private Beatmap beatmap;

	@Column(name = "started_at", insertable = false, updatable = false)
	private LocalDateTime startedAt;

	@Column(name = "ended_at")
	private LocalDateTime endedAt;

	@OneToMany(mappedBy = "session")
	private List<Score> scores = new ArrayList<>();
}
