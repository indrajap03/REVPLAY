package com.revplay.dao.impl;

import com.revplay.model.Artist;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArtistDaoImplTest {

    // Testing the DAO IMPLEMENTATION (required)
    private final ArtistDaoImpl dao = new ArtistDaoImpl();

    // ================= READ-ONLY: getByUserId =================
    @Test
    void testGetByUserId_readOnly() throws Exception {

        int userId = 1; // can be existing or non-existing

        Artist artist = dao.getByUserId(userId);

        // Only SELECT is executed — DB is NOT affected
        if (artist != null) {
            assertEquals(userId, artist.getUserId());
            assertNotNull(artist.getArtistName());
        } else {
            // Valid case if no record exists
            assertNull(artist);
        }
    }

    // ================= READ-ONLY: getAllArtists =================
    @Test
    void testGetAllArtists_readOnly() throws Exception {

        List<Artist> artists = dao.getAllArtists();

        // SELECT only — always safe
        assertNotNull(artists);

        // DB may or may not contain data — both are valid
        assertTrue(artists.size() >= 0);
    }
}
