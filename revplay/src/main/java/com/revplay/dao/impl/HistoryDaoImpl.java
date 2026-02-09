package com.revplay.dao.impl;

import com.revplay.dao.HistoryDao;
import com.revplay.model.ListeningHistory;
import com.revplay.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryDaoImpl implements HistoryDao {

    private static final Logger logger =
            LogManager.getLogger(HistoryDaoImpl.class);

    @Override
    public void addHistory(int userId, int songId) throws Exception {

        logger.info("Adding listening history: userId={}, songId={}", userId, songId);

        String sql = """
            INSERT INTO listening_history (history_id, user_id, song_id, played_at)
            VALUES (history_seq.NEXTVAL, ?, ?, SYSDATE)
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        }

        logger.info("Listening history added successfully for userId={}, songId={}", userId, songId);
    }

    @Override
    public List<ListeningHistory> getHistoryByUser(int userId) throws Exception {

        logger.info("Fetching listening history for userId={}", userId);

        List<ListeningHistory> list = new ArrayList<>();

        String sql = """
            SELECT history_id, user_id, song_id, played_at
            FROM listening_history
            WHERE user_id = ?
            ORDER BY played_at DESC
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ListeningHistory h = new ListeningHistory();
                h.setHistoryId(rs.getInt("history_id"));
                h.setUserId(rs.getInt("user_id"));
                h.setSongId(rs.getInt("song_id"));
                h.setPlayedAt(rs.getTimestamp("played_at"));
                list.add(h);
            }
        }

        logger.info("Fetched {} history records for userId={}", list.size(), userId);
        return list;
    }

    // ✅ RECENTLY PLAYED
    @Override
    public List<ListeningHistory> getRecentHistory(int userId, int limit) throws Exception {

        logger.info("Fetching recent listening history for userId={}, limit={}", userId, limit);

        List<ListeningHistory> list = new ArrayList<>();

        String sql = """
            SELECT *
            FROM (
                SELECT history_id, user_id, song_id, played_at
                FROM listening_history
                WHERE user_id = ?
                ORDER BY played_at DESC
            )
            WHERE ROWNUM <= ?
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ListeningHistory h = new ListeningHistory();
                h.setSongId(rs.getInt("song_id"));
                h.setPlayedAt(rs.getTimestamp("played_at"));
                list.add(h);
            }
        }

        logger.info("Fetched {} recent history records for userId={}", list.size(), userId);
        return list;
    }
}