package com.revplay.dao.impl;

import com.revplay.model.ListeningHistory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryDaoImplTest {

    // Testing the DAO IMPLEMENTATION
    private final HistoryDaoImpl dao = new HistoryDaoImpl();

    // ================= READ-ONLY: getHistoryByUser =================
    @Test
    void testGetHistoryByUser_readOnly() throws Exception {

        int userId = 1; // can be existing or non-existing

        List<ListeningHistory> history = dao.getHistoryByUser(userId);

        // SELECT only — DB safe
        assertNotNull(history);

        if (!history.isEmpty()) {
            for (ListeningHistory h : history) {
                assertEquals(userId, h.getUserId());
                assertNotNull(h.getPlayedAt());
            }
        }
    }

    // ================= READ-ONLY: getRecentHistory =================
    @Test
    void testGetRecentHistory_readOnly() throws Exception {

        int userId = 1; // any value
        int limit = 5;  // max rows

        List<ListeningHistory> recent = dao.getRecentHistory(userId, limit);

        // SELECT only — DB safe
        assertNotNull(recent);

        // Result size will never exceed limit
        assertTrue(recent.size() <= limit);

        if (!recent.isEmpty()) {
            for (ListeningHistory h : recent) {
                assertNotNull(h.getPlayedAt());
            }
        }
    }
}
