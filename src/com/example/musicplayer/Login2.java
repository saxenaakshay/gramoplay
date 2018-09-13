package com.example.musicplayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Login2 extends Activity {

	String FILENAME = "hello_file";
	String str = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login2);
		

		FileOutputStream fos=null;
		FileInputStream fis;
	
			try {
				fis= openFileInput(FILENAME);
				if (fis!=null) { 
					Intent i=new Intent(Login2.this, MainActivity.class);
				startActivity(i);}
				fis.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setContentView(R.layout.activity_login2);
			
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		FileInputStream fis;
		
		try {
			fis= openFileInput(FILENAME);
			if (fis!=null) { 
				Intent i=new Intent(Login2.this, MainActivity.class);
			startActivity(i);}
			fis.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void inputName(View v)
	{
		try {
			FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			
			EditText et = (EditText) findViewById(R.id.editText1);
			str = et.getText().toString();
			System.out.println("Name:"+ str);
			fos.write(str.getBytes());
			fos.close();
			Intent i=new Intent(Login2.this, MainActivity.class);
			startActivity(i);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
