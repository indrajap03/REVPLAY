package com.revplay.service;

import com.revplay.dao.FavoriteDao;
import com.revplay.dao.impl.FavoriteDaoImpl;
import com.revplay.dao.SongDao;
import com.revplay.dao.impl.SongDaoImpl;
import com.revplay.util.DBUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class FavoriteService {

    private static final Logger logger =
            LogManager.getLogger(FavoriteService.class);

    private final FavoriteDao favoriteDao = new FavoriteDaoImpl();
    private final SongDao songDao = new SongDaoImpl();

    public void add(int userId, int songId) throws Exception {

        logger.info("Add favorite requested: userId={}, songId={}", userId, songId);

        if (favoriteDao.isFavorite(userId, songId)) {
            logger.info("Song already exists in favorites: userId={}, songId={}", userId, songId);
            System.out.println("Song already in favorites.");
            return;
        }

        favoriteDao.addFavorite(userId, songId);
        System.out.println("Song added to favorites.");

        logger.info("Song added to favorites successfully: userId={}, songId={}", userId, songId);
    }

    public void remove(int userId, int songId) throws Exception {

        logger.info("Remove favorite requested: userId={}, songId={}", userId, songId);

        favoriteDao.removeFavorite(userId, songId);
        System.out.println("Song removed from favorites.");

        logger.info("Song removed from favorites successfully: userId={}, songId={}", userId, songId);
    }

    public void viewFavorites(int userId) throws Exception {

        logger.info("View favorites requested: userId={}", userId);

        String sql = """
            SELECT s.song_id, s.title, s.duration
            FROM favorites f
            JOIN songs s ON f.song_id = s.song_id
            WHERE f.user_id = ?
        """;

        var con = DBUtil.getConnection();
        var ps = con.prepareStatement(sql);
        ps.setInt(1, userId);
        var rs = ps.executeQuery();

        System.out.println("\n=== MY FAVORITES ===");
        boolean found = false;

        while (rs.next()) {
            found = true;
            System.out.println(
                    rs.getInt("song_id") + " | " +
                            rs.getString("title") + " | " +
                            rs.getInt("duration") + " sec"
            );
        }

        if (!found) {
            logger.info("No favorite songs found for userId={}", userId);
            System.out.println("No favorite songs yet.");
        } else {
            logger.info("Favorites displayed successfully for userId={}", userId);
        }

        con.close();
    }
}