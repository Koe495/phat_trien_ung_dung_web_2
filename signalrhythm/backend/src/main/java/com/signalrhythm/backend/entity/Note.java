package com.signalrhythm.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "note_id")
	private Integer noteId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "beatmap_id", nullable = false)
	private Beatmap beatmap;

	@Column(name = "time_ms", nullable = false)
	private Integer timeMs;

	@Column(name = "lane_left")
	private Byte laneLeft;

	@Column(name = "lane_right")
	private Byte laneRight;

	@Column(name = "center_char", length = 5)
	private String centerChar;

	@Enumerated(EnumType.STRING)
	@Column(name = "note_type")
	private NoteType noteType = NoteType.tap;

	@Column(name = "duration_ms")
	private Integer durationMs = 0;
}
