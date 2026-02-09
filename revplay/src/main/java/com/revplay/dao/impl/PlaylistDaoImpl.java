package com.revplay.dao.impl;

import com.revplay.dao.PlaylistDao;
import com.revplay.model.Playlist;
import com.revplay.model.Song;
import com.revplay.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDaoImpl implements PlaylistDao {

    private static final Logger logger =
            LogManager.getLogger(PlaylistDaoImpl.class);

    @Override
    public void createPlaylist(int userId, String name, String description, String privacy) throws Exception {

        logger.info("Creating playlist '{}' for userId={}", name, userId);

        String sql = """
            INSERT INTO playlists
            (playlist_id, user_id, name, description, is_public)
            VALUES (playlist_seq.NEXTVAL, ?, ?, ?, ?)
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, description);
            ps.setString(4, privacy.equalsIgnoreCase("PUBLIC") ? "Y" : "N");
            ps.executeUpdate();
        }

        logger.info("Playlist '{}' created successfully for userId={}", name, userId);
    }

    @Override
    public List<Playlist> getPlaylistsByUser(int userId) throws Exception {

        logger.info("Fetching playlists for userId={}", userId);

        List<Playlist> list = new ArrayList<>();
        String sql = "SELECT * FROM playlists WHERE user_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Playlist p = new Playlist();
                p.setPlaylistId(rs.getInt("playlist_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrivacy(rs.getString("is_public").equals("Y") ? "PUBLIC" : "PRIVATE");
                list.add(p);
            }
        }

        logger.info("Fetched {} playlists for userId={}", list.size(), userId);
        return list;
    }

    // ✅ CHECK IF SONG ALREADY EXISTS
    @Override
    public boolean songExistsInPlaylist(int playlistId, int songId) throws Exception {

        logger.info("Checking if songId={} exists in playlistId={}", songId, playlistId);

        String sql = """
            SELECT 1 FROM playlist_songs
            WHERE playlist_id = ? AND song_id = ?
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();

            logger.info("Song exists={} for playlistId={}, songId={}", exists, playlistId, songId);
            return exists;
        }
    }

    // ✅ SAFE ADD
    @Override
    public void addSongToPlaylist(int playlistId, int songId) throws Exception {

        logger.info("Adding songId={} to playlistId={}", songId, playlistId);

        String sql = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        }

        logger.info("SongId={} added to playlistId={}", songId, playlistId);
    }

    @Override
    public void removeSongFromPlaylist(int playlistId, int songId) throws Exception {

        logger.info("Removing songId={} from playlistId={}", songId, playlistId);

        String sql = "DELETE FROM playlist_songs WHERE playlist_id = ? AND song_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        }

        logger.info("SongId={} removed from playlistId={}", songId, playlistId);
    }

    // ✅ USER-OWNED PLAYLIST SONGS ONLY
    @Override
    public List<Song> getSongsInPlaylist(int playlistId, int userId) throws Exception {

        logger.info("Fetching songs for playlistId={} owned by userId={}", playlistId, userId);

        List<Song> songs = new ArrayList<>();

        String sql = """
            SELECT s.*
            FROM playlists p
            JOIN playlist_songs ps ON p.playlist_id = ps.playlist_id
            JOIN songs s ON ps.song_id = s.song_id
            WHERE p.playlist_id = ? AND p.user_id = ?
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song s = new Song();
                s.setSongId(rs.getInt("song_id"));
                s.setTitle(rs.getString("title"));
                s.setDuration(rs.getInt("duration"));
                songs.add(s);
            }
        }

        logger.info("Fetched {} songs for playlistId={}", songs.size(), playlistId);
        return songs;
    }

    @Override
    public void updatePlaylist(int playlistId, String name, String description, String privacy) throws Exception {

        logger.info("Updating playlistId={}", playlistId);

        String sql = """
            UPDATE playlists
            SET name = ?, description = ?, is_public = ?
            WHERE playlist_id = ?
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, privacy.equalsIgnoreCase("PUBLIC") ? "Y" : "N");
            ps.setInt(4, playlistId);
            ps.executeUpdate();
        }

        logger.info("PlaylistId={} updated successfully", playlistId);
    }

    @Override
    public void deletePlaylist(int playlistId, int userId) throws Exception {

        logger.info("Deleting playlistId={} for userId={}", playlistId, userId);

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps1 =
                    con.prepareStatement("DELETE FROM playlist_songs WHERE playlist_id = ?");
            ps1.setInt(1, playlistId);
            ps1.executeUpdate();

            PreparedStatement ps2 =
                    con.prepareStatement("DELETE FROM playlists WHERE playlist_id = ? AND user_id = ?");
            ps2.setInt(1, playlistId);
            ps2.setInt(2, userId);
            ps2.executeUpdate();
        }

        logger.info("PlaylistId={} deleted for userId={}", playlistId, userId);
    }

    @Override
    public List<Playlist> getPublicPlaylists() throws Exception {

        logger.info("Fetching public playlists");

        List<Playlist> list = new ArrayList<>();
        String sql = "SELECT * FROM playlists WHERE is_public = 'Y'";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Playlist p = new Playlist();
                p.setPlaylistId(rs.getInt("playlist_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrivacy("PUBLIC");
                list.add(p);
            }
        }

        logger.info("Fetched {} public playlists", list.size());
        return list;
    }
}