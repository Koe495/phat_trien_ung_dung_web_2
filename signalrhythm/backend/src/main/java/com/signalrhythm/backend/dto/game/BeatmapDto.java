package com.signalrhythm.backend.dto.game;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeatmapDto {
	private Integer beatmapId;
	private Integer songId;
	private String difficulty;
	private Integer level;
	private Integer noteCount;
	private LocalDateTime createdAt;
}

