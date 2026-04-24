package com.signalrhythm.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.signalrhythm.backend.entity.PlaySession;

public interface PlaySessionRepository extends JpaRepository<PlaySession, Integer> {
	List<PlaySession> findByUserUserIdOrderByStartedAtDesc(Integer userId);

	List<PlaySession> findByBeatmapBeatmapIdOrderByStartedAtDesc(Integer beatmapId);
}
