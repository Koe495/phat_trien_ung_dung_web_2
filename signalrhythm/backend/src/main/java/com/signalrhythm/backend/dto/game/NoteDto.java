package com.signalrhythm.backend.dto.game;

import com.signalrhythm.backend.entity.NoteType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoteDto {
	private Integer noteId;
	private Integer beatmapId;
	private Integer timeMs;
	private Byte laneLeft;
	private Byte laneRight;
	private String centerChar;
	private NoteType noteType;
	private Integer durationMs;
}

