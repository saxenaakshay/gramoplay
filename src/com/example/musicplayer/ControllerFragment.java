package com.example.musicplayer;
import java.util.List;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ControllerFragment extends FragmentPagerAdapter{
	
	List<Fragment> fragments;
	Context context;
	
	public ControllerFragment(FragmentManager fm, List<Fragment> fragments)
	{
		super(fm);
		this.fragments=fragments;
		
	}
	
	public Fragment getItem(int position){
		
		return fragments.get(position);
		
	}
	
	public int getCount(){
		return fragments.size();
		
	}
	
	public Object instantiateItem(View view, int position){
		LinearLayout linearLayout =  (LinearLayout) view.inflate(context, R.layout.custom_fragment, null);
		TextView tv_text= (TextView) linearLayout.findViewById(R.id.tv_text);
		//ImageView iv_image = (ImageView) linearLayout.findViewById(R.id.iv_image);
		ListView lv=(ListView) view.findViewById(R.id.listView1);
		((ViewPager) view).addView(linearLayout);
		return linearLayout;
		
		
	}
}
