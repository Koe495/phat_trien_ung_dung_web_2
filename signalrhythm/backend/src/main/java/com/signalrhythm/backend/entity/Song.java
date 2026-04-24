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
@Table(name = "songs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Song {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "song_id")
	private Integer songId;

	@Column(name = "title", nullable = false, length = 100)
	private String title;

	@Column(name = "artist", length = 100)
	private String artist;

	@Column(name = "bpm", nullable = false)
	private Float bpm;

	@Column(name = "offset_ms")
	private Integer offsetMs = 0;

	@Column(name = "audio_path", length = 255)
	private String audioPath;

	@Column(name = "background_path", length = 255)
	private String backgroundPath;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "song")
	private List<Beatmap> beatmaps = new ArrayList<>();
}
