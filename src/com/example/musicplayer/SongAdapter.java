package com.example.musicplayer;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SongAdapter extends BaseAdapter {
	 
	private ArrayList<Song> arraylist;
	private ArrayList<Song> songs;
	private LayoutInflater songInf;
	private String mfname;
	private Context con;
	
	public SongAdapter(Context c, ArrayList<Song> theSongs, String x){
		  mfname=x;
		  songs=theSongs;
		  songInf=LayoutInflater.from(c);
		  con=c;
		  this.arraylist = new ArrayList<Song>();
	      this.arraylist.addAll(songs);
		}
	
	  @Override
	  public int getCount() {
	    // TODO Auto-generated method stub
	    return songs.size();
	  }
	 
	  @Override
	  public Object getItem(int arg0) {
	    // TODO Auto-generated method stub
	    return null;
	  }
	 
	  @Override
	  public long getItemId(int arg0) {
	    // TODO Auto-generated method stub
	    return 0;
	  }
	  
	  public String milliSecondsToTimer(int milliseconds){
	        String finalTimerString = "";
	        String secondsString = "";
	 
	        // Convert total duration into time
	           int hours = (int)( milliseconds / (1000*60*60));
	           int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
	           int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
	           // Add hours if there
	           if(hours > 0){
	               finalTimerString = hours + ":";
	           }
	 
	           // Prepending 0 to seconds if it is one digit
	           if(seconds < 10){
	               secondsString = "0" + seconds;
	           }else{
	               secondsString = "" + seconds;}
	 
	           finalTimerString = ""+finalTimerString + minutes + ":" + secondsString;
	 
	        // return timer string
	        return finalTimerString;
	    }
	  
	  public static int calculateInSampleSize(BitmapFactory.Options options,
	            int reqWidth, int reqHeight) {

	        final int height = options.outHeight;
	        final int width = options.outWidth;
	        int inSampleSize = 1;

	        if (height > reqHeight || width > reqWidth) {
	            if (width > height) {
	                inSampleSize = Math.round((float) height / (float) reqHeight);
	            } else {
	                inSampleSize = Math.round((float) width / (float) reqWidth);
	             }
	         }
	         return inSampleSize;
	        }
	 
	  @Override
	 /* public View getView(int arg0, View arg1, ViewGroup arg2) {
	    // TODO Auto-generated method stub
	    return null;
	  }*/
	  
	  
	  
	  
	  public View getView(int position, View convertView, ViewGroup parent) {
		  //map to song layout
	
		  RelativeLayout relLay = (RelativeLayout)songInf.inflate(R.layout.songlist, parent, false);
		  
		  TextView songView = (TextView)relLay.findViewById(R.id.title);
		  TextView artistView = (TextView)relLay.findViewById(R.id.artist);
		  TextView albumView = (TextView)relLay.findViewById(R.id.album);
		  TextView release = (TextView)relLay.findViewById(R.id.releaseYear);
		  ImageView imgView = (ImageView)relLay.findViewById(R.id.thumbnail);
		  
		  
		  Song currSong = songs.get(position);
		  //get title and artist strings
		  songView.setText(currSong.getTitle());
		  artistView.setText(currSong.getArtist());
		  albumView.setText(currSong.getAlbum());
		  int x;
		  try{
			  x=Integer.parseInt(currSong.getDuration());
		  }
		  catch(Exception e)
		  {
			  System.out.println(e);
			  x=0;
			  
		  }
		  String dur=milliSecondsToTimer(x);
		  
		  release.setText(dur);
		  
		  
		  final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
	      Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, currSong.getalbumID());
          ContentResolver res = con.getContentResolver();
          InputStream in = null;
          
          Bitmap artwork = null;
	 
          int sampleSize=4;
          
          final Options bitmapOptions=new Options();
          bitmapOptions.inDensity=sampleSize;
          bitmapOptions.inTargetDensity=1;
          //final Bitmap scaledBitmap=BitmapFactory.decodeResource(con.getResources(),imageResId,bitmapOptions);
          try 
          {
			in = res.openInputStream(albumArtUri);
			artwork=BitmapFactory.decodeStream(in,null, bitmapOptions);
	        artwork.setDensity(Bitmap.DENSITY_NONE);
		  }
          catch (FileNotFoundException e) 
          {
			// TODO Auto-generated catch block
			 artwork = BitmapFactory.decodeResource(con.getResources(),R.drawable.sound);
			e.printStackTrace();
		  }
          catch (OutOfMemoryError oem)
          {
        	  artwork = BitmapFactory.decodeResource(con.getResources(),R.drawable.wait);
        	  
          }
         
          imgView.setImageBitmap(artwork);  
	      
          relLay.setTag(R.id.position, position);
          relLay.setTag(R.id.mfname, songs);
          
	   //   relLay.setTag( position);
	  
	      return relLay;
		}

	  public void filter(String charText) {
	        charText = charText.toLowerCase(Locale.getDefault());
	        songs.clear();
	        if (charText.length() == 0) {
	            songs.addAll(arraylist);
	        } 
	        else 
	        {
	            for (Song wp : arraylist) 
	            {
	                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) 
	                {
	                    songs.add(wp);
	                }
	            }
	        }
	        notifyDataSetChanged();
	    }
}
