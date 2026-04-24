package com.signalrhythm.backend.dto.game;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaySessionDto {
	private Integer sessionId;
	private Integer userId;
	private Integer beatmapId;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
}

