package com.dc.smedia;


import com.dc.smedia.music.MusicListActivity;
import com.dc.smedia.photo.PhotoListActivity;
import com.dc.smedia.video.VideoListActivity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class FragmentLocal extends Fragment implements OnClickListener{
	Button local_music,local_video,local_photo;
	Button btn_src_next,btn_src_prev;
	ImageView select_photo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MediaSelectControl.getInst().setSelect(MediaSelectControl.local);
		View layout =inflater.inflate(R.layout.layout_local, container, false);
		initUI(layout);
		return layout;
	}

	private void initUI(View root) {
		local_music=(Button) root.findViewById(R.id.local_music);
		local_video=(Button) root.findViewById(R.id.local_video);
		local_photo=(Button) root.findViewById(R.id.local_photo);
		
		btn_src_next=(Button) root.findViewById(R.id.btn_src_local_next);
		btn_src_prev=(Button) root.findViewById(R.id.btn_src_local_prev);
		
		btn_src_next.setOnClickListener(this);
		btn_src_prev.setOnClickListener(this);
		
		local_music.setOnClickListener(this);
		local_video.setOnClickListener(this);
		local_photo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_src_local_next:
			
			break;
		case R.id.btn_src_local_prev:
			
			break;
		case R.id.local_music:
			intent=new Intent(getActivity(),MusicListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			getActivity().startActivity(intent);
			break;
		case R.id.local_video:
			intent=new Intent(getActivity(),VideoListActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.local_photo:
			intent=new Intent(getActivity(),PhotoListActivity.class);
			getActivity().startActivity(intent);
			break;
		default:
			break;
		}
	}
}
