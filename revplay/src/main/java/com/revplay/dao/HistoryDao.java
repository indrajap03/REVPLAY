package com.revplay.dao;

import com.revplay.model.ListeningHistory;
import java.util.List;

public interface HistoryDao {

    void addHistory(int userId, int songId) throws Exception;

    List<ListeningHistory> getHistoryByUser(int userId) throws Exception;

    // NEW
    List<ListeningHistory> getRecentHistory(int userId, int limit) throws Exception;
}