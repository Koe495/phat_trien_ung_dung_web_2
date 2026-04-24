package com.signalrhythm.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.signalrhythm.backend.entity.Beatmap;

public interface BeatmapRepository extends JpaRepository<Beatmap, Integer> {
	List<Beatmap> findBySongSongIdOrderByLevelAsc(Integer songId);

	Optional<Beatmap> findBySongSongIdAndDifficultyIgnoreCase(Integer songId, String difficulty);
}
