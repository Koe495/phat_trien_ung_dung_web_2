package com.signalrhythm.backend.exception;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage(), req);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
		return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
		String msg = ex.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.map(fe -> fe.getField() + " " + fe.getDefaultMessage())
				.orElse("Validation error");
		return build(HttpStatus.BAD_REQUEST, msg, req);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest req) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req);
	}

	private static ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest req) {
		ApiError body = new ApiError(
				OffsetDateTime.now(),
				status.value(),
				status.getReasonPhrase(),
				message,
				req.getRequestURI());
		return ResponseEntity.status(status).body(body);
	}
}

