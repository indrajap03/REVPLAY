package com.revplay.service;

import com.revplay.dao.SongDao;
import com.revplay.dao.impl.SongDaoImpl;
import com.revplay.model.Song;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class SongService {

    private static final Logger logger =
            LogManager.getLogger(SongService.class);

    private final SongDao songDao = new SongDaoImpl();
    private final HistoryService historyService = new HistoryService();
    private final Scanner sc = new Scanner(System.in);

    public void showAllSongsAndPlay(int userId) throws Exception {

        logger.info("Show all songs requested by userId={}", userId);

        List<Song> songs = songDao.getAllSongs();

        if (songs.isEmpty()) {
            logger.info("No songs available in system");
            System.out.println("No songs available.");
            return;
        }

        System.out.println("\n--- SONG LIST ---");
        for (Song s : songs) {
            System.out.println(
                    s.getSongId() + " | " +
                            s.getTitle() + " | " +
                            s.getDuration() + " sec | Plays: " +
                            s.getPlayCount()
            );
        }

        System.out.print("\nEnter Song ID to play (0 to cancel): ");
        int songId = Integer.parseInt(sc.nextLine());

        if (songId == 0) {
            logger.info("User cancelled song play (userId={})", userId);
            return;
        }

        Song song = songDao.getSongById(songId);

        if (song == null) {
            logger.warn("Invalid songId={} entered by userId={}", songId, userId);
            System.out.println("Invalid Song ID.");
            return;
        }

        // PLAY
        logger.info("Playing songId={} ({}) for userId={}", songId, song.getTitle(), userId);

        System.out.println("\n Playing: " + song.getTitle());
        System.out.println("Duration: " + song.getDuration() + " sec");

        songDao.incrementPlayCount(songId);
        historyService.recordPlay(userId, songId);

        // CONTROLS
        System.out.println("\nControls: [P]ause [S]kip [R]epeat [E]xit");

        while (true) {
            String cmd = sc.nextLine().toUpperCase();

            switch (cmd) {
                case "P" -> {
                    System.out.println("Paused");
                    logger.info("Song paused: songId={}, userId={}", songId, userId);
                }
                case "R" -> {
                    System.out.println("Replaying " + song.getTitle());
                    logger.info("Song replayed: songId={}, userId={}", songId, userId);
                }
                case "S" -> {
                    System.out.println("Skipped");
                    logger.info("Song skipped: songId={}, userId={}", songId, userId);
                    return;
                }
                case "E" -> {
                    System.out.println("Stopped");
                    logger.info("Song stopped: songId={}, userId={}", songId, userId);
                    return;
                }
                default -> {
                    System.out.println("Invalid control");
                    logger.warn("Invalid playback control '{}' by userId={}", cmd, userId);
                }
            }
        }
    }
}