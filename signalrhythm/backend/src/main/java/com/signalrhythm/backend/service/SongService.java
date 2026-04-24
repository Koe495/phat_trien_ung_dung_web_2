package com.signalrhythm.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.signalrhythm.backend.dto.game.SongDto;
import com.signalrhythm.backend.entity.Song;
import com.signalrhythm.backend.exception.NotFoundException;
import com.signalrhythm.backend.repository.SongRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SongService {
	private final SongRepository songRepository;

	@Transactional(readOnly = true)
	public List<SongDto> listSongs() {
		return songRepository.findAllByOrderByCreatedAtDesc().stream().map(SongService::toDto).toList();
	}

	@Transactional(readOnly = true)
	public SongDto getSong(Integer songId) {
		Song s = songRepository.findById(songId).orElseThrow(() -> new NotFoundException("Song not found"));
		return toDto(s);
	}

	@Transactional(readOnly = true)
	public List<SongDto> searchByTitle(String q) {
		return songRepository.findByTitleContainingIgnoreCaseOrderByTitleAsc(q).stream().map(SongService::toDto).toList();
	}

	@Transactional(readOnly = true)
	public List<SongDto> searchByArtist(String q) {
		return songRepository.findByArtistContainingIgnoreCaseOrderByArtistAsc(q).stream().map(SongService::toDto).toList();
	}

	private static SongDto toDto(Song s) {
		return new SongDto(
				s.getSongId(),
				s.getTitle(),
				s.getArtist(),
				s.getBpm(),
				s.getOffsetMs(),
				s.getAudioPath(),
				s.getBackgroundPath(),
				s.getCreatedAt());
	}
}

