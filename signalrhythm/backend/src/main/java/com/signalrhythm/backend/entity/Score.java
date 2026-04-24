package com.signalrhythm.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Score {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "score_id")
	private Integer scoreId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id")
	private PlaySession session;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "beatmap_id")
	private Beatmap beatmap;

	@Column(name = "score")
	private Integer score;

	@Column(name = "max_combo")
	private Integer maxCombo;

	@Column(name = "perfect_count")
	private Integer perfectCount;

	@Column(name = "good_count")
	private Integer goodCount;

	@Column(name = "fail_count")
	private Integer failCount;

	@Column(name = "accuracy")
	private Float accuracy;

	@Column(name = "played_at", insertable = false, updatable = false)
	private LocalDateTime playedAt;
}
