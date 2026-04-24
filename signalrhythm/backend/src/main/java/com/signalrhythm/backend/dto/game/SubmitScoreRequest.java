package com.signalrhythm.backend.dto.game;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitScoreRequest {
	@NotNull
	private Integer sessionId;

	@NotNull
	private Integer userId;

	@NotNull
	private Integer beatmapId;

	@NotNull
	private Integer score;

	private Integer maxCombo;
	private Integer perfectCount;
	private Integer goodCount;
	private Integer failCount;
	private Float accuracy;
}

