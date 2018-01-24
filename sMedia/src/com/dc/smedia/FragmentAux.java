package com.dc.smedia;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class FragmentAux extends Fragment implements OnClickListener{
	
	Button btn_src_next,btn_src_prev;
	ImageView select_photo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MediaSelectControl.getInst().setSelect(MediaSelectControl.aux);
		View layout =inflater.inflate(R.layout.layout_aux, container, false);
		initUI(layout);
		return layout;
	}

	private void initUI(View root) {

		
		btn_src_next=(Button) root.findViewById(R.id.btn_src_aux_next);
		btn_src_prev=(Button) root.findViewById(R.id.btn_src_aux_prev);
		
		btn_src_next.setOnClickListener(this);
		btn_src_prev.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_src_aux_next:
			
			break;
		case R.id.btn_src_aux_prev:
			
			break;

		default:
			break;
		}
	}
}
