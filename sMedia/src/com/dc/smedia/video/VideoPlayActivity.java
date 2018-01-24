package com.dc.smedia.video;

import org.videolan.vlc.VLCApplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dc.smedia.MediaSaveControl;
import com.dc.smedia.MediaSelectControl;
import com.dc.smedia.R;
import com.dc.smedia.ToPlayData;
import com.dc.smedia.btmusic.MainApp;
import com.dc.smedia.music.ControlMusicMode;
import com.dc.smedia.until.ParseMusicInfo;
import com.dc.smedia.video.PlayVideoFileTool.VideoCallBack;
import com.yecon.common.SourceManager;

public class VideoPlayActivity extends Activity implements
		OnSeekBarChangeListener, OnClickListener {
	private PlayVideoFileTool mPft = PlayVideoFileTool.getPft();
	private SeekBar mSeekBar;
	private String videoTitle;
	private long mCurrentTime, mTotalTime;
	private TextView mCTime, mTTime;
	private FrameLayout mFrame;

	private final static int Msg_Refresh = 100;
	private final static int MSG_HIDE_BAR = 101;
	public static boolean isLocal = false;

	private boolean seekTouch = false;

	private ToPlayData ftoplay = new ToPlayData();
	private boolean toplay = false;
	private long nPosition;
	private boolean beHaveTool = true;

	private TextView btn_video_list, btn_video_prev, btn_video_next,
			btn_video_mode;
	private ImageButton btn_video_play;
	private TextView video_ctime, video_ttime;
	private LinearLayout bottom;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Msg_Refresh:
				handler.removeMessages(Msg_Refresh);
				if (mTotalTime > 0) {
					if (mPft.mIsPrepared) {
						mCurrentTime = mPft.getCurrentTime();
						mTotalTime = mPft.getTotalTime();
						if (mTotalTime != 0) {
							if (!mSeekBar.isEnabled()) {
								mSeekBar.setEnabled(true);
							}
							mSeekBar.setProgress((int) (1000 * mCurrentTime / mTotalTime));
							video_ctime.setText(ParseMusicInfo
									.convertDurationToTime(mCurrentTime + ""));
							video_ttime.setText(ParseMusicInfo
									.convertDurationToTime(mTotalTime + ""));
							if (mPft.isPlaying()) {
								btn_video_play
										.setBackgroundResource(R.drawable.btn_music_pause);
							} else {
								btn_video_play
										.setBackgroundResource(R.drawable.btn_music_play);
							}
						}
					} else {
						if (mSeekBar.isEnabled()) {
							mSeekBar.setEnabled(false);
							video_ctime.setText(ParseMusicInfo
									.convertDurationToTime(0 + ""));
							video_ttime.setText(ParseMusicInfo
									.convertDurationToTime(0 + ""));
						}
					}
				} else {
					if (mPft.mIsPrepared) {
						mCurrentTime = mPft.getCurrentTime();
						mTotalTime = mPft.getTotalTime();
					}
				}
				handler.sendEmptyMessageDelayed(Msg_Refresh, 500);
				break;
			case MSG_HIDE_BAR:
				handler.removeMessages(MSG_HIDE_BAR);
				showBar(false);
				break;
			default:
				break;
			}
		};
	};
	// private Object sourceTocken=null;
	private final String TAG = "VideoPlayActivity";
	private boolean pausedByAudioFocus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
		VLCApplication.getInst().addActivity(this);

		initUI();
		initData();
		handler.sendEmptyMessage(Msg_Refresh);
	}

	private void initUI() {

		mFrame = (FrameLayout) findViewById(R.id.video_surface);
		bottom = (LinearLayout) findViewById(R.id.bottom);
		mSeekBar = (SeekBar) findViewById(R.id.sb_playback_process);

		btn_video_list = (TextView) findViewById(R.id.btn_video_list);
		btn_video_prev = (TextView) findViewById(R.id.btn_video_prev);
		btn_video_next = (TextView) findViewById(R.id.btn_video_next);
		btn_video_mode = (TextView) findViewById(R.id.btn_video_mode);
		btn_video_play = (ImageButton) findViewById(R.id.btn_video_play);
		video_ctime = (TextView) findViewById(R.id.video_ctime);
		video_ttime = (TextView) findViewById(R.id.video_ttime);
		mFrame.setOnClickListener(this);

		btn_video_list.setOnClickListener(this);
		btn_video_prev.setOnClickListener(this);
		btn_video_next.setOnClickListener(this);
		btn_video_mode.setOnClickListener(this);
		btn_video_play.setOnClickListener(this);

		mPft.setSurface(mFrame);
		mPft.createPlayer();
		mSeekBar.setOnSeekBarChangeListener(this);
		mPft.setCallBack(new VideoCallBack() {

			@Override
			public void newSongPlayed() {
				// TODO Auto-generated method stub
				mSeekBar.setEnabled(false);
				mSeekBar.setProgress(0);
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(VideoPlayActivity.this,
				VideoListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
				| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivity(intent);

		// super.onBackPressed();
	}

	private void initData() {
		// TODO Auto-generated method stub
		String path = null;
		try {
			path = getIntent().getStringExtra("video_path");
			nPosition = getIntent().getLongExtra("Media_Current", 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mPft.getCurrentPath() != null) {
			if (path.equals(mPft.getCurrentPath())) {

				if (mPft.isPlaying()) {

				} else {

					mPft.play_pause(true);
				}
				getIntent().getExtras().clear();
				return;
			}
		}

		if (path != null) {
			// mPft.createPlayer();
			ftoplay.path = path;
			ftoplay.nPosition = (int) nPosition;
			PlayVideoFileTool.getPft().startPlay(ftoplay);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		initData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// œ‘ æøÿ÷∆¿∏
		showBar(true);
		isLocal = MediaSelectControl.getInst().isLocal();
		setLoopButtonImage();
		// …Ë÷√◊Ó÷’≤•∑≈Œ™“Ù¿÷
		MediaSaveControl.getInst().setFinalMedia(MediaSaveControl.finalVideo);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// “˛≤ÿ◊¥Ã¨¿∏
		showBar(false);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// SourceManager.unregisterSourceListener(sourceTocken);
		if (handler != null) {
			handler.removeMessages(Msg_Refresh);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekbar, int progress, boolean arg2) {
		// TODO Auto-generated method stub
		if (seekTouch) {
			mPft.seekTo(mTotalTime * progress / 1000);
			showBar(true);
			// handler.sendEmptyMessageDelayed(MSG_HIDE_BAR, 3000);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		seekTouch = true;
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		seekTouch = false;
	}

	public void showBar(boolean bShow) {

		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (bShow) {
			handler.removeMessages(MSG_HIDE_BAR);
			handler.sendEmptyMessageDelayed(MSG_HIDE_BAR, 5000);
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			bottom.setVisibility(View.VISIBLE);
		} else {

			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			bottom.setVisibility(View.INVISIBLE);

		}
		window.setAttributes(params);

	}

	private void setLoopButtonImage() {
		if (ControlVideoMode.getVideoMode() == ControlVideoMode.MODE_CIRCLE) {
			Drawable drawable = getResources().getDrawable(
					R.drawable.btn_video_circle);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			btn_video_mode.setCompoundDrawables(null, drawable, null, null);
			btn_video_mode.setText(R.string.str_btn_loop);
		} else if (ControlVideoMode.getVideoMode() == ControlVideoMode.MODE_RANDOM) {
			Drawable drawable = getResources().getDrawable(
					R.drawable.btn_video_random);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			btn_video_mode.setCompoundDrawables(null, drawable, null, null);
			btn_video_mode.setText(R.string.str_btn_rand);
		} else if (ControlVideoMode.getVideoMode() == ControlVideoMode.MODE_SINGLE) {
			Drawable drawable = getResources().getDrawable(
					R.drawable.btn_video_single);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			btn_video_mode.setCompoundDrawables(null, drawable, null, null);
			btn_video_mode.setText(R.string.str_btn_loop_current);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		boolean bPreFullHideBar = true;
		switch (v.getId()) {
		case R.id.video_surface:
			bPreFullHideBar = false;
			if (bottom.getVisibility() == View.VISIBLE) {
				showBar(false);
			} else {
				showBar(true);
			}
			break;
		case R.id.btn_video_list:
			// mPft.vp.suspendPlay();

			intent = new Intent(VideoPlayActivity.this, VideoListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
					| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			startActivity(intent);
			break;
		case R.id.btn_video_prev:
			String path = mPft.getCurrentPath();
			mPft.play_Prev(path);
			break;
		case R.id.btn_video_play:
			if (mPft.isPlaying()) {
				mPft.play_pause(false);
			} else {
				mPft.play_pause(true);
			}

			break;

		case R.id.btn_video_next:
			path = mPft.getCurrentPath();
			mPft.play_Next(path);
			break;
		case R.id.btn_video_mode:
			if (ControlVideoMode.getVideoMode() == ControlVideoMode.MODE_CIRCLE) {
				SystemProperties.set("persist.sys.video_mode",
						ControlVideoMode.MODE_RANDOM + "");
			} else if (ControlVideoMode.getVideoMode() == ControlVideoMode.MODE_RANDOM) {
				SystemProperties.set("persist.sys.video_mode",
						ControlVideoMode.MODE_SINGLE + "");
			} else if (ControlVideoMode.getVideoMode() == ControlVideoMode.MODE_SINGLE) {
				SystemProperties.set("persist.sys.video_mode",
						ControlVideoMode.MODE_CIRCLE + "");
			}
			setLoopButtonImage();
			break;
		default:
			bPreFullHideBar = true;
			break;
		}
		if (bPreFullHideBar) {
			showBar(true);
		}
	};

}
