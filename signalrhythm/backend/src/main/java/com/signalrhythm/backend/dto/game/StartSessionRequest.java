package com.signalrhythm.backend.dto.game;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StartSessionRequest {
	@NotNull
	private Integer userId;

	@NotNull
	private Integer beatmapId;
}

