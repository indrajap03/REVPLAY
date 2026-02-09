# REVPLAY
RevPlay – Console-Based Music Streaming Application

RevPlay is a Java-based console application that simulates a music streaming platform.
It supports users and artists, playlist management, favorites, listening history, and role-based access using a layered architecture.

Features:<br>
User:<br>
1.Register and login <br>
2.Search songs, artists, albums, playlists<br>
3.Browse by genre, artist, album<br>
4.Play songs (text-based simulation)<br>
5.Create and manage playlists (public/private)<br>
6.Add or remove favorite songs<br>
7.View listening history and recently played songs<br>
8.Change and recover password<br>
<br>
Artist:<br>
1.Register and login as artist<br>
2.Create and manage artist profile<br>
3.Upload and manage songs<br>
4.Create albums and add songs to albums<br>
5.View uploaded songs and albums<br>
6.Track song play count and popularity<br>
<br>
Technologies Used:<br>
Java--->	Core application logic<br>
Oracle DB / MySQL--->	Database<br>
JDBC	--->Database connectivity<br>
Log4J	--->Logging<br>
JUnit--->	Unit testing<br>
Git	--->Version control<br>
<br>
<br>
Database Design:<br>
Relational database with normalized tables<br>
Many-to-many relationships handled using junction tables:<br>
playlist_songs<br>
favorites<br>
Optional relationships supported (songs without albums)<br>
ER Diagram included in the repository<br>

RevPlay/<br>
│── src/<br>
│   ├── app/<br>
│   ├── config/<br>
│   ├── dao/<br>
│   ├── dao/impl/<br>
│   ├── model/<br>
│   ├── service/<br>
│   └── util/<br>
│── db/<br>
│   └── schema.sql<br>
│── docs/<br>
│   └── RevPlay_ERD.png<br>
│── README.md<br>
<br>
<br>
How to Run the Project<br>
<br>
Prerequisites:<br>
Java 8 or higher<br>
Oracle DB / MySQL<br>
JDBC Driver<br>
Git<br>
<br>
Steps:<br>
Clone the repository:<br>
git clone https://github.com/indrajap03/RevPlay.git<br>
<br>
Configure database:<br>
Update DB credentials in:<br>
src/com/revplay/config/DBConfig.java<br>
<br>
Create database tables:<br>
Run the SQL script from:<br>
db/schema.sql<br>
<br>
Run the application:<br>
Run RevPlayApplication.java<br>


Testing:

Unit tests written using JUnit
Test data cleaned using SQL scripts
Logging enabled using Log4J

