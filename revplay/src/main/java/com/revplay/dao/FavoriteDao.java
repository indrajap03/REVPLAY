package com.revplay.dao;

public interface FavoriteDao {

    void addFavorite(int userId, int songId) throws Exception;

    void removeFavorite(int userId, int songId) throws Exception;

    boolean isFavorite(int userId, int songId) throws Exception;
}
