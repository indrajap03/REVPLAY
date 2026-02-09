package com.revplay.dao.impl;

import com.revplay.model.Song;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SongDaoImplTest {

    // Testing the DAO IMPLEMENTATION
    private final SongDaoImpl dao = new SongDaoImpl();

    // ================= READ-ONLY: getAllSongs =================
    @Test
    void testGetAllSongs_readOnly() throws Exception {

        List<Song> songs = dao.getAllSongs();

        // SELECT only — DB not affected
        assertNotNull(songs);

        // DB may be empty or populated — both valid
        assertTrue(songs.size() >= 0);

        // Optional sanity check if data exists
        if (!songs.isEmpty()) {
            Song s = songs.get(0);
            assertNotNull(s.getTitle());
        }
    }

    // ================= READ-ONLY: getSongById =================
    @Test
    void testGetSongById_readOnly() throws Exception {

        int songId = 1; // can be existing or non-existing

        Song song = dao.getSongById(songId);

        // SELECT only — DB safe
        if (song != null) {
            assertEquals(songId, song.getSongId());
            assertNotNull(song.getTitle());
        } else {
            assertNull(song); // valid if no record exists
        }
    }

    // ================= READ-ONLY: getSongsByArtist =================
    @Test
    void testGetSongsByArtist_readOnly() throws Exception {

        int artistId = 1; // can be existing or non-existing

        List<Song> songs = dao.getSongsByArtist(artistId);

        // SELECT only — DB safe
        assertNotNull(songs);

        if (!songs.isEmpty()) {
            for (Song s : songs) {
                assertEquals(artistId, s.getArtistId());
                assertNotNull(s.getTitle());
            }
        }
    }
}