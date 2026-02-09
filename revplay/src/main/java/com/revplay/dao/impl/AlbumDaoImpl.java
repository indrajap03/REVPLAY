package com.revplay.dao.impl;

import com.revplay.dao.AlbumDao;
import com.revplay.model.Album;
import com.revplay.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDaoImpl implements AlbumDao {

    private static final Logger logger =
            LogManager.getLogger(AlbumDaoImpl.class);

    // ---------------- CREATE ALBUM ----------------
    @Override
    public void createAlbum(Album album) throws Exception {

        logger.info("Creating album: {}", album.getAlbumTitle());

        String sql = """
            INSERT INTO albums
            (album_id, artist_id, album_title, release_date)
            VALUES (album_seq.NEXTVAL, ?, ?, ?)
        """;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, album.getArtistId());
        ps.setString(2, album.getAlbumTitle());
        ps.setDate(3, album.getReleaseDate());

        ps.executeUpdate();
        con.close();

        logger.info("Album created successfully");
    }

    // ---------------- GET ALBUMS BY ARTIST ----------------
    @Override
    public List<Album> getAlbumsByArtist(int artistId) throws Exception {

        logger.info("Fetching albums for artistId: {}", artistId);

        List<Album> list = new ArrayList<>();

        String sql = "SELECT * FROM albums WHERE artist_id = ?";
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, artistId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Album a = new Album();
            a.setAlbumId(rs.getInt("album_id"));
            a.setArtistId(rs.getInt("artist_id"));
            a.setAlbumTitle(rs.getString("album_title"));
            a.setReleaseDate(rs.getDate("release_date"));
            list.add(a);
        }

        con.close();
        logger.info("Fetched {} albums for artistId {}", list.size(), artistId);

        return list;
    }

    // ---------------- ADD SONG TO ALBUM ----------------
    @Override
    public void addSongToAlbum(int songId, int albumId) throws Exception {

        logger.info("Adding songId {} to albumId {}", songId, albumId);

        String sql = "UPDATE songs SET album_id = ? WHERE song_id = ?";

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, albumId);
        ps.setInt(2, songId);

        ps.executeUpdate();
        con.close();

        logger.info("Song {} added to album {}", songId, albumId);
    }

    // ---------------- GET ALL ALBUMS ----------------
    @Override
    public List<Album> getAllAlbums() throws Exception {

        logger.info("Fetching all albums");

        List<Album> list = new ArrayList<>();

        String sql = "SELECT * FROM albums";
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Album a = new Album();
            a.setAlbumId(rs.getInt("album_id"));
            a.setArtistId(rs.getInt("artist_id"));
            a.setAlbumTitle(rs.getString("album_title"));
            a.setReleaseDate(rs.getDate("release_date"));
            list.add(a);
        }

        con.close();
        logger.info("Fetched {} albums", list.size());

        return list;
    }
}