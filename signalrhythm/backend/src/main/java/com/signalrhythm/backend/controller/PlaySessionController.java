package com.signalrhythm.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.signalrhythm.backend.dto.game.EndSessionRequest;
import com.signalrhythm.backend.dto.game.PlaySessionDto;
import com.signalrhythm.backend.dto.game.StartSessionRequest;
import com.signalrhythm.backend.service.PlaySessionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class PlaySessionController {
	private final PlaySessionService playSessionService;

	@PostMapping
	public PlaySessionDto start(@Valid @RequestBody StartSessionRequest req) {
		return playSessionService.start(req);
	}

	@PatchMapping("/{sessionId}/end")
	public PlaySessionDto end(@PathVariable Integer sessionId, @RequestBody(required = false) EndSessionRequest req) {
		return playSessionService.end(sessionId, req != null ? req.getEndedAt() : null);
	}

	@GetMapping("/users/{userId}")
	public List<PlaySessionDto> history(@PathVariable Integer userId) {
		return playSessionService.historyByUser(userId);
	}
}

