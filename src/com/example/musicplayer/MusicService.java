package com.example.musicplayer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/*
 * This is demo code to accompany the Mobiletuts+ series:
 * Android SDK: Creating a Music Player
 * 
 * Sue Smith - February 2014
 */

public class MusicService extends Service implements 
MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener {
	
	private FileInputStream fis;
	private String FILENAME = "hello_file";

	//media player
	private MediaPlayer player;
	//song list
	private ArrayList<Song> songs;
	//current position
	private int songPosn;
	//binder
	private final IBinder musicBind = new MusicBinder();
	//title of current song
	private String songTitle="";
	//notification id
	private static final int NOTIFY_ID=1;
	//shuffle flag and random
	private boolean shuffle=false;
	private Random rand;

	public void onCreate(){
		//create the service
		super.onCreate();
		//initialize position
		songPosn=0;
		//random
		rand=new Random();
		//create player
		player = new MediaPlayer();
		//initialize
		initMusicPlayer();
	}

	public void initMusicPlayer(){
		//set player properties
		player.setWakeMode(getApplicationContext(), 
				PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		//set listeners
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}

	//pass song list
	public void setList(ArrayList<Song> theSongs){
		songs=theSongs;
	}

	//binder
	public class MusicBinder extends Binder {
		MusicService getService() { 
			return MusicService.this;
		}
	}

	//activity will bind to service
	@Override
	public IBinder onBind(Intent intent) {
		return musicBind;
	}

	//release resources when unbind
	@Override
	public boolean onUnbind(Intent intent){
		player.stop();
		player.release();
		return false;
	}

	//play a song
	Song playSong;
	public void playSong(){
		//play
		player.reset();
		//get song
		 playSong = songs.get(songPosn);
		//get title
		songTitle=playSong.getTitle();
		//get id
		long currSong = playSong.getID();
		//set uri
		Uri trackUri = ContentUris.withAppendedId(
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				currSong);
		//set the data source
		try{ 
			player.setDataSource(getApplicationContext(), trackUri);
		}
		catch(Exception e){
			Log.e("MUSIC SERVICE", "Error setting data source", e);
		}
		player.prepareAsync(); 
	}

	//set the song
	public void setSong(int songIndex){
		songPosn=songIndex;	
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		//check if playback has reached the end of a track
		if(player.getCurrentPosition()>0){
			mp.reset();
			playNext();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.v("MUSIC PLAYER", "Playback Error");
		mp.reset();
		return false;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onPrepared(MediaPlayer mp) {
		//start playback
		mp.start();
		//notification
		Intent notIntent = new Intent(this, MainActivity.class);
		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendInt = PendingIntent.getActivity(this, 0,
				notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(this);

		builder.setContentIntent(pendInt)
		.setSmallIcon(R.drawable.ic_action_play)
		.setTicker(songTitle)
		.setOngoing(true)
		.setContentTitle("Playing")
		.setContentText(songTitle);
		Notification not = builder.build();
		startForeground(NOTIFY_ID, not);
		
		PostData();
	}

	//playback methods
	public int getPosn(){
		return player.getCurrentPosition();
	}

	public int getDur(){
		return player.getDuration();
	}

	public boolean isPng(){
		return player.isPlaying();
	}

	public void pausePlayer(){
		player.pause();
	}

	public void seek(int posn){
		player.seekTo(posn);
	}

	public void go(){
		player.start();
	}

	//skip to previous track
	public void playPrev(){
		songPosn--;
		if(songPosn<0) songPosn=songs.size()-1;
		playSong();
	}

	//skip to next
	public void playNext(){
		if(shuffle){
			int newSong = songPosn;
			while(newSong==songPosn){
				newSong=rand.nextInt(songs.size());
			}
			songPosn=newSong;
		}
		else{
			songPosn++;
			if(songPosn>=songs.size()) songPosn=0;
		}
		playSong();
	}

	@Override
	public void onDestroy() {
		stopForeground(true);
	}

	//toggle shuffle
	public void setShuffle(){
		if(shuffle) shuffle=false;
		else shuffle=true;
	}
	
public void PostData() {
		
		//String givenUsername="GRAMO2"; String givenPassword="TUNES";
		String title=playSong.getTitle();
		String artist= playSong.getArtist();
		String album= playSong.getAlbum();
		
		String user="";
		try {
			fis= openFileInput(FILENAME);
			if (fis!=null) { 
				int content;
				while ((content = fis.read()) != -1) {
					// convert to char and display it
					user+=((char) content);
				}
				
				//Log.d("LOGGED IN USER:", user);
				}
			fis.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		class SendPostReqAsyncTask extends AsyncTask<String, Void, String>{

			@Override
			protected String doInBackground(String... params) {
				
				
				
				String paramUsername = params[0];
				String paramTitle = params[1];
				String paramArtist = params[2];
				String paramAlbum = params[3];

				System.out.println("*** doInBackground ** paramUsername " + paramUsername + " paramPassword :" + paramTitle + " " + paramArtist + " " + paramAlbum);

				HttpClient httpClient = new DefaultHttpClient();

				// In a POST request, we don't pass the values in the URL.
				//Therefore we use only the web page URL as the parameter of the HttpPost argument
				HttpPost httpPost = new HttpPost("http://gramotwo.appspot.com/");

				// Because we are not passing values over the URL, we should have a mechanism to pass the values that can be
				//uniquely separate by the other end.
				//To achieve that we use BasicNameValuePair				
				//Things we need to pass with the POST request
				BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair("paramUsername", paramUsername);
				BasicNameValuePair titleBasicNameValuePAir = new BasicNameValuePair("paramTitle", paramTitle);

				BasicNameValuePair artistBasicNameValuePAir = new BasicNameValuePair("paramArtist", paramArtist);

				BasicNameValuePair albumBasicNameValuePAir = new BasicNameValuePair("paramAlbum", paramAlbum);

			
				// We add the content that we want to pass with the POST request to as name-value pairs
				//Now we put those sending details to an ArrayList with type safe of NameValuePair
				List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
				nameValuePairList.add(usernameBasicNameValuePair);
				nameValuePairList.add(titleBasicNameValuePAir);
				nameValuePairList.add(artistBasicNameValuePAir);
				nameValuePairList.add(albumBasicNameValuePAir);

				try {
					// UrlEncodedFormEntity is an entity composed of a list of url-encoded pairs. 
					//This is typically useful while sending an HTTP POST request. 
					UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);

					// setEntity() hands the entity (here it is urlEncodedFormEntity) to the request.
					httpPost.setEntity(urlEncodedFormEntity);

					try {
						// HttpResponse is an interface just like HttpPost.
						//Therefore we can't initialize them
						HttpResponse httpResponse = httpClient.execute(httpPost);

						// According to the JAVA API, InputStream constructor do nothing. 
						//So we can't initialize InputStream although it is not an interface
						InputStream inputStream = httpResponse.getEntity().getContent();

						InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

						BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

						StringBuilder stringBuilder = new StringBuilder();

						String bufferedStrChunk = null;

						while((bufferedStrChunk = bufferedReader.readLine()) != null){
							stringBuilder.append(bufferedStrChunk);
						}

						return stringBuilder.toString();

					} catch (ClientProtocolException cpe) {
						System.out.println("Firstption caz of HttpResponese :" + cpe);
						cpe.printStackTrace();
					} catch (IOException ioe) {
						System.out.println("Secondption caz of HttpResponse :" + ioe);
						ioe.printStackTrace();
					}

				} catch (UnsupportedEncodingException uee) {
					System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
					uee.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				/*if(result.equals("working")){
					Toast.makeText(getApplicationContext(), "HTTP POST is working...", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(), "Invalid POST req..."+result, Toast.LENGTH_LONG).show();
				}*/
				
				System.out.println (result);
			}			
		}

		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute(user,title,artist,album);		
		     } 
}
