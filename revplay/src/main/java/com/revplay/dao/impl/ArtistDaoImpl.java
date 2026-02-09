package com.revplay.dao.impl;

import com.revplay.dao.ArtistDao;
import com.revplay.model.Artist;
import com.revplay.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ArtistDaoImpl implements ArtistDao {

    private static final Logger logger =
            LogManager.getLogger(ArtistDaoImpl.class);

    // ================= GET BY USER ID =================
    @Override
    public Artist getByUserId(int userId) throws Exception {

        logger.info("Fetching artist profile for userId: {}", userId);

        String sql = "SELECT * FROM artist_profile WHERE user_id = ?";
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Artist artist = new Artist();
            artist.setArtistId(rs.getInt("artist_id"));
            artist.setUserId(rs.getInt("user_id"));
            artist.setArtistName(rs.getString("artist_name"));
            artist.setBio(rs.getString("bio"));
            artist.setPrimaryGenre(rs.getString("primary_genre"));
            artist.setSocialLinks(rs.getString("social_links"));
            con.close();

            logger.info("Artist profile found for userId: {}", userId);
            return artist;
        }

        con.close();
        logger.info("No artist profile found for userId: {}", userId);
        return null;
    }

    // ================= CREATE PROFILE =================
    @Override
    public void createProfile(Artist artist) throws Exception {

        logger.info("Creating artist profile for userId: {}", artist.getUserId());

        String sql = """
            INSERT INTO artist_profile
            (artist_id, user_id, artist_name, bio, primary_genre, social_links)
            VALUES (artist_seq.NEXTVAL, ?, ?, ?, ?, ?)
        """;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, artist.getUserId());
        ps.setString(2, artist.getArtistName());
        ps.setString(3, artist.getBio());
        ps.setString(4, artist.getPrimaryGenre());
        ps.setString(5, artist.getSocialLinks());

        ps.executeUpdate();
        con.close();

        logger.info("Artist profile created successfully for userId: {}", artist.getUserId());
    }

    // ================= UPDATE PROFILE =================
    @Override
    public void updateProfile(Artist artist) throws Exception {

        logger.info("Updating artist profile for userId: {}", artist.getUserId());

        String sql = """
            UPDATE artist_profile
            SET artist_name = ?, bio = ?, primary_genre = ?, social_links = ?
            WHERE user_id = ?
        """;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, artist.getArtistName());
        ps.setString(2, artist.getBio());
        ps.setString(3, artist.getPrimaryGenre());
        ps.setString(4, artist.getSocialLinks());
        ps.setInt(5, artist.getUserId());

        ps.executeUpdate();
        con.close();

        logger.info("Artist profile updated for userId: {}", artist.getUserId());
    }

    // ================= GET ALL ARTISTS =================
    @Override
    public List<Artist> getAllArtists() throws Exception {

        logger.info("Fetching all artist profiles");

        List<Artist> list = new ArrayList<>();

        String sql = "SELECT * FROM artist_profile";
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Artist artist = new Artist();
            artist.setArtistId(rs.getInt("artist_id"));
            artist.setUserId(rs.getInt("user_id"));
            artist.setArtistName(rs.getString("artist_name"));
            artist.setBio(rs.getString("bio"));
            artist.setPrimaryGenre(rs.getString("primary_genre"));
            artist.setSocialLinks(rs.getString("social_links"));
            list.add(artist);
        }

        con.close();
        logger.info("Fetched {} artist profiles", list.size());

        return list;
    }
}