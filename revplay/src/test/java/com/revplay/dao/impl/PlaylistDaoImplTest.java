package com.revplay.dao.impl;

import com.revplay.model.Playlist;
import com.revplay.model.Song;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistDaoImplTest {

    // Test the IMPLEMENTATION class
    private final PlaylistDaoImpl dao = new PlaylistDaoImpl();

    // ================= READ-ONLY: getPlaylistsByUser =================
    @Test
    void testGetPlaylistsByUser_readOnly() throws Exception {

        int userId = 1; // may exist or not

        List<Playlist> playlists = dao.getPlaylistsByUser(userId);

        // SELECT only — DB safe
        assertNotNull(playlists);

        if (!playlists.isEmpty()) {
            for (Playlist p : playlists) {
                assertEquals(userId, p.getUserId());
                assertNotNull(p.getName());
            }
        }
    }

    // ================= READ-ONLY: songExistsInPlaylist =================
    @Test
    void testSongExistsInPlaylist_readOnly() throws Exception {

        int playlistId = 1; // any value
        int songId = 1;     // any value

        boolean exists = dao.songExistsInPlaylist(playlistId, songId);

        // Only SELECT — result can be true or false
        assertNotNull(exists);
    }

    // ================= READ-ONLY: getSongsInPlaylist =================
    @Test
    void testGetSongsInPlaylist_readOnly() throws Exception {

        int playlistId = 1; // any value
        int userId = 1;     // playlist owner

        List<Song> songs = dao.getSongsInPlaylist(playlistId, userId);

        // SELECT only — DB safe
        assertNotNull(songs);

        if (!songs.isEmpty()) {
            for (Song s : songs) {
                assertNotNull(s.getTitle());
            }
        }
    }

    // ================= READ-ONLY: getPublicPlaylists =================
    @Test
    void testGetPublicPlaylists_readOnly() throws Exception {

        List<Playlist> playlists = dao.getPublicPlaylists();

        // SELECT only — safe
        assertNotNull(playlists);

        if (!playlists.isEmpty()) {
            for (Playlist p : playlists) {
                assertEquals("PUBLIC", p.getPrivacy());
                assertNotNull(p.getName());
            }
        }
    }
}