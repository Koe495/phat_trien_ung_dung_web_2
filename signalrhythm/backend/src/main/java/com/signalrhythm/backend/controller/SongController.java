package com.signalrhythm.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.signalrhythm.backend.dto.game.BeatmapDto;
import com.signalrhythm.backend.dto.game.SongDto;
import com.signalrhythm.backend.service.BeatmapService;
import com.signalrhythm.backend.service.SongService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {
	private final SongService songService;
	private final BeatmapService beatmapService;

	@GetMapping
	public List<SongDto> list(@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "artist", required = false) String artist) {
		if (title != null && !title.isBlank()) {
			return songService.searchByTitle(title);
		}
		if (artist != null && !artist.isBlank()) {
			return songService.searchByArtist(artist);
		}
		return songService.listSongs();
	}

	@GetMapping("/{songId}")
	public SongDto get(@PathVariable Integer songId) {
		return songService.getSong(songId);
	}

	@GetMapping("/{songId}/beatmaps")
	public List<BeatmapDto> beatmaps(@PathVariable Integer songId) {
		return beatmapService.listBeatmapsBySong(songId);
	}

	@GetMapping("/{songId}/beatmaps/by-difficulty")
	public BeatmapDto beatmapByDifficulty(@PathVariable Integer songId, @RequestParam("difficulty") String difficulty) {
		return beatmapService.getBeatmapBySongAndDifficulty(songId, difficulty);
	}
}

