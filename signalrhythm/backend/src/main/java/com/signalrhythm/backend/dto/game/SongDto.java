package com.signalrhythm.backend.dto.game;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SongDto {
	private Integer songId;
	private String title;
	private String artist;
	private Float bpm;
	private Integer offsetMs;
	private String audioPath;
	private String backgroundPath;
	private LocalDateTime createdAt;
}

