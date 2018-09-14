# Gramoplay
This Android project is a music player app called GramoPlay, aimed to seamlessly play music from music files stored on any android device and share and receive updates about songs played by other users of the app around the world. 

The app provides a library of songs to provide real time lists according to song title, song artists and song albums. The app uses media controller to control the song being played by providing functionality to seek song, play/pause current song, play next, play previous, play forward and play backward. The app also provides functionality to search for a song in the music library. The app receives real time feed from all users using the app around the world and displays it in real time in the social feed section.

Languages: Java, PHP, MySQL, HTML & CSS

Hosted the database as an online instance of MySQL on Google Cloud. The database consists of the following table-

| ID | Name | Song | Artist | Album |

Here, ID is the primary key, Name is the name of the user using the application, Song is the title of the music file being played, Artist and Album is the artist name and album name respectively.

The application provides two basic services - Music Player and a Social Feed. The music player allows the user to search and play any song of his choices from the pre-categorized library. The social feed gives real-time updates of people playing songs on the app across the world. 
  
**PRODUCT FUNCTIONS**

* LOGIN: After the initial splash screen, the user is asked to select a user name when using the app for the first time. This is stored in a file inside the internal SD Card memory. The user is then navigated to the library of the songs.
* PLAY MUSIC: Each page in the ViewPager loads different song lists, each sorted according to song title, artists and albums. Clicking on any list item in the song list, begins the playback with 
the song list present on that page. 
* SHUFFLE: This randomizes the songlist being played.
* SEARCH: The user can enter the song title and play the requested song.
* MUSIC CONTROLLER: The music controller is implemented as a widget that gives Play, Pause, skip, seek, forward and  previous functions for playing songs in the music library.
* SOCIAL FEED: This leads the user to the social feed page where all the music updates are displayed in real-time. There is also a REFRESH button to reload the feed.

**CONCLUSION**

The app syncs all the music files (.mp3) present in the internal memory with the music library. The music library is populated each time the app starts. Easy navigation by swiping right or left across the screen to view song lists according to the song titles, artists and albums. Clicking on a song item in the list, begins the music service which plays the song on the device. Music controllers are shown when the song is clicked to control the song. Searching for particular song title is also implemented. A social feed can be viewed inside the app which generates the list of songs with details that are playing currently and have been played using the app by all users worldwide.
 
