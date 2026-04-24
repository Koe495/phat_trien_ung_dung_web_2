package com.signalrhythm.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.signalrhythm.backend.dto.game.ScoreDto;
import com.signalrhythm.backend.dto.game.SubmitScoreRequest;
import com.signalrhythm.backend.service.ScoreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {
	private final ScoreService scoreService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ScoreDto submit(@Valid @RequestBody SubmitScoreRequest req) {
		return scoreService.submit(req);
	}

	@GetMapping("/leaderboard")
	public List<ScoreDto> leaderboard(@RequestParam("beatmapId") Integer beatmapId) {
		return scoreService.leaderboard(beatmapId);
	}

	@GetMapping("/users/{userId}")
	public List<ScoreDto> history(@PathVariable Integer userId) {
		return scoreService.historyByUser(userId);
	}
}

