package com.revplay.dao.impl;

import com.revplay.model.Album;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlbumDaoImplTest {

    // Testing the IMPLEMENTATION class
    private final AlbumDaoImpl dao = new AlbumDaoImpl();

    // ================= READ-ONLY: getAlbumsByArtist =================
    @Test
    void testGetAlbumsByArtist_readOnly() throws Exception {

        int artistId = 1; // can exist or not

        List<Album> albums = dao.getAlbumsByArtist(artistId);

        // SELECT only — DB not affected
        assertNotNull(albums);

        // If albums exist, validate mapping
        if (!albums.isEmpty()) {
            for (Album album : albums) {
                assertEquals(artistId, album.getArtistId());
                assertNotNull(album.getAlbumTitle());
            }
        }
    }

    // ================= READ-ONLY: getAllAlbums =================
    @Test
    void testGetAllAlbums_readOnly() throws Exception {

        List<Album> albums = dao.getAllAlbums();

        // SELECT only — always safe
        assertNotNull(albums);

        // DB may be empty or populated — both valid
        assertTrue(albums.size() >= 0);
    }
}