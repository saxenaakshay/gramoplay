package com.example.musicplayer;

/*
 * This is demo code to accompany the Mobiletuts+ series:
 * Android SDK: Creating a Music Player
 * 
 * Sue Smith - February 2014
 */

public class Song {
	private long id;
	private String title;
	private String artist;
	private String album;
	private String duration;
	private long albumId;
	
	public Song(long songID, String songTitle, String songArtist, String songAlbum,long aId, String dur) {
		  id=songID;
		  title=songTitle;
		  artist=songArtist;
		  album=songAlbum;
		  albumId=aId;
		  duration=dur;
	}
	public long getID(){return id;}
	public String getTitle(){return title;}
	public String getArtist(){return artist;}
	public String getAlbum() {return album;}
	public String getDuration() {return duration;}
	public long getalbumID(){return albumId;}
	

}
