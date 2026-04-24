package com.signalrhythm.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.signalrhythm.backend.dto.game.ScoreDto;
import com.signalrhythm.backend.dto.game.SubmitScoreRequest;
import com.signalrhythm.backend.entity.Beatmap;
import com.signalrhythm.backend.entity.PlaySession;
import com.signalrhythm.backend.entity.Score;
import com.signalrhythm.backend.entity.User;
import com.signalrhythm.backend.exception.BadRequestException;
import com.signalrhythm.backend.exception.NotFoundException;
import com.signalrhythm.backend.repository.BeatmapRepository;
import com.signalrhythm.backend.repository.PlaySessionRepository;
import com.signalrhythm.backend.repository.ScoreRepository;
import com.signalrhythm.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreService {
	private final ScoreRepository scoreRepository;
	private final PlaySessionRepository playSessionRepository;
	private final UserRepository userRepository;
	private final BeatmapRepository beatmapRepository;

	@Transactional
	public ScoreDto submit(SubmitScoreRequest req) {
		PlaySession session = playSessionRepository.findById(req.getSessionId())
				.orElseThrow(() -> new NotFoundException("Session not found"));
		User user = userRepository.findById(req.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
		Beatmap beatmap = beatmapRepository.findById(req.getBeatmapId()).orElseThrow(() -> new NotFoundException("Beatmap not found"));

		if (session.getUser() == null || !session.getUser().getUserId().equals(user.getUserId())) {
			throw new BadRequestException("Session does not belong to user");
		}
		if (session.getBeatmap() == null || !session.getBeatmap().getBeatmapId().equals(beatmap.getBeatmapId())) {
			throw new BadRequestException("Session does not belong to beatmap");
		}

		Score s = new Score();
		s.setSession(session);
		s.setUser(user);
		s.setBeatmap(beatmap);
		s.setScore(req.getScore());
		s.setMaxCombo(req.getMaxCombo());
		s.setPerfectCount(req.getPerfectCount());
		s.setGoodCount(req.getGoodCount());
		s.setFailCount(req.getFailCount());
		s.setAccuracy(req.getAccuracy());

		return toDto(scoreRepository.save(s));
	}

	@Transactional(readOnly = true)
	public List<ScoreDto> leaderboard(Integer beatmapId) {
		return scoreRepository.findTop10ByBeatmapBeatmapIdOrderByScoreDescAccuracyDesc(beatmapId).stream()
				.map(ScoreService::toDto)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<ScoreDto> historyByUser(Integer userId) {
		return scoreRepository.findByUserUserIdOrderByPlayedAtDesc(userId).stream().map(ScoreService::toDto).toList();
	}

	private static ScoreDto toDto(Score s) {
		return new ScoreDto(
				s.getScoreId(),
				s.getSession() != null ? s.getSession().getSessionId() : null,
				s.getUser() != null ? s.getUser().getUserId() : null,
				s.getBeatmap() != null ? s.getBeatmap().getBeatmapId() : null,
				s.getScore(),
				s.getMaxCombo(),
				s.getPerfectCount(),
				s.getGoodCount(),
				s.getFailCount(),
				s.getAccuracy(),
				s.getPlayedAt());
	}
}

