package com.signalrhythm.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.signalrhythm.backend.entity.Song;

public interface SongRepository extends JpaRepository<Song, Integer> {
	List<Song> findAllByOrderByCreatedAtDesc();

	List<Song> findByTitleContainingIgnoreCaseOrderByTitleAsc(String title);

	List<Song> findByArtistContainingIgnoreCaseOrderByArtistAsc(String artist);
}
