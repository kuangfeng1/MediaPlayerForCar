package com.dc.smedia.video;

import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyMediaController extends MediaController {
	public MyMediaController(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyMediaController(Context context, VideoView videoView) {
		super(context, videoView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return false;
	}


}
