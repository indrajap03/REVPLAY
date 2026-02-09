package com.revplay.app;

import com.revplay.model.User;
import com.revplay.service.*;

import java.util.Scanner;

public class RevPlayApplication {

    private static final Scanner sc = new Scanner(System.in);

    // Services
    private static final AuthService authService = new AuthService();
    private static final SongService songService = new SongService();
    private static final PlaylistService playlistService = new PlaylistService();
    private static final FavoriteService favoriteService = new FavoriteService();
    private static final HistoryService historyService = new HistoryService();
    private static final SearchService searchService = new SearchService();
    private static final BrowseService browseService = new BrowseService();
    private static final ArtistService artistService = new ArtistService();

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=== REVPLAY ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> loginFlow();
                case 3 -> forgotPassword();
                case 4 -> {
                    System.out.println("Goodbye ");
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    // ================= REGISTER =================
    private static void registerUser() {
        try {
            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            System.out.print("Password Hint (for recovery): ");
            String hint = sc.nextLine();

            System.out.print("Role (USER / ARTIST): ");
            String role = sc.nextLine();

            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setPasswordHint(hint);
            user.setRole(role.toUpperCase());

            authService.register(user);
            System.out.println("Registration successful ");

        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }

    // ================= LOGIN FLOW (FIXED) =================
    private static void loginFlow() {
        try {
            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            User user = authService.login(email, password);

            if (user == null) {
                System.out.println("Invalid credentials ");
                return;
            }

            System.out.println("Welcome " + user.getEmail());

            //  ROLE BASED ROUTING
            if ("ARTIST".equalsIgnoreCase(user.getRole())) {
                artistMenu(user);
            } else {
                userMenu(user);
            }

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    // ================= FORGOT PASSWORD =================
    private static void forgotPassword() {
        try {
            System.out.print("Enter registered email: ");
            String email = sc.nextLine();

            User user = authService.getUserByEmail(email);

            if (user == null) {
                System.out.println("No user found with this email.");
                return;
            }

            System.out.println("Password Hint: " + user.getPasswordHint());

            System.out.print("Enter new password: ");
            String newPass = sc.nextLine();

            authService.changePassword(user.getUserId(), newPass);
            System.out.println("Password updated successfully ");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ================= USER MENU =================
    private static void userMenu(User user) {

        while (true) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("1. View & Play Songs");
            System.out.println("2. Playlists");
            System.out.println("3. Add Song to Favorites");
            System.out.println("4. Remove Song from Favorites");
            System.out.println("5. View Favorite Songs");
            System.out.println("6. Search");
            System.out.println("7. Browse by Category");
            System.out.println("8. View Listening History");
            System.out.println("9. Recently Played");
            System.out.println("10. Change Password");
            System.out.println("11. Logout");
            System.out.print("Choice: ");

            int choice = Integer.parseInt(sc.nextLine());

            try {
                switch (choice) {
                    case 1 -> songService.showAllSongsAndPlay(user.getUserId());
                    case 2 -> playlistService.playlistMenu(user.getUserId());
                    case 3 -> {
                        System.out.print("Enter Song ID: ");
                        favoriteService.add(user.getUserId(), Integer.parseInt(sc.nextLine()));
                    }
                    case 4 -> {
                        System.out.print("Enter Song ID: ");
                        favoriteService.remove(user.getUserId(), Integer.parseInt(sc.nextLine()));
                    }
                    case 5 -> favoriteService.viewFavorites(user.getUserId());
                    case 6 -> {
                        System.out.print("Enter keyword to search: ");
                        searchService.search(sc.nextLine());
                    }
                    case 7 -> browseMenu();
                    case 8 -> historyService.showUserHistory(user.getUserId());
                    case 9 -> historyService.showRecentlyPlayed(user.getUserId());
                    case 10 -> changePassword(user);
                    case 11 -> {
                        System.out.println("Logged out.");
                        return;
                    }
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ================= ARTIST MENU =================
    private static void artistMenu(User user) {

        while (true) {
            System.out.println("\n=== ARTIST MENU ===");
            System.out.println("1. Manage Artist Profile");
            System.out.println("2. Upload Song");
            System.out.println("3. View My Songs");
            System.out.println("4. Create Album");
            System.out.println("5. Add Song to Album");
            System.out.println("6. View My Albums");
            System.out.println("7. Delete Song");
            System.out.println("8. Delete Album");
            System.out.println("9. Search");
            System.out.println("10. Change Password");
            System.out.println("11. Logout");
            System.out.print("Choice: ");

            int choice = Integer.parseInt(sc.nextLine());

            try {
                switch (choice) {
                    case 1 -> artistService.manageProfile(user.getUserId());
                    case 2 -> artistService.uploadSong(user.getUserId());
                    case 3 -> artistService.viewMySongs(user.getUserId());
                    case 4 -> artistService.createAlbum(user.getUserId());
                    case 5 -> artistService.addSongToAlbum(user.getUserId());
                    case 6 -> artistService.viewMyAlbums(user.getUserId());
                    case 7 -> artistService.deleteSong(user.getUserId());
                    case 8 -> artistService.deleteAlbum(user.getUserId());
                    case 9 -> {
                        System.out.print("Enter keyword to search: ");
                        searchService.search(sc.nextLine());
                    }
                    case 10 -> changePassword(user);
                    case 11 -> {
                        System.out.println("Logged out.");
                        return;
                    }
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ================= CHANGE PASSWORD =================
    private static void changePassword(User user) {
        try {
            System.out.print("Enter new password: ");
            String newPass = sc.nextLine();
            authService.changePassword(user.getUserId(), newPass);
            System.out.println("Password changed successfully ");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ================= BROWSE MENU =================
    private static void browseMenu() {
        System.out.println("\nBrowse By:");
        System.out.println("1. Genre");
        System.out.println("2. Artist");
        System.out.println("3. Album");
        System.out.print("Choice: ");

        int opt = Integer.parseInt(sc.nextLine());

        try {
            switch (opt) {
                case 1 -> browseService.browseByGenre();
                case 2 -> browseService.browseByArtist();
                case 3 -> browseService.browseByAlbum();
                default -> System.out.println("Invalid option");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}