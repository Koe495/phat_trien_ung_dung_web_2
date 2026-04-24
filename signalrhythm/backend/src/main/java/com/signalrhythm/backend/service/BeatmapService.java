package com.signalrhythm.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.signalrhythm.backend.dto.game.BeatmapDto;
import com.signalrhythm.backend.entity.Beatmap;
import com.signalrhythm.backend.exception.NotFoundException;
import com.signalrhythm.backend.repository.BeatmapRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeatmapService {
	private final BeatmapRepository beatmapRepository;

	@Transactional(readOnly = true)
	public BeatmapDto getBeatmap(Integer beatmapId) {
		Beatmap b = beatmapRepository.findById(beatmapId).orElseThrow(() -> new NotFoundException("Beatmap not found"));
		return toDto(b);
	}

	@Transactional(readOnly = true)
	public List<BeatmapDto> listBeatmapsBySong(Integer songId) {
		return beatmapRepository.findBySongSongIdOrderByLevelAsc(songId).stream().map(BeatmapService::toDto).toList();
	}

	@Transactional(readOnly = true)
	public BeatmapDto getBeatmapBySongAndDifficulty(Integer songId, String difficulty) {
		Beatmap b = beatmapRepository.findBySongSongIdAndDifficultyIgnoreCase(songId, difficulty)
				.orElseThrow(() -> new NotFoundException("Beatmap not found"));
		return toDto(b);
	}

	private static BeatmapDto toDto(Beatmap b) {
		return new BeatmapDto(
				b.getBeatmapId(),
				b.getSong().getSongId(),
				b.getDifficulty(),
				b.getLevel(),
				b.getNoteCount(),
				b.getCreatedAt());
	}
}

