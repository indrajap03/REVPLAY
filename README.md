# REVPLAY
RevPlay – Console-Based Music Streaming Application

RevPlay is a Java-based console application that simulates a music streaming platform.
It supports users and artists, playlist management, favorites, listening history, and role-based access using a layered architecture.

Features:
User:
1.Register and login
2.Search songs, artists, albums, playlists
3.Browse by genre, artist, album
4.Play songs (text-based simulation)
5.Create and manage playlists (public/private)
6.Add or remove favorite songs
7.View listening history and recently played songs
8.Change and recover password

Artist:
1.Register and login as artist
2.Create and manage artist profile
3.Upload and manage songs
4.Create albums and add songs to albums
5.View uploaded songs and albums
6.Track song play count and popularity

Technologies Used:
Java--->	Core application logic
Oracle DB / MySQL--->	Database
JDBC	--->Database connectivity
Log4J	--->Logging
JUnit--->	Unit testing
Git	--->Version control


Database Design:
Relational database with normalized tables
Many-to-many relationships handled using junction tables:
playlist_songs
favorites
Optional relationships supported (songs without albums)
ER Diagram included in the repository

RevPlay/
│── src/
│   ├── app/
│   ├── config/
│   ├── dao/
│   ├── dao/impl/
│   ├── model/
│   ├── service/
│   └── util/
│── db/
│   └── schema.sql
│── docs/
│   └── RevPlay_ERD.png
│── README.md


How to Run the Project

Prerequisites:
Java 8 or higher
Oracle DB / MySQL
JDBC Driver
Git

Steps:
Clone the repository:
git clone https://github.com/your-username/RevPlay.git

Configure database:
Update DB credentials in:
src/com/revplay/config/DBConfig.java

Create database tables:
Run the SQL script from:
db/schema.sql

Run the application:
Run RevPlayApplication.java


Testing:

Unit tests written using JUnit
Test data cleaned using SQL scripts
Logging enabled using Log4J

