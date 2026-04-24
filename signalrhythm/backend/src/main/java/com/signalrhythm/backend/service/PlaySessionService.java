package com.signalrhythm.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.signalrhythm.backend.dto.game.PlaySessionDto;
import com.signalrhythm.backend.dto.game.StartSessionRequest;
import com.signalrhythm.backend.entity.Beatmap;
import com.signalrhythm.backend.entity.PlaySession;
import com.signalrhythm.backend.entity.User;
import com.signalrhythm.backend.exception.NotFoundException;
import com.signalrhythm.backend.repository.BeatmapRepository;
import com.signalrhythm.backend.repository.PlaySessionRepository;
import com.signalrhythm.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaySessionService {
	private final PlaySessionRepository playSessionRepository;
	private final UserRepository userRepository;
	private final BeatmapRepository beatmapRepository;

	@Transactional
	public PlaySessionDto start(StartSessionRequest req) {
		User u = userRepository.findById(req.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
		Beatmap b = beatmapRepository.findById(req.getBeatmapId()).orElseThrow(() -> new NotFoundException("Beatmap not found"));

		PlaySession s = new PlaySession();
		s.setUser(u);
		s.setBeatmap(b);
		PlaySession saved = playSessionRepository.save(s);
		return toDto(saved);
	}

	@Transactional
	public PlaySessionDto end(Integer sessionId, LocalDateTime endedAt) {
		PlaySession s = playSessionRepository.findById(sessionId).orElseThrow(() -> new NotFoundException("Session not found"));
		s.setEndedAt(endedAt != null ? endedAt : LocalDateTime.now());
		return toDto(playSessionRepository.save(s));
	}

	@Transactional(readOnly = true)
	public List<PlaySessionDto> historyByUser(Integer userId) {
		return playSessionRepository.findByUserUserIdOrderByStartedAtDesc(userId).stream().map(PlaySessionService::toDto).toList();
	}

	private static PlaySessionDto toDto(PlaySession s) {
		return new PlaySessionDto(
				s.getSessionId(),
				s.getUser() != null ? s.getUser().getUserId() : null,
				s.getBeatmap() != null ? s.getBeatmap().getBeatmapId() : null,
				s.getStartedAt(),
				s.getEndedAt());
	}
}

