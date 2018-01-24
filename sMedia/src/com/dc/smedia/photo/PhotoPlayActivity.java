package com.dc.smedia.photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.videolan.vlc.VLCApplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dc.smedia.BaseActivity;
import com.dc.smedia.R;
import com.dc.smedia.btmusic.MainApp;
import com.dc.smedia.photo.PlayPicTool.PhotoCallBack;
import com.dc.smedia.until.Until;

public class PhotoPlayActivity extends BaseActivity implements OnClickListener{
	TextView btn_photo_list,btn_photo_prev,btn_photo_next,btn_photo_mode ,pic_title;
	ImageButton btn_photo_play;
	
	private int nPosition;
	private String ftoplay;
	private PlayPicTool mPift =PlayPicTool.getPift();
	
	Bitmap photo_image;
	ImageView pic_photo;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.photo_player);
		VLCApplication.getInst().addActivity(this);
		initUI();
		initData();
		mPift.setCallBack(new PhotoCallBack() {
			
			@Override
			public void newPhotoPlayed(String path) {
				// TODO Auto-generated method stub
				pic_title.setText(path);
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		initData();
	}
	
	private void initUI() {
		btn_photo_list =(TextView) findViewById(R.id.btn_photo_list);
		btn_photo_prev =(TextView) findViewById(R.id.btn_photo_prev);
		btn_photo_next =(TextView) findViewById(R.id.btn_photo_next);
		btn_photo_mode =(TextView) findViewById(R.id.btn_photo_mode);
		pic_title =(TextView) findViewById(R.id.pic_title);
		
		btn_photo_play =(ImageButton) findViewById(R.id.btn_photo_play);
		pic_photo =(ImageView) findViewById(R.id.pic_photo);
		
		pic_photo.setOnClickListener(this);
		btn_photo_list.setOnClickListener(this);
		btn_photo_prev.setOnClickListener(this);
		btn_photo_next.setOnClickListener(this);
		btn_photo_mode.setOnClickListener(this);
		btn_photo_play.setOnClickListener(this);
		
		
	}

	@SuppressLint("NewApi")
	private void initData() {
		try {
			ftoplay = getIntent().getStringExtra("photo_path");
			nPosition = getIntent().getIntExtra("photo_position", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//œ‘ æÕº∆¨
		photo_image =mPift.StartPlay(ftoplay);
		if(photo_image!=null){
			pic_photo.setBackgroundDrawable(Until.bitmap2Drawable(photo_image));
		}
		
	}
	
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_photo_list:
			intent =new Intent(PhotoPlayActivity.this,PhotoListActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_photo_prev:
			ftoplay =mPift.getPrevPath(ftoplay);
			photo_image=mPift.StartPlay(ftoplay);
			if(photo_image!=null){
				pic_photo.setBackgroundDrawable(Until.bitmap2Drawable(photo_image));
			}
			break;
		case R.id.btn_photo_play:
			//À≥–Ú≤•∑≈
			
			break;
		case R.id.btn_photo_next:
			ftoplay =mPift.getNextPath(ftoplay);
			photo_image=mPift.StartPlay(ftoplay);
			if(photo_image!=null){
				pic_photo.setBackgroundDrawable(Until.bitmap2Drawable(photo_image));
			}
			break;
		case R.id.btn_photo_mode:
			
			break;
		case R.id.pic_photo:
			intent =new Intent(PhotoPlayActivity.this,PhotoPlayFullActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	
}
