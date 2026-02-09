package com.revplay.dao;

import com.revplay.model.Album;
import java.util.List;

public interface AlbumDao {

    void createAlbum(Album album) throws Exception;

    List<Album> getAlbumsByArtist(int artistId) throws Exception;

    void addSongToAlbum(int songId, int albumId) throws Exception;

    // ✅ NEW
    List<Album> getAllAlbums() throws Exception;
}