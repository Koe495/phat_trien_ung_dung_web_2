package com.signalrhythm.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.signalrhythm.backend.dto.game.NoteDto;
import com.signalrhythm.backend.entity.Note;
import com.signalrhythm.backend.repository.NoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoteService {
	private final NoteRepository noteRepository;

	@Transactional(readOnly = true)
	public List<NoteDto> listNotesByBeatmap(Integer beatmapId) {
		return noteRepository.findByBeatmapBeatmapIdOrderByTimeMsAsc(beatmapId).stream().map(NoteService::toDto).toList();
	}

	private static NoteDto toDto(Note n) {
		return new NoteDto(
				n.getNoteId(),
				n.getBeatmap().getBeatmapId(),
				n.getTimeMs(),
				n.getLaneLeft(),
				n.getLaneRight(),
				n.getCenterChar(),
				n.getNoteType(),
				n.getDurationMs());
	}
}

