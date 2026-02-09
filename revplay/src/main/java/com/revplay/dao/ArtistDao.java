package com.revplay.dao;

import com.revplay.model.Artist;
import java.util.List;

public interface ArtistDao {

    // Get artist profile using logged-in user id
    Artist getByUserId(int userId) throws Exception;

    // Create new artist profile
    void createProfile(Artist artist) throws Exception;

    // Update existing artist profile
    void updateProfile(Artist artist) throws Exception;

    // Used for search & browse
    List<Artist> getAllArtists() throws Exception;
}