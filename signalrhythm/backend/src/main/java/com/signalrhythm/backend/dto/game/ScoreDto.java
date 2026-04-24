package com.signalrhythm.backend.dto.game;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreDto {
	private Integer scoreId;
	private Integer sessionId;
	private Integer userId;
	private Integer beatmapId;
	private Integer score;
	private Integer maxCombo;
	private Integer perfectCount;
	private Integer goodCount;
	private Integer failCount;
	private Float accuracy;
	private LocalDateTime playedAt;
}

