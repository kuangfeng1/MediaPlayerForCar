package com.dc.smedia.video;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.MediaController.PlayControl;
import io.vov.vitamio.widget.MediaController.onPauseListener;
import io.vov.vitamio.widget.VideoView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.videolan.vlc.VLCApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.dc.smedia.R;
import com.dc.smedia.ToPlayData;
import com.dc.smedia.until.Logg;
import com.dc.smedia.until.Until;

public class VitamioPlayer implements OnClickListener, OnCompletionListener,
		OnInfoListener, OnPreparedListener, OnErrorListener,
		OnBufferingUpdateListener, OnSeekCompleteListener,IVideoPlayFile {
	// private boolean isCreated;
	private MyMediaController mMediaController;
	
	Handler mhandler=new Handler();

	private VideoView mVideoView;
	private int mLayout = VideoView.VIDEO_LAYOUT_STRETCH;
	// private TextView mTv_NoPlay;
	private long mLastPosition;

	// private View mVolumeBrightnessLayout;
	// private ImageView mOperationBg;
	// private ImageView mOperationPercent;
	private AudioManager mAudioManager;
	/** 闁哄牞鎷峰鍫嗗喛绱ｉ梻濠忔嫹 */
	private int mMaxVolume;
	/** 鐟滅増鎸告晶鐘崇珶娴煎鍙�*/
	private int mVolume = -1;
	/** 鐟滅増鎸告晶鐘崇椤旂厧顔�*/
	private float mBrightness = -1f;
	/** 鐟滅増鎸告晶鐘电磽閳哄倹鏉规俊顖楋拷宕囩 */
	private GestureDetector mGestureDetector;
	private float mFast_forward;
	// private View mFl_Progress;
	// private TextView mTv_progress;
	// private ImageView mIv_Progress_bg;
	private boolean isFast_Forword;
	private boolean isUp_downScroll;
	FrameLayout root;
	Context con;

	private ToPlayData ftoplay;

	protected Object doInBackground(Object... params) {
		Vitamio.initialize(con.getApplicationContext());
		if (Vitamio.isInitialized(con.getApplicationContext()))
			return null;

		// 闁告瑥绉撮惃鐘垫喆閿濆懎绔�
		try {
			Class c = Class.forName("io.vov.vitamio.Vitamio");
			Method extractLibs = c.getDeclaredMethod("extractLibs",
					new Class[] { android.content.Context.class, int.class });
			extractLibs.setAccessible(true);
			extractLibs.invoke(c, new Object[] { con.getApplicationContext(),
					R.raw.libarm });

			// Field vitamioLibraryPath =
			// c.getDeclaredField("vitamioLibraryPath");
			//
			// AndroidContextUtils.getDataDir(ctx) + "libs/"

		} catch (NoSuchMethodException e) {
			Logg.e("extractLibs", e.toString());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}


	private FrameLayout mRl_PlayView;

	private void initVideoView() {
		mRl_PlayView = (FrameLayout) root.findViewById(R.id.video_surface);
		if (mVideoView != null) {
			mRl_PlayView.removeView(mVideoView);
			mVideoView = null;
		}
		mVideoView = new io.vov.vitamio.widget.VideoView(con);
		// mVideoView.setLayoutParams(new FrameLayout.LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		VideoView mVideoView1 = (VideoView) root
				.findViewById(R.id.vitamio_surface);
		mVideoView.setLayoutParams(mVideoView1.getLayoutParams());
		// FrameLayout vroot = (FrameLayout) root
		// .findViewById(R.id.FrameLayoutMain_1);
		// vroot.addView(mVideoView);
		// // mVideoView = (VideoView) root.findViewById(R.id.surface_viewABCD);
		// mLoadingView = root.findViewById(R.id.video_loading);
		// mTv_NoPlay = (TextView) root.findViewById(R.id.tv_noPlay);

		mRl_PlayView.addView(mVideoView);

		// mVolumeBrightnessLayout = root
		// .findViewById(R.id.operation_volume_brightness);
		// mOperationBg = (ImageView) root.findViewById(R.id.operation_bg);
		// mOperationPercent = (ImageView) root
		// .findViewById(R.id.operation_percent);
		// mTv_progress = (TextView) findViewById(R.id.tv_progress);
		// mFl_Progress = (FrameLayout)findViewById(R.id.fl_set_progress);
		// mIv_Progress_bg = (ImageView)findViewById(R.id.iv_progress_bg);
		mAudioManager = (AudioManager) con
				.getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnInfoListener(this);
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnErrorListener(this);
		mVideoView.setOnBufferingUpdateListener(this);
		mVideoView.setOnSeekCompleteListener(this);
	}

	// AttributeSet as=new AttributeSet(){;
	// @Override
	public void onConfigurationChanged(Configuration newConfig) {

		int mCurrentOrientation = con.getResources().getConfiguration().orientation;
		if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
			// Utils.full(false, VitamioPlayer.this);
			mRl_PlayView.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 400));
			// if (mVideoView != null)
			// mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);

			mMediaController = new MyMediaController(
					VLCApplication.getAppContext(), mVideoView);
			mMediaController.setOnPauseListener(mPauseListener);
			mVideoView.setMediaController(mMediaController);
		} else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Utils.full(true, VitamioPlayer.this);
			mRl_PlayView.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			/*
			 * if (mVideoView != null) mVideoView.setVideoLayout(mLayout, 0);
			 */mMediaController = new MyMediaController(
					 VLCApplication.getAppContext(), mVideoView);
			mMediaController.setOnPauseListener(mPauseListener);
			mVideoView.setMediaController(mMediaController);
		}

		mMediaController.setEnabled(false);
		// super.onConfigurationChanged(newConfig);
	}

	// @Override
	public boolean onTouchEvent(MotionEvent event) {
		// if (mGestureDetector.onTouchEvent(event))
		// return true;
		//
		// // 濠㈣泛瀚幃濠囧箥鐎ｎ亜鈼㈢紓浣规尰濞硷拷
		// switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// case MotionEvent.ACTION_UP:
		// endGesture();
		// break;
		// }

		// return super.onTouchEvent(event);
		return false;
	}

	/** 闁归潧顑呮繛宥囩磼閹惧瓨灏�*/
	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;
		if (isFast_Forword) {
			onSeekProgress(mFast_forward);
		}
		// 闂傚懏鍔樺Λ锟�
		mDismissHandler.removeMessages(0);
		mDismissHandler.sendEmptyMessageDelayed(0, 800);
	}

	public void executeCmd() {
		if (ftoplay.path.startsWith("http:"))
			mVideoView.setVideoURI(Uri.parse(ftoplay.path));
		else
			mVideoView.setVideoPath(ftoplay.path);
		// 閻犱礁澧介悿鍡涘及閸撗佷粵闁告艾绉惰ⅷ
		mMediaController = new MyMediaController(
				VLCApplication.getAppContext(), mVideoView);
		mMediaController.setmPlayControl(mPlayControll);
		mMediaController.setOnPauseListener(mPauseListener);
		mVideoView.setMediaController(mMediaController);
		mMediaController.setFileName("");

		int mCurrentOrientation = con.getResources().getConfiguration().orientation;
		if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
			// Utils.full(false, VitamioPlayer.this);
			// mRl_PlayView.setLayoutParams(new LinearLayout.LayoutParams(
			// LayoutParams.MATCH_PARENT, 400));
			// if (mVideoView != null) {
			// mVideoView
			// .setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
			// }
		} else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Utils.full(true, VitamioPlayer.this);
			// mRl_PlayView.setLayoutParams(new LinearLayout.LayoutParams(
			// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			// if (mVideoView != null)
			// mVideoView.setVideoLayout(mLayout, 0);
		}
		mVideoView.requestFocus();
		mGestureDetector = new GestureDetector(new MyGestureListener());
	}

	private class MyGestureListener extends SimpleOnGestureListener {

		/** 闁告瑥鑻崵锟�*/
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
				mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
			else
				mLayout++;
			if (mVideoView != null)
				mVideoView.setVideoLayout(mLayout, 0);
			return true;
		}

		/** 婵犲﹥鍨垫慨锟�*/
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// mMediaController.hide();
			float mOldX = e1.getX(), mOldY = e1.getY();
			int y = (int) e2.getRawY();
			int x = (int) e2.getRawX();
			/*
			 * Display disp = con.getWindowManager().getDefaultDisplay(); int
			 * windowWidth = disp.getWidth(); int windowHeight =
			 * disp.getHeight();
			 * 
			 * if (Math.abs(x - mOldX) > 20 && !isUp_downScroll) { // 闁圭瑳鍡╂斀闊浂鍋夌换妯跨疀椤愶讣鎷烽敓锟�
			 * isFast_Forword = true; mFast_forward = x - mOldX;
			 * fast_ForWord(mFast_forward); } else if (mOldX > windowWidth * 1.0
			 * / 2 && Math.abs(mOldY - y) > 3 && !isFast_Forword)// 闁告瑥鐤囩粩鐔奉煥閹存繂袟
			 * onVolumeSlide((mOldY - y) / windowHeight); else if (mOldX <
			 * windowWidth / 2.0 && Math.abs(mOldY - y) > 3 &&
			 * !isFast_Forword)// 鐎归潻绠掔粩鐔奉煥閹存繂袟 onBrightnessSlide((mOldY - y) /
			 * windowHeight); return super.onScroll(e1, e2, distanceX,
			 * distanceY);
			 */
			return false;
		}
	}

	public Runnable refresh = new Runnable() {
		public void run() {
			mDismissHandler.postDelayed(refresh, 300);
			
		}
	};
	// handler.postDelayed(this, 300);

	/** 閻庤纰嶅鍌炴⒕閹邦垱顥�*/
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			isFast_Forword = false;
			isUp_downScroll = false;
			// mVolumeBrightnessLayout.setVisibility(View.GONE);
			// mFl_Progress.setVisibility(View.GONE);
		}
	};

	private void onSeekProgress(float dis) {
		Logg.e("position ==", mVideoView.getCurrentPosition() + 500 * (long) dis
				+ "/" + mVideoView.getDuration());
		mVideoView.seekTo(mVideoView.getCurrentPosition() + 500 * (long) dis);
	}

	private void fast_ForWord(float dis) {
		long currentProgress;
		long duration = mVideoView.getDuration();
		if (mVideoView.getCurrentPosition() + 500 * (long) dis < 0)
			currentProgress = 0;
		else
			currentProgress = mVideoView.getCurrentPosition() + 500
					* (long) dis;
		// mTv_progress.setText(Utils.generateTime(currentProgress) + "/" +
		// Utils.generateTime(duration));
		// if (dis > 0)
		// mIv_Progress_bg.setImageResource(R.drawable.btn_fast_forword);
		// else
		// mIv_Progress_bg.setImageResource(R.drawable.btn_back_forword);
		// mFl_Progress.setVisibility(View.VISIBLE);
	}

	/**
	 * 婵犲﹥鍨垫慨鈺呭绩閻熸澘缍佸閫涘嵆閻撹埖寰勮閻拷
	 * 
	 * @param percent
	 */
	private void onVolumeSlide(float percent) {
		isUp_downScroll = true;
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;

			// 闁哄嫬澧介妵锟�
			// mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			// mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// 闁告瑦蓱濞叉寧绔熸导瀵稿従
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		// 闁告瑦蓱濞叉寧娼诲☉妯侯唺闁哄鎷�
		// ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		// lp.width =
		// root.findViewById(R.id.operation_full).getLayoutParams().width
		// * index / mMaxVolume;
		// mOperationPercent.setLayoutParams(lp);
	}

	/**
	 * 婵犲﹥鍨垫慨鈺呭绩閻熸澘缍佸ù婊庡枛鐎癸拷
	 * 
	 * @param percent
	 */
	private void onBrightnessSlide(float percent) {
		// isUp_downScroll = true;
		// if (mBrightness < 0) {
		// mBrightness = getWindow().getAttributes().screenBrightness;
		// if (mBrightness <= 0.00f)
		// mBrightness = 0.50f;
		// if (mBrightness < 0.01f)
		// mBrightness = 0.01f;
		//
		// // 闁哄嫬澧介妵锟�
		// mOperationBg.setImageResource(R.drawable.video_brightness_bg);
		// mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		// }
		// WindowManager.LayoutParams lpa = getWindow().getAttributes();
		// lpa.screenBrightness = mBrightness + percent;
		// if (lpa.screenBrightness > 1.0f)
		// lpa.screenBrightness = 1.0f;
		// else if (lpa.screenBrightness < 0.01f)
		// lpa.screenBrightness = 0.01f;
		// getWindow().setAttributes(lpa);
		//
		// ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		// lp.width = (int)
		// (findViewById(R.id.operation_full).getLayoutParams().width *
		// lpa.screenBrightness);
		// mOperationPercent.setLayoutParams(lp);
	}

	private void stopPlayer() {
		if (mVideoView != null)
			mVideoView.pause();
	}

	private void startPlayer() {
		if (mVideoView != null)
			mVideoView.start();
			
	}

	private void playCurrentFile() {
		if (ftoplay == null) {
			return;
		}
		if (root == null) {
			return;// 鏉╂ɑ鐥呴張澶婂灥婵瀵茬紒鎾存将
		}
		initVideoView();

		executeCmd();
	}


	/** 闁哄嫷鍨伴幆渚�晸閿燂拷?闁跨喐鏋婚幏鐑芥嚊椤忓嫬袟闁诡厹鍨归ˇ鏌ュ箻椤撶喐鏉归柨娑樼灱閺併倖绂嶆惔銈呮闁告柣鍔嶅▓蹇涘磻濠婃劗绀夐柟顓滃灩椤︽煡骞橀鐔告澒 */
	private boolean needResume;
