package com.signalrhythm.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.signalrhythm.backend.entity.Score;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
	List<Score> findTop10ByBeatmapBeatmapIdOrderByScoreDescAccuracyDesc(Integer beatmapId);

	List<Score> findByUserUserIdOrderByPlayedAtDesc(Integer userId);

	Optional<Score> findTop1ByUserUserIdAndBeatmapBeatmapIdOrderByPlayedAtDesc(Integer userId, Integer beatmapId);
}
