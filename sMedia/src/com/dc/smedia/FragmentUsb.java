package com.dc.smedia;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dc.smedia.music.MusicListActivity;
import com.dc.smedia.photo.PhotoListActivity;
import com.dc.smedia.usblistener.StorageListener;
import com.dc.smedia.video.VideoListActivity;
@SuppressLint("NewApi")
public class FragmentUsb extends Fragment implements OnClickListener{
	Button usb_music,usb_video,usb_photo;
	Button btn_src_next,btn_src_prev;
	ImageView select_photo;
	TextView no_usb;
	FrameLayout usb_media;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MediaSelectControl.getInst().setSelect(MediaSelectControl.usb);
		
		View layout =inflater.inflate(R.layout.layout_usb, container, false);
		initUI(layout);
		
		return layout;
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(hidden){
			
		}else{
			MediaSelectControl.getInst().setSelect(MediaSelectControl.usb);
			if(StorageListener.getInstance().checkStorageExist(getActivity())){
				no_usb.setVisibility(View.GONE);
				usb_media.setVisibility(View.VISIBLE);
			}else{
				no_usb.setVisibility(View.VISIBLE);
				usb_media.setVisibility(View.GONE);
			}
		}
	}
	
	

	private void initUI(View root) {
		usb_music=(Button) root.findViewById(R.id.usb_music);
		usb_video=(Button) root.findViewById(R.id.usb_video);
		usb_photo=(Button) root.findViewById(R.id.usb_photo);
		
		btn_src_next=(Button) root.findViewById(R.id.btn_src_usb_next);
		btn_src_prev=(Button) root.findViewById(R.id.btn_src_usb_prev);
		
		no_usb =(TextView) root.findViewById(R.id.no_usb);
		usb_media =(FrameLayout) root.findViewById(R.id.usb_media);
		
		btn_src_next.setOnClickListener(this);
		btn_src_prev.setOnClickListener(this);
		usb_music.setOnClickListener(this);
		usb_video.setOnClickListener(this);
		usb_photo.setOnClickListener(this);
		
		if(StorageListener.getInstance().checkStorageExist(getActivity())){
			no_usb.setVisibility(View.GONE);
			usb_media.setVisibility(View.VISIBLE);
		}else{
			no_usb.setVisibility(View.VISIBLE);
			usb_media.setVisibility(View.GONE);
		}
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_src_usb_next:
			
			break;
		case R.id.btn_src_usb_prev:
			
			break;
		case R.id.usb_music:
			intent=new Intent(getActivity(),MusicListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			getActivity().startActivity(intent);
			break;
		case R.id.usb_video:
			intent=new Intent(getActivity(),VideoListActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.usb_photo:
			intent=new Intent(getActivity(),PhotoListActivity.class);
			getActivity().startActivity(intent);
			break;
		default:
			break;
		}
	}
}
