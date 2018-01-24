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
import android.widget.ImageButton;

import com.dc.smedia.btmusic.BtMusicPlayActivity;

@SuppressLint("NewApi")
public class FragmentBtmusic extends Fragment implements OnClickListener{
	
	Button btn_src_next,btn_src_prev;
	ImageButton btn_enterbtmusic;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MediaSelectControl.getInst().setSelect(MediaSelectControl.btmusic);
		View layout =inflater.inflate(R.layout.layout_btmusic, container, false);
		initUI(layout);
		return layout;
	}

	private void initUI(View root) {

		
		btn_src_next=(Button) root.findViewById(R.id.btn_src_btmusic_next);
		btn_src_prev=(Button) root.findViewById(R.id.btn_src_btmusic_prev);
		
		btn_enterbtmusic =(ImageButton) root.findViewById(R.id.btn_enterbtmusic);
		
		btn_src_next.setOnClickListener(this);
		btn_src_prev.setOnClickListener(this);
		btn_enterbtmusic.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_src_btmusic_next:
			
			break;
		case R.id.btn_src_btmusic_prev:
			
			break;
		case R.id.btn_enterbtmusic:
			
			Intent intent =new Intent(getActivity(),BtMusicPlayActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			getActivity().startActivity(intent);
			break;
		default:
			break;
		}
	}
}
