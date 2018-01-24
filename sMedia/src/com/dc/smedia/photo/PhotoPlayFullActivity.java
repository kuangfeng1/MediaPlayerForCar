package com.dc.smedia.photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.videolan.vlc.VLCApplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dc.smedia.R;
import com.dc.smedia.until.DragImageView;
import com.dc.smedia.until.Until;

public class PhotoPlayFullActivity extends Activity implements OnClickListener {
	private String path;
	private int nPosition;

	private Bitmap photo_image;

	private int window_width, window_height;// 控件宽度
	private DragImageView dragImageView;// 自定义控件
	private int state_height;// 状态栏的高度
	private LinearLayout photo_bottom;

	private ViewTreeObserver viewTreeObserver;
	private final static int MSG_HIDE_BAR = 100;

	private TextView btn_photo_full_list, btn_photo_full_prev,
			btn_photo_full_next, btn_photo_full_mode;
	private ImageButton btn_photo_full_play;

	private FrameLayout photo_frame;
	private PlayPicTool mPift = PlayPicTool.getPift();

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_HIDE_BAR:
				mhandler.removeMessages(MSG_HIDE_BAR);
				showBar(false);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		super.onCreate(arg0);
		/** 获取可見区域高度 **/
		WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();

		setContentView(R.layout.photo_play_full);
		VLCApplication.getInst().addActivity(this);
		initUI();
		getData();
		dragImageView.setmActivity(this);
		/** 测量状态栏高度 **/
		viewTreeObserver = dragImageView.getViewTreeObserver();
		viewTreeObserver
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (state_height == 0) {
							// 获取状况栏高度
							Rect frame = new Rect();
							getWindow().getDecorView()
									.getWindowVisibleDisplayFrame(frame);
							state_height = frame.top;
							dragImageView.setScreen_H(window_height
									- state_height);
							dragImageView.setScreen_W(window_width);
						}

					}
				});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 显示控制栏
		showBar(true);
	}

	private void initUI() {
		// TODO Auto-generated method stub
		dragImageView = (DragImageView) findViewById(R.id.photo_full);

		photo_bottom = (LinearLayout) findViewById(R.id.photo_bottom);
		btn_photo_full_list = (TextView) findViewById(R.id.btn_photo_full_list);
		btn_photo_full_prev = (TextView) findViewById(R.id.btn_photo_full_prev);
		btn_photo_full_next = (TextView) findViewById(R.id.btn_photo_full_next);
		btn_photo_full_mode = (TextView) findViewById(R.id.btn_photo_full_mode);
		btn_photo_full_play = (ImageButton) findViewById(R.id.btn_photo_full_play);
		photo_frame = (FrameLayout) findViewById(R.id.photo_frame);

		btn_photo_full_list.setOnClickListener(this);
		btn_photo_full_prev.setOnClickListener(this);
		btn_photo_full_next.setOnClickListener(this);
		btn_photo_full_mode.setOnClickListener(this);
		btn_photo_full_play.setOnClickListener(this);
		photo_frame.setOnClickListener(this);
		dragImageView.setOnClickListener(this);

	}

	public void showBar(boolean bShow) {

		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (bShow) {
			mhandler.removeMessages(MSG_HIDE_BAR);
			mhandler.sendEmptyMessageDelayed(MSG_HIDE_BAR, 5000);
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			photo_bottom.setVisibility(View.VISIBLE);
		} else {

			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			photo_bottom.setVisibility(View.INVISIBLE);

		}
		window.setAttributes(params);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
//		try {
//			path = getIntent().getStringExtra("photo_path");
//			nPosition = getIntent().getIntExtra("photo_position", 1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		path =mPift.getCurrentPath();
		if (photo_image != null) {
			// dragImageView.setBackgroundDrawable(Until.bitmap2Drawable(photo_image));
			dragImageView.setImageBitmap(photo_image);
		} else {
			photo_image=mPift.StartPlay(mPift.getCurrentPath());
			if (photo_image != null) {
				dragImageView.setImageBitmap(photo_image);
			}

		}

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		boolean bPreFullHideBar = true;
		switch (v.getId()) {
		case R.id.btn_photo_full_list:
			intent = new Intent(PhotoPlayFullActivity.this,
					PhotoListActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_photo_full_prev:
			path = PlayPicTool.getPift().getPrevPath(path);
			photo_image=PlayPicTool.getPift().StartPlay(path);
			if (photo_image != null) {
				dragImageView.setImageBitmap(photo_image);
			}
			break;
		case R.id.btn_photo_full_play:

			break;
		case R.id.btn_photo_full_next:
			path = PlayPicTool.getPift().getNextPath(path);
			photo_image=PlayPicTool.getPift().StartPlay(path);
			if (photo_image != null) {
				dragImageView.setImageBitmap(photo_image);
			}
			break;
		case R.id.btn_photo_full_mode:

			break;

		case R.id.photo_frame:
			bPreFullHideBar = false;
			if (photo_bottom.getVisibility() == View.VISIBLE) {
				showBar(false);
			} else {
				showBar(true);
			}
			break;
		case R.id.photo_full:
			if(dragImageView.isZoom())
				break;
			bPreFullHideBar = false;
			if (photo_bottom.getVisibility() == View.VISIBLE) {
				showBar(false);
			} else {
				showBar(true);
			}
			break;
		default:
			bPreFullHideBar = true;
			break;
		}
		if (bPreFullHideBar) {
			showBar(true);
		}

	}

}
