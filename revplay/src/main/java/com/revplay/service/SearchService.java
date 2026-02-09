package com.revplay.service;

import com.revplay.dao.*;
import com.revplay.dao.impl.*;
import com.revplay.model.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SearchService {

    private static final Logger logger =
            LogManager.getLogger(SearchService.class);

    private final SongDao songDao = new SongDaoImpl();
    private final ArtistDao artistDao = new ArtistDaoImpl();
    private final AlbumDaoImpl albumDao = new AlbumDaoImpl();
    private final PlaylistDao playlistDao = new PlaylistDaoImpl();

    // CALLED FROM RevPlayApplication
    public void search(String keyword) throws Exception {

        logger.info("Search started with keyword='{}'", keyword);

        boolean found = false;
        keyword = keyword.toLowerCase();

        System.out.println("\n=== SEARCH RESULTS ===");

        // -------- SONGS --------
        List<Song> songs = songDao.getAllSongs();
        for (Song s : songs) {
            if (s.getTitle().toLowerCase().contains(keyword)) {
                System.out.println("Song: " + s.getTitle());
                logger.info("Song match found: {}", s.getTitle());
                found = true;
            }
        }

        // -------- ARTISTS --------
        List<Artist> artists = artistDao.getAllArtists();
        for (Artist a : artists) {
            if (a.getArtistName().toLowerCase().contains(keyword)) {
                System.out.println("Artist: " + a.getArtistName());
                logger.info("Artist match found: {}", a.getArtistName());
                found = true;
            }
        }

        // -------- ALBUMS --------
        List<Album> albums = albumDao.getAllAlbums();
        for (Album a : albums) {
            if (a.getAlbumTitle().toLowerCase().contains(keyword)) {
                System.out.println("Album: " + a.getAlbumTitle());
                logger.info("Album match found: {}", a.getAlbumTitle());
                found = true;
            }
        }

        // -------- PLAYLISTS (PUBLIC ONLY) --------
        List<Playlist> playlists = playlistDao.getPublicPlaylists();
        for (Playlist p : playlists) {
            if (p.getName().toLowerCase().contains(keyword)) {
                System.out.println(" Playlist: " + p.getName());
                logger.info("Playlist match found: {}", p.getName());
                found = true;
            }
        }

        if (!found) {
            System.out.println(" NO MATCH FOUND");
            logger.info("No matches found for keyword='{}'", keyword);
        }

        logger.info("Search completed for keyword='{}'", keyword);
    }
}