package com.revplay.dao.impl;

import com.revplay.dao.SongDao;
import com.revplay.model.Song;
import com.revplay.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDaoImpl implements SongDao {

    private static final Logger logger =
            LogManager.getLogger(SongDaoImpl.class);

    @Override
    public void addSong(Song song) throws Exception {

        logger.info("Adding song: title='{}', artistId={}", song.getTitle(), song.getArtistId());

        String sql = """
            INSERT INTO songs
            (song_id, title, album_id, genre_id, artist_id, duration, play_count)
            VALUES (song_seq.NEXTVAL, ?, ?, ?, ?, ?, 0)
        """;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, song.getTitle());

        if (song.getAlbumId() == 0)
            ps.setNull(2, Types.INTEGER);
        else
            ps.setInt(2, song.getAlbumId());

        ps.setInt(3, song.getGenreId());
        ps.setInt(4, song.getArtistId());
        ps.setInt(5, song.getDuration());

        ps.executeUpdate();
        con.close();

        logger.info("Song '{}' added successfully", song.getTitle());
    }

    @Override
    public List<Song> getAllSongs() throws Exception {

        logger.info("Fetching all songs");

        List<Song> songs = new ArrayList<>();

        String sql = "SELECT * FROM songs";
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Song s = mapSong(rs);
            songs.add(s);
        }

        con.close();
        logger.info("Fetched {} songs", songs.size());

        return songs;
    }

    @Override
    public Song getSongById(int songId) throws Exception {

        logger.info("Fetching song by songId={}", songId);

        String sql = "SELECT * FROM songs WHERE song_id = ?";
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, songId);

        ResultSet rs = ps.executeQuery();

        Song s = null;
        if (rs.next()) {
            s = mapSong(rs);
        }

        con.close();
        logger.info("Song found={} for songId={}", s != null, songId);

        return s;
    }

    @Override
    public List<Song> getSongsByArtist(int artistId) throws Exception {

        logger.info("Fetching songs for artistId={}", artistId);

        List<Song> songs = new ArrayList<>();

        String sql = "SELECT * FROM songs WHERE artist_id = ?";
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, artistId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            songs.add(mapSong(rs));
        }

        con.close();
        logger.info("Fetched {} songs for artistId={}", songs.size(), artistId);

        return songs;
    }

    @Override
    public void incrementPlayCount(int songId) throws Exception {

        logger.info("Incrementing play count for songId={}", songId);

        String sql = "UPDATE songs SET play_count = play_count + 1 WHERE song_id = ?";
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, songId);
        ps.executeUpdate();
        con.close();

        logger.info("Play count incremented for songId={}", songId);
    }

    // ✅ UPDATE SONG
    @Override
    public void updateSong(Song song) throws Exception {

        logger.info("Updating songId={}", song.getSongId());

        String sql = """
            UPDATE songs
            SET title = ?, genre_id = ?, duration = ?
            WHERE song_id = ?
        """;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, song.getTitle());
        ps.setInt(2, song.getGenreId());
        ps.setInt(3, song.getDuration());
        ps.setInt(4, song.getSongId());

        ps.executeUpdate();
        con.close();

        logger.info("SongId={} updated successfully", song.getSongId());
    }

    // ✅ DELETE SONG
    @Override
    public void deleteSong(int songId) throws Exception {

        logger.info("Deleting songId={}", songId);

        String sql = "DELETE FROM songs WHERE song_id = ?";
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, songId);
        ps.executeUpdate();
        con.close();

        logger.info("SongId={} deleted successfully", songId);
    }

    private Song mapSong(ResultSet rs) throws SQLException {
        Song s = new Song();
        s.setSongId(rs.getInt("song_id"));
        s.setTitle(rs.getString("title"));
        s.setAlbumId(rs.getInt("album_id"));
        s.setGenreId(rs.getInt("genre_id"));
        s.setArtistId(rs.getInt("artist_id"));
        s.setDuration(rs.getInt("duration"));
        s.setPlayCount(rs.getInt("play_count"));
        return s;
    }
}