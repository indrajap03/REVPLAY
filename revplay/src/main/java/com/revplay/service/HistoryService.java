package com.revplay.service;

import com.revplay.dao.HistoryDao;
import com.revplay.dao.impl.HistoryDaoImpl;
import com.revplay.model.ListeningHistory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class HistoryService {

    private static final Logger logger =
            LogManager.getLogger(HistoryService.class);

    private final HistoryDao historyDao = new HistoryDaoImpl();

    public void recordPlay(int userId, int songId) {

        logger.info("Record play requested: userId={}, songId={}", userId, songId);

        try {
            historyDao.addHistory(userId, songId);
            logger.info("Play history recorded successfully: userId={}, songId={}", userId, songId);
        } catch (Exception e) {
            logger.error("Error recording play history: userId={}, songId={}", userId, songId, e);
            System.out.println("History error: " + e.getMessage());
        }
    }

    // FULL HISTORY
    public void showUserHistory(int userId) {

        logger.info("Show full listening history requested: userId={}", userId);

        try {
            List<ListeningHistory> list =
                    historyDao.getHistoryByUser(userId);

            if (list.isEmpty()) {
                logger.info("No listening history found for userId={}", userId);
                System.out.println("No listening history found.");
                return;
            }

            System.out.println("\n=== LISTENING HISTORY ===");
            logger.info("Displaying {} history records for userId={}", list.size(), userId);

            for (ListeningHistory h : list) {
                System.out.println(
                        "Song ID: " + h.getSongId() +
                                " | Played At: " + h.getPlayedAt()
                );
            }

        } catch (Exception e) {
            logger.error("Error fetching listening history for userId={}", userId, e);
            System.out.println("History error: " + e.getMessage());
        }
    }

    // ✅ RECENTLY PLAYED
    public void showRecentlyPlayed(int userId) {

        logger.info("Show recently played requested: userId={}", userId);

        try {
            List<ListeningHistory> list =
                    historyDao.getRecentHistory(userId, 5);

            if (list.isEmpty()) {
                logger.info("No recently played songs for userId={}", userId);
                System.out.println("No recently played songs.");
                return;
            }

            System.out.println("\n=== RECENTLY PLAYED ===");
            logger.info("Displaying {} recently played songs for userId={}", list.size(), userId);

            for (ListeningHistory h : list) {
                System.out.println(
                        "Song ID: " + h.getSongId() +
                                " | Played At: " + h.getPlayedAt()
                );
            }

        } catch (Exception e) {
            logger.error("Error fetching recently played songs for userId={}", userId, e);
            System.out.println("History error: " + e.getMessage());
        }
    }
}