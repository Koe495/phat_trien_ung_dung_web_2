package com.signalrhythm.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.signalrhythm.backend.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Integer> {
	List<Note> findByBeatmapBeatmapIdOrderByTimeMsAsc(Integer beatmapId);
}
