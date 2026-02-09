package com.revplay.dao.impl;

import com.revplay.dao.FavoriteDao;
import com.revplay.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FavoriteDaoImpl implements FavoriteDao {

    private static final Logger logger =
            LogManager.getLogger(FavoriteDaoImpl.class);

    @Override
    public void addFavorite(int userId, int songId) throws Exception {

        logger.info("Adding favorite: userId={}, songId={}", userId, songId);

        String sql = "INSERT INTO favorites (user_id, song_id) VALUES (?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        }

        logger.info("Favorite added successfully for userId={}, songId={}", userId, songId);
    }

    @Override
    public void removeFavorite(int userId, int songId) throws Exception {

        logger.info("Removing favorite: userId={}, songId={}", userId, songId);

        String sql = "DELETE FROM favorites WHERE user_id = ? AND song_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        }

        logger.info("Favorite removed successfully for userId={}, songId={}", userId, songId);
    }

    @Override
    public boolean isFavorite(int userId, int songId) throws Exception {

        logger.info("Checking favorite status: userId={}, songId={}", userId, songId);

        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND song_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, songId);

            try (ResultSet rs = ps.executeQuery()) {
                boolean exists = rs.next();
                logger.info("Favorite exists={} for userId={}, songId={}", exists, userId, songId);
                return exists;
            }
        }
    }
}