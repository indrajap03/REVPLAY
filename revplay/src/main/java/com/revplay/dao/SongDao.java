package com.revplay.dao;

import com.revplay.model.Song;
import java.util.List;

public interface SongDao {

    void addSong(Song song) throws Exception;

    List<Song> getAllSongs() throws Exception;

    Song getSongById(int songId) throws Exception;

    List<Song> getSongsByArtist(int artistId) throws Exception;

    void incrementPlayCount(int songId) throws Exception;

    // NEW
    void updateSong(Song song) throws Exception;

    void deleteSong(int songId) throws Exception;
}