package com.dc.smedia;

import com.dc.smedia.scanner.MediaSCtrol;
import com.dc.smedia.scanner.MediaScanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class FragmentLoading extends Fragment {
	ImageView loading;
	AnimationDrawable anim;
	int nPosition=0;

//	private final int Msg_CheckState=0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout=inflater.inflate(R.layout.layout_loading, container, false);
		loading =(ImageView) layout.findViewById(R.id.loading);
		loading.setBackgroundResource(R.anim.animation_loading);
		anim =(AnimationDrawable) loading.getBackground();
		anim.setOneShot(false);
		anim.start();
//		handler.sendEmptyMessage(Msg_CheckState);
		
		return layout;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
//		handler.sendEmptyMessage(Msg_CheckState);
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
//		handler.removeMessages(Msg_CheckState);
	}
}
