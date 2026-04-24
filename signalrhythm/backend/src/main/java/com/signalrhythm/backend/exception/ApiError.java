package com.signalrhythm.backend.exception;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
	private OffsetDateTime timestamp;
	private int status;
	private String error;
	private String message;
	private String path;
}

