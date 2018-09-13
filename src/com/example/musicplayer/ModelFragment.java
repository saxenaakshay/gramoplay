package com.example.musicplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class ModelFragment extends Fragment {
	
	public static final String _text ="text";
	public static final String _image ="image";
	Context context;
	SongAdapter songAdt;
	SearchView editsearch;
	ListView lv;
	//private static MusicController controller;
	
	
	public static final ModelFragment newInstance(String text, int image){
		ModelFragment mf= new ModelFragment();
		Bundle bundle= new Bundle();
		bundle.putString(_text, text);
		bundle.putInt(_image, image);
		
		mf.setArguments(bundle);
		return mf;	
	} 

	public View onCreateView(LayoutInflater inflater,  ViewGroup group, Bundle bundle)
	{
		View view=inflater.inflate(R.layout.custom_fragment, group, false);
		String text=getArguments().getString(_text);
		TextView tv_text=(TextView) view.findViewById(R.id.tv_text);
		lv=(ListView) view.findViewById(R.id.listView1);
		editsearch = (SearchView) view.findViewById(R.id.search);
		
		ArrayList<Song> songList= new ArrayList<Song>();
		songList=getSongList(text);
	    songAdt = new SongAdapter(this.getActivity(), songList, text);
		lv.setAdapter(songAdt);
		tv_text.setText(text);
		editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
	            @Override
	            public boolean onQueryTextSubmit(String query) {
	                callSearch(query);
	                return true;
	            }

	            @Override
	            public boolean onQueryTextChange(String newText) {
//	              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
	                    callSearch(newText);
//	              }
	                return true;
	            }

	            public void callSearch(String query) {
	                //Do searching
	            	songAdt.filter(query);
	            }

	        });
		/*editsearch.addTextChangedListener(new TextWatcher() {
			  
	            @Override
	            public void afterTextChanged(Editable arg0) {
	                // TODO Auto-generated method stub
	                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
	                songAdt.filter(text);
	                for (int i=0; i< text.length(); i++)
	                {
	                	if (text.charAt(i)=='\n')
	                	{
	                		System.out.println("ENTER PRESSED");
	                		editsearch.setText("");
	                		
	                	}
	                }
	              
	            }
	 
	            @Override
	            public void beforeTextChanged(CharSequence arg0, int arg1,
	                    int arg2, int arg3) {
	                // TODO Auto-generated method stub
	            }
	 
	            @Override
	            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
	                    int arg3) {
	                // TODO Auto-generated method stub
	            
	            }
	        });*/
	
	 
		 return view;
		
	}
	

	
	public  ArrayList<Song> getSongList(String listType) {
		  //retrieve song info
		
		
		ArrayList<Song> songList=new ArrayList<Song>();
		ContentResolver musicResolver = this.getActivity().getContentResolver();
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
		
		if(musicCursor!=null && musicCursor.moveToFirst()){
			  //get columns
			  int titleColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media.TITLE);
			  int idColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media._ID);
			  int artistColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media.ARTIST);
			  int albumColumn = musicCursor.getColumnIndex
					    (android.provider.MediaStore.Audio.Media.ALBUM);
			  
			  int albumId =musicCursor.getColumnIndex
					    (android.provider.MediaStore.Audio.Media.ALBUM_ID);
			  int duration =musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.DURATION);
			  //add songs to list
			  do {
			    long thisId = musicCursor.getLong(idColumn);
			    String thisTitle = musicCursor.getString(titleColumn);
			    String thisArtist = musicCursor.getString(artistColumn);
			    String thisAlbum = musicCursor.getString(albumColumn);
			    long thisAlbumId= musicCursor.getLong(albumId);
			    String thisDuration = musicCursor.getString(duration);
			
			    songList.add(new Song(thisId, thisTitle, thisArtist, thisAlbum, thisAlbumId, thisDuration));
		
			  }
			  while (musicCursor.moveToNext());
		}
		
		if (listType=="SONGS"){
			Collections.sort(songList, new Comparator<Song>(){
				public int compare(Song a, Song b){
					return a.getTitle().compareTo(b.getTitle());
				}
			});	
			System.out.println("CREATING SONGTITLE LIST for text: "+listType);
		}
		else if (listType=="ALBUMS")
		{
			Collections.sort(songList, new Comparator<Song>(){
				public int compare(Song a, Song b){
					return a.getAlbum().compareTo(b.getAlbum());
				}
				
			});
			System.out.println("CREATING ALBUM LIST for text: "+listType);
		}
		else if (listType=="ARTISTS")
		{
			Collections.sort(songList, new Comparator<Song>(){
				public int compare(Song a, Song b){
					return a.getArtist().compareTo(b.getArtist());
				}			
				
			});
			System.out.println("CREATING ARTIST LIST for text: "+listType);
		}
		else
		{
			System.out.println("djkd"+listType);
			
		}
		return songList;
	}


}
