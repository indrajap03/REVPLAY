package com.revplay.service;

import com.revplay.dao.*;
import com.revplay.dao.impl.*;
import com.revplay.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class ArtistService {

    private static final Logger logger =
            LogManager.getLogger(ArtistService.class);

    private final Scanner sc = new Scanner(System.in);

    private final ArtistDao artistDao = new ArtistDaoImpl();
    private final SongDao songDao = new SongDaoImpl();
    private final AlbumDao albumDao = new AlbumDaoImpl();

    // ================= MANAGE PROFILE =================
    public void manageProfile(int userId) throws Exception {

        logger.info("Manage artist profile requested for userId={}", userId);

        Artist artist = artistDao.getByUserId(userId);

        if (artist == null) {
            logger.info("No existing artist profile found. Creating new profile for userId={}", userId);
            System.out.println("Creating new artist profile");

            Artist newArtist = new Artist();
            newArtist.setUserId(userId);

            System.out.print("Artist Name: ");
            newArtist.setArtistName(sc.nextLine());

            System.out.print("Bio: ");
            newArtist.setBio(sc.nextLine());

            System.out.print("Primary Genre: ");
            newArtist.setPrimaryGenre(sc.nextLine());

            System.out.print("Social Links: ");
            newArtist.setSocialLinks(sc.nextLine());

            artistDao.createProfile(newArtist);
            System.out.println("Artist profile created ");
            logger.info("Artist profile created successfully for userId={}", userId);

        } else {
            logger.info("Existing artist profile found. Updating profile for userId={}", userId);
            System.out.println("Updating artist profile");

            System.out.print("Artist Name: ");
            artist.setArtistName(sc.nextLine());

            System.out.print("Bio: ");
            artist.setBio(sc.nextLine());

            System.out.print("Primary Genre: ");
            artist.setPrimaryGenre(sc.nextLine());

            System.out.print("Social Links: ");
            artist.setSocialLinks(sc.nextLine());

            artistDao.updateProfile(artist);
            System.out.println("Artist profile updated ");
            logger.info("Artist profile updated successfully for userId={}", userId);
        }
    }

    public void uploadSong(int userId) throws Exception {

        logger.info("Upload song requested by userId={}", userId);

        // 1️Get artist profile
        Artist artist = artistDao.getByUserId(userId);
        if (artist == null) {
            System.out.println(" Please create artist profile first.");
            return;
        }

        // 2️Take song details
        System.out.print("Song Title: ");
        String title = sc.nextLine();

        System.out.print("Genre ID: ");
        int genreId = Integer.parseInt(sc.nextLine());

        System.out.print("Duration (in seconds): ");
        int duration = Integer.parseInt(sc.nextLine());

        // 3️Build song
        Song song = new Song();
        song.setTitle(title);
        song.setArtistId(artist.getArtistId()); // correct FK
        song.setGenreId(genreId);               // fixes FK_SONG_GENRE
        song.setDuration(duration);             // avoids bad data
        song.setAlbumId(0);                     // optional, keeps NULL

        songDao.addSong(song);

        System.out.println("Song uploaded successfully ");
        logger.info("Song '{}' uploaded by artistId={}", title, artist.getArtistId());
    }

    // ================= VIEW MY SONGS =================
    public void viewMySongs(int userId) throws Exception {

        logger.info("View songs requested for userId={}", userId);

        // 🔑 Convert userId → artistId
        Artist artist = artistDao.getByUserId(userId);

        if (artist == null) {
            System.out.println(" Artist profile not found.");
            return;
        }

        List<Song> songs = songDao.getSongsByArtist(artist.getArtistId());

        if (songs.isEmpty()) {
            System.out.println("No songs found");
            return;
        }

        for (Song s : songs) {
            System.out.println(s.getSongId() + " | " + s.getTitle());
        }
    }
    // ================= CREATE ALBUM =================
    public void createAlbum(int userId) throws Exception {

        logger.info("Create album requested by userId={}", userId);

        System.out.print("Album Title: ");
        String title = sc.nextLine();

        System.out.print("Release Date (yyyy-mm-dd): ");
        Date date = Date.valueOf(sc.nextLine());

        Album album = new Album();
        album.setArtistId(userId);
        album.setAlbumTitle(title);
        album.setReleaseDate(date);

        albumDao.createAlbum(album);
        System.out.println("Album created ");

        logger.info("Album '{}' created successfully by userId={}", title, userId);
    }

    // ================= ADD SONG TO ALBUM =================
    public void addSongToAlbum(int userId) throws Exception {

        logger.info("Add song to album requested by userId={}", userId);

        System.out.print("Song ID: ");
        int songId = Integer.parseInt(sc.nextLine());

        System.out.print("Album ID: ");
        int albumId = Integer.parseInt(sc.nextLine());

        albumDao.addSongToAlbum(songId, albumId);
        System.out.println("Song added to album ");

        logger.info("SongId={} added to albumId={} by userId={}", songId, albumId, userId);
    }

    // ================= VIEW MY ALBUMS =================
    public void viewMyAlbums(int userId) throws Exception {

        logger.info("View albums requested by userId={}", userId);

        List<Album> albums = albumDao.getAlbumsByArtist(userId);

        if (albums.isEmpty()) {
            logger.info("No albums found for userId={}", userId);
            System.out.println("No albums found");
            return;
        }

        logger.info("Found {} albums for userId={}", albums.size(), userId);

        for (Album a : albums) {
            System.out.println(a.getAlbumId() + " | " + a.getAlbumTitle());
        }
    }

    // ================= DELETE SONG =================
    public void deleteSong(int userId) throws Exception {

        logger.info("Delete song requested by userId={}", userId);

        System.out.print("Enter Song ID to delete: ");
        int songId = Integer.parseInt(sc.nextLine());

        songDao.deleteSong(songId);
        System.out.println("Song deleted ");

        logger.info("SongId={} deleted by userId={}", songId, userId);
    }

    // ================= DELETE ALBUM =================
    public void deleteAlbum(int userId) throws Exception {

        logger.info("Delete album requested by userId={}", userId);

        System.out.print("Enter Album ID to delete: ");
        int albumId = Integer.parseInt(sc.nextLine());
    }
}