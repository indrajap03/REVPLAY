package com.revplay.service;

import com.revplay.dao.PlaylistDao;
import com.revplay.dao.impl.PlaylistDaoImpl;
import com.revplay.model.Playlist;
import com.revplay.model.Song;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class PlaylistService {

    private static final Logger logger =
            LogManager.getLogger(PlaylistService.class);

    private final PlaylistDao playlistDao = new PlaylistDaoImpl();
    private final Scanner sc = new Scanner(System.in);

    public void playlistMenu(int userId) throws Exception {

        logger.info("Playlist menu opened for userId={}", userId);

        while (true) {
            System.out.println("\n=== PLAYLIST MENU ===");
            System.out.println("1. Create Playlist");
            System.out.println("2. View My Playlists");
            System.out.println("3. View Songs in Playlist");
            System.out.println("4. Add Song to Playlist");
            System.out.println("5. Remove Song from Playlist");
            System.out.println("6. Update Playlist");
            System.out.println("7. Delete Playlist");
            System.out.println("8. View Public Playlists");
            System.out.println("9. Back");

            System.out.print("Choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            logger.info("Playlist menu choice={} selected by userId={}", choice, userId);

            switch (choice) {
                case 1 -> createPlaylist(userId);
                case 2 -> viewPlaylists(userId);
                case 3 -> viewSongs(userId);
                case 4 -> addSong(userId);
                case 5 -> removeSong(userId);
                case 6 -> updatePlaylist();
                case 7 -> deletePlaylist(userId);
                case 8 -> viewPublicPlaylists();
                case 9 -> {
                    logger.info("Exiting playlist menu for userId={}", userId);
                    return;
                }
            }
        }
    }

    private void createPlaylist(int userId) throws Exception {

        logger.info("Create playlist requested by userId={}", userId);

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Description: ");
        String desc = sc.nextLine();

        System.out.print("Privacy (PUBLIC/PRIVATE): ");
        String privacy = sc.nextLine();

        playlistDao.createPlaylist(userId, name, desc, privacy);
        System.out.println("Playlist created ");

        logger.info("Playlist '{}' created by userId={}", name, userId);
    }

    private void viewPlaylists(int userId) throws Exception {

        logger.info("View playlists requested by userId={}", userId);

        playlistDao.getPlaylistsByUser(userId)
                .forEach(p ->
                        System.out.println(p.getPlaylistId() + " | " + p.getName())
                );
    }

    private void viewSongs(int userId) throws Exception {

        System.out.print("Playlist ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        logger.info("View songs requested for playlistId={} by userId={}", pid, userId);

        List<Song> songs = playlistDao.getSongsInPlaylist(pid, userId);

        if (songs.isEmpty()) {
            logger.info("No songs found in playlistId={} for userId={}", pid, userId);
            System.out.println("No songs found.");
            return;
        }

        songs.forEach(s ->
                System.out.println(s.getSongId() + " | " + s.getTitle())
        );
    }

    private void addSong(int userId) throws Exception {

        System.out.print("Playlist ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.print("Song ID: ");
        int sid = Integer.parseInt(sc.nextLine());

        logger.info("Add song requested: songId={} to playlistId={} by userId={}", sid, pid, userId);

        if (playlistDao.songExistsInPlaylist(pid, sid)) {
            logger.info("SongId={} already exists in playlistId={}", sid, pid);
            System.out.println("Song already exists in playlist ");
            return;
        }

        playlistDao.addSongToPlaylist(pid, sid);
        System.out.println("Song added to playlist ");

        logger.info("SongId={} added to playlistId={}", sid, pid);
    }

    private void removeSong(int userId) throws Exception {

        System.out.print("Playlist ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.print("Song ID: ");
        int sid = Integer.parseInt(sc.nextLine());

        logger.info("Remove song requested: songId={} from playlistId={} by userId={}", sid, pid, userId);

        playlistDao.removeSongFromPlaylist(pid, sid);
        System.out.println("Song removed from playlist ");

        logger.info("SongId={} removed from playlistId={}", sid, pid);
    }

    private void updatePlaylist() throws Exception {

        System.out.print("Playlist ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.print("New Name: ");
        String name = sc.nextLine();

        System.out.print("New Description: ");
        String desc = sc.nextLine();

        System.out.print("Privacy (PUBLIC/PRIVATE): ");
        String privacy = sc.nextLine();

        logger.info("Update playlist requested for playlistId={}", pid);

        playlistDao.updatePlaylist(pid, name, desc, privacy);
        System.out.println("Playlist updated ");

        logger.info("PlaylistId={} updated successfully", pid);
    }

    private void deletePlaylist(int userId) throws Exception {

        System.out.print("Playlist ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        logger.info("Delete playlist requested: playlistId={} by userId={}", pid, userId);

        playlistDao.deletePlaylist(pid, userId);
        System.out.println("Playlist deleted ");

        logger.info("PlaylistId={} deleted by userId={}", pid, userId);
    }

    private void viewPublicPlaylists() throws Exception {

        logger.info("View public playlists requested");

        playlistDao.getPublicPlaylists()
                .forEach(p ->
                        System.out.println(p.getPlaylistId() + " | " + p.getName())
                );
    }
}