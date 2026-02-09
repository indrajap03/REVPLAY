package com.revplay.dao;

import com.revplay.model.Playlist;
import com.revplay.model.Song;
import java.util.List;

public interface PlaylistDao {

    void createPlaylist(int userId, String name, String description, String privacy) throws Exception;

    List<Playlist> getPlaylistsByUser(int userId) throws Exception;

    void addSongToPlaylist(int playlistId, int songId) throws Exception;

    void removeSongFromPlaylist(int playlistId, int songId) throws Exception;

    List<Song> getSongsInPlaylist(int playlistId, int userId) throws Exception;

    boolean songExistsInPlaylist(int playlistId, int songId) throws Exception;

    void updatePlaylist(int playlistId, String name, String description, String privacy) throws Exception;

    void deletePlaylist(int playlistId, int userId) throws Exception;

    List<Playlist> getPublicPlaylists() throws Exception;
}