//	private boolean bePrepared = false;

	@Override
	public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
		switch (arg1) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			// 闁跨噦鎷�闁跨喐鏋婚幏椋庣磽閹惧磭鎽犻柨娑樻湰濞堝繘宕戝鍕啊闁跨噦鎷�
			if (isPlaying()) {
				stopPlayer();
				needResume = true;
			}
			// mLoadingView.setVisibility(View.VISIBLE);
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			// 缂傚倹鎸搁悺銊э拷鐟版湰閸ㄦ岸鏁嶅畝锟介幋椋庣磼椤撶喐灏￠柨鐕傛嫹?
			if (needResume) {
				startPlayer();
			}
			// mLoadingView.setVisibility(View.GONE);
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			// 闁哄嫬澧介妵锟�濞戞挸顑堝ù鍥焻閻斿嘲顔�
			break;
		}
		return true;
	}

	/**
	 * 闁圭虎鍘介弬浣猴拷鐟版湰閸拷
	 */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		System.out.println("onCompletion...  2");
		PlayVideoFileTool.getPft().play_Next(getVideo_Path());
	}

	/**
	 * //闁革负鍔忛～瀣紣閹达富鏆曞璺哄閹﹦锟界懓鏈崹姘跺触鎼淬倗娈堕柣鈧妸閿熻棄鍊稿﹢顏嗘喆閸℃侗鏆ュΛ鏉垮椤︹晠鎮堕崱妤冩殮闁瑰瓨鍔曢幃妤冩偖椤愩儳娈堕柣鈧妸閿熻棄鍊归婵嬪籍閹偊娼掑Λ鐗堝灩濞堟垹锟界妫勭�鎶藉Υ娓氾拷閻濐喗鎯旈敂鐚存嫹娴ｅ壊鍟嶅Δ鍌浬戦惁顔界┍閳╋拷
	 * 娴煎懎顔忛懠顒傜梾闁兼儳鍢茶ぐ鍥礆鐢喚绀夋慨婵勫�濡炲倿宕ｉ婵堟闁烩懇鏆瀍ekTo閻犱讲鏅為～瀣紣閹存粎鐭ら柟绋挎搐閻ｇ偓鎷呭鍥╂瀭鐎殿噯鎷峰┑顔碱儐閹搁亶寮ㄦ穱鎲嬫嫹閿燂拷
	 */
	@Override
	public void onPrepared(MediaPlayer arg0) {
		PlayVideoFileTool.mIsPrepared = true;
		mDismissHandler.post(refresh);
		if (ftoplay.nPosition != 0) {
			if (mVideoView != null) {
				mVideoView.seekTo(ftoplay.nPosition);
			}
			ftoplay.nPosition = 0;
		}
	}

	/**
	 * 闁革负鍔岀槐鎾愁潰閵夛附鎯欏ù锝嗙矎閻ㄧ喖鎮介妸銊х畺缂佸顑勯懙鎴﹀矗閹寸姵鏅搁梺鎸庣懆椤曘倝寮幆鎵闁烩偓鍔婇敓钘夊�缁躲儲淇婇崒婵愭綊濡増鍨舵晶锕�嚕閿熻姤寰勬潏顐バ曢柕鍡嫹
	 */
	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		// mLoadingView.setVisibility(View.GONE);
		// mTv_NoPlay.setVisibility(View.VISIBLE);
		System.out.println("onError...  1");

		

		return false;
	}

	/**
	 * 闁革负鍔庣紞澶岀磼濠婂棭娼掑Λ鐗堝灦缁侊妇绱撻幘鍐叉毐闁告瑦锚鐎垫煡寮幆鎵闁烩偓鍔婇敓鏂ゆ嫹
	 * 
	 * @param arg0
	 * @param arg1
	 */
	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		// mTv_NoPlay.setVisibility(View.GONE);
		// mLoadingView.setVisibility(View.VISIBLE);
	}

	/**
	 * 闁革腹鏆瀍ek闁瑰灝绉崇紞鏃傦拷鐟版湰閸ㄦ岸宕ユ惔銈囨闁烩偓鍔婇敓鏂ゆ嫹
	 */
	@Override
	public void onSeekComplete(MediaPlayer arg0) {
	}

	private MediaController.PlayControl mPlayControll = new PlayControl() {

		@Override
		public void downLoad() {

		}

		@Override
		public void collect() {
		}

	};

	private onPauseListener mPauseListener = new onPauseListener() {

		@Override
		public void onPause() {
			Logg.d("pause", "pause");
		}

		@Override
		public void onPlay() {
			Logg.e("onPlay", "play");
		}
	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	public void clearItems() {
		// TODO Auto-generated method stub
		if (mVideoView != null) {
			mVideoView = null;
		}
	}


	@Override
	public long getCurrentPosition() {
		// TODO Auto-generated method stub
		if (!PlayVideoFileTool.mIsPrepared) {
			return 0;
		}
		if (mVideoView == null) {
			return 0;
		}
		return mVideoView.getCurrentPosition();
	}

	@Override
	public void seekTo(long i) {
		// TODO Auto-generated method stub
		if (mVideoView != null) {
			mVideoView.seekTo(i);
		}
	}

	@Override
	public long getDuration() {
		// TODO Auto-generated method stub
		if (mVideoView == null) {
			return 0;
		}
		return mVideoView.getDuration();
	}

	@SuppressLint("NewApi")
	@Override
	public void setSurfaceLocation(Rect r) {
		// TODO Auto-generated method stub
		if (mVideoView == null)
			return;
		Object o = mVideoView.getLayoutParams();

		android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) o;
		lp.leftMargin = 0;
		lp.topMargin = 0;
		lp.rightMargin = 0;
		lp.bottomMargin = 0;

		lp.width = r.width();
		lp.height = r.height();
		
		Logg.e("vitamio", "vitamio lp.height:"+lp.height+"lp.width:"+lp.width);
		mVideoView.setLayoutParams(lp);

		if (r.width() == 800) {

			mVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mVideoView.setVideoLayout(0, 0, 0.99f, 0.99f);
		} else {
			mVideoView
					.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mVideoView.setVideoLayout(0, 0, 0, 0);
		}
	}

	@Override
	public void stopPlay() {
		// TODO Auto-generated method stub
		if (mVideoView == null) {
			return;
		}
		mVideoView.stopPlayback();
		mVideoView.destroyDrawingCache();
		mRl_PlayView.removeView(mVideoView);
		mVideoView = null;
		mDismissHandler.removeCallbacks(refresh);
	}

	@Override
	public boolean isInited() {
		// TODO Auto-generated method stub
		return PlayVideoFileTool.mIsPrepared;
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		if (mVideoView == null)
			return false;
		return mVideoView.isPlaying();
		
	}

	@Override
	public boolean isPlayed() {
		// TODO Auto-generated method stub
		return PlayVideoFileTool.mIsPrepared;
	}

	@Override
	public String getVideo_Title() {
		// TODO Auto-generated method stub
		return Until.PathtoTitle(ftoplay.path);
	}

	@Override
	public String getVideo_Path() {
		// TODO Auto-generated method stub
		if(ftoplay!=null){
			return ftoplay.path;	
		}
		
		return null;
	}

	@Override
	public void installSuerfaceView(SurfaceView sv) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isIsPrepared() {
		// TODO Auto-generated method stub
		return PlayVideoFileTool.mIsPrepared;
	}

	@Override
	public void playPauseToggle(boolean forceplay) {
		// TODO Auto-generated method stub
		if(mVideoView ==null)
			return ;
		if(forceplay){
			if (!mVideoView.isPlaying()) {
				mVideoView.start();
				
			} 
		}else{
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
			}
		}
	}

	@Override
	public void startPlay(ToPlayData mtoplay) {
		// TODO Auto-generated method stub
		ftoplay = mtoplay;
		mhandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(ftoplay!=null)
				playCurrentFile();
			}
		}, 500);
		
	}

	@Override
	public void setSurfaceSize(int width, int height, int visible_width,
			int visible_height, int sar_num, int sar_den) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createSurfaceView(FrameLayout mframe) {
		// TODO Auto-generated method stub
		con = VLCApplication.getAppContext();
		root = mframe;
		PlayVideoFileTool.mIsPrepared = false;
		// super.onCreate(savedInstanceState);
//		if (false) {
//			new AsyncTask<Object, Object, Object>() {
//				@Override
//				protected Object doInBackground(Object... params) {
//					doInBackground(params);
//				 return "";
//				}
//			}.execute();
//		}
//		else {
			doInBackground(null);
//		}
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		if (mVideoView != null) {
			mVideoView.setVisibility(View.GONE);
		}
	}

	@Override
	public void suspendPlay() {
		if (mVideoView != null) {
			mVideoView.suspend();
		}
		
	}

	@Override
	public void resumePlay() {
		if (mVideoView != null) {
			mVideoView.resume();
		}
		
	}

}
