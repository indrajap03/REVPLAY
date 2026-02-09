package com.revplay.service;

import com.revplay.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BrowseService {

    private static final Logger logger =
            LogManager.getLogger(BrowseService.class);

    // -------- BROWSE BY GENRE --------
    public void browseByGenre() throws Exception {

        logger.info("Browse by genre started");

        Connection con = DBUtil.getConnection();

        String sql = """
            SELECT g.genre_name, s.song_id, s.title
            FROM songs s
            JOIN genres g ON s.genre_id = g.genre_id
            ORDER BY g.genre_name
        """;

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        String currentGenre = "";

        System.out.println("\n=== BROWSE BY GENRE ===");

        while (rs.next()) {
            String genre = rs.getString("genre_name");

            if (!genre.equals(currentGenre)) {
                currentGenre = genre;
                System.out.println("\n-- " + genre + " --");
                logger.info("Browsing genre: {}", genre);
            }

            System.out.println(
                    rs.getInt("song_id") + " | " +
                            rs.getString("title")
            );
        }

        con.close();
        logger.info("Browse by genre completed");
    }

    // -------- BROWSE BY ARTIST --------
    public void browseByArtist() throws Exception {

        logger.info("Browse by artist started");

        Connection con = DBUtil.getConnection();

        String sql = """
            SELECT a.artist_name, s.song_id, s.title
            FROM songs s
            JOIN artist_profile a ON s.artist_id = a.artist_id
            ORDER BY a.artist_name
        """;

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        String currentArtist = "";

        System.out.println("\n=== BROWSE BY ARTIST ===");

        while (rs.next()) {
            String artist = rs.getString("artist_name");

            if (!artist.equals(currentArtist)) {
                currentArtist = artist;
                System.out.println("\n-- " + artist + " --");
                logger.info("Browsing artist: {}", artist);
            }

            System.out.println(
                    rs.getInt("song_id") + " | " +
                            rs.getString("title")
            );
        }

        con.close();
        logger.info("Browse by artist completed");
    }

    // -------- BROWSE BY ALBUM --------
    public void browseByAlbum() throws Exception {

        logger.info("Browse by album started");

        Connection con = DBUtil.getConnection();

        String sql = """
            SELECT al.album_title, s.song_id, s.title
            FROM songs s
            JOIN albums al ON s.album_id = al.album_id
            ORDER BY al.album_title
        """;

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        String currentAlbum = "";

        System.out.println("\n=== BROWSE BY ALBUM ===");

        while (rs.next()) {
            String album = rs.getString("album_title");

            if (!album.equals(currentAlbum)) {
                currentAlbum = album;
                System.out.println("\n-- " + album + " --");
                logger.info("Browsing album: {}", album);
            }

            System.out.println(
                    rs.getInt("song_id") + " | " +
                            rs.getString("title")
            );
        }

        con.close();
        logger.info("Browse by album completed");
    }
}