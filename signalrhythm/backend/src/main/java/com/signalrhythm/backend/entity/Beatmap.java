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
@Table(name = "beatmaps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Beatmap {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "beatmap_id")
	private Integer beatmapId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "song_id", nullable = false)
	private Song song;

	@Column(name = "difficulty", length = 20)
	private String difficulty;

	@Column(name = "level")
	private Integer level;

	@Column(name = "note_count")
	private Integer noteCount;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "beatmap")
	private List<Note> notes = new ArrayList<>();

	@OneToMany(mappedBy = "beatmap")
	private List<PlaySession> playSessions = new ArrayList<>();

	@OneToMany(mappedBy = "beatmap")
	private List<Score> scores = new ArrayList<>();
}
