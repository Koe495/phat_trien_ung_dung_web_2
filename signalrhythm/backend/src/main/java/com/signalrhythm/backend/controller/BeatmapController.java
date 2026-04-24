package com.signalrhythm.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.signalrhythm.backend.dto.game.BeatmapDto;
import com.signalrhythm.backend.dto.game.NoteDto;
import com.signalrhythm.backend.service.BeatmapService;
import com.signalrhythm.backend.service.NoteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/beatmaps")
@RequiredArgsConstructor
public class BeatmapController {
	private final BeatmapService beatmapService;
	private final NoteService noteService;

	@GetMapping("/{beatmapId}")
	public BeatmapDto get(@PathVariable Integer beatmapId) {
		return beatmapService.getBeatmap(beatmapId);
	}

	@GetMapping("/{beatmapId}/notes")
	public List<NoteDto> notes(@PathVariable Integer beatmapId) {
		return noteService.listNotesByBeatmap(beatmapId);
	}
}

