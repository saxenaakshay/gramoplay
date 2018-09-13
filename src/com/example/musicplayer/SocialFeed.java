package com.example.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class SocialFeed extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_feed);
		WebView myWebView = (WebView) findViewById(R.id.webView1);
		myWebView.loadUrl("http://gramotwo.appspot.com/");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu2, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//menu item selected
		switch (item.getItemId()) {
	
		case R.id.action_f5:
			
		 WebView wv= (WebView) this.findViewById(R.id.webView1);
		 wv.loadUrl("http://gramotwo.appspot.com/");
		 break;
		}
		return super.onOptionsItemSelected(item);
	}
}
