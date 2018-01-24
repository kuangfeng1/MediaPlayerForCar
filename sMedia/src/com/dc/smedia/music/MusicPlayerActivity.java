package com.dc.smedia.music;



import org.videolan.vlc.VLCApplication;
import org.videolan.vlc.util.MarqueeTextView;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemProperties;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dc.smedia.BaseActivity;
import com.dc.smedia.MainActivity;
import com.dc.smedia.MediaSaveControl;
import com.dc.smedia.MediaSelectControl;
import com.dc.smedia.R;
import com.dc.smedia.ToPlayData;
import com.dc.smedia.btmusic.MainApp;
import com.dc.smedia.music.PlayAudioFileTool.MusicCallBack;
import com.dc.smedia.scanner.MediaDataControl;
import com.dc.smedia.until.ArtworkUtils;
import com.dc.smedia.until.ParseMusicInfo;
import com.dc.smedia.until.Until;
import com.dc.smedia.usblistener.StorageListener;
import com.dc.smedia.usblistener.StorageListener.MediaPlugCallback;

public class MusicPlayerActivity extends BaseActivity implements
		OnSeekBarChangeListener, OnClickListener {

	private PlayAudioFileTool mPaft = PlayAudioFileTool.getPaft();
	private SeekBar mSeekBar;
	private String musicTitle;
	private long mCurrentTime, mTotalTime;
	private  MarqueeTextView mTitle, mArtist, mAlbum;
	private Bitmap mBitmap;

	private int nPosition = 1;
	private final static int Msg_Refresh = 0;
	private boolean seekTouch = false;
	private int selectMode = ControlMusicMode.MODE_CIRCLE;

	private ToPlayData fToplay = new ToPlayData();

	private TextView btn_music_list, btn_music_prev, btn_music_next, btn_music_mode;
	private ImageButton btn_music_play;
	private ImageView album_photo,btn_eq;
	private TextView music_ctime,music_ttime;
	
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Msg_Refresh:
				handler.removeMessages(Msg_Refresh);
				if (mTotalTime > 0) {
					//判断当前播放是否准备
					if(PlayAudioFileTool.mIsPrepared){
						mCurrentTime = mPaft.getCurrentTime();
						mTotalTime = mPaft.getTotalTime();
						if (mTotalTime != 0){
							if(!mSeekBar.isEnabled()){
								mSeekBar.setEnabled(true);
							}
							mSeekBar.setProgress((int) (1000 * mCurrentTime / mTotalTime));
							music_ctime.setText(ParseMusicInfo.convertDurationToTime(""+mCurrentTime));
							music_ttime.setText(ParseMusicInfo.convertDurationToTime(""+mTotalTime));
						}
					}else{
						if(mSeekBar.isEnabled()){
							mSeekBar.setEnabled(false);	
						}
						mSeekBar.setProgress(0);
						music_ctime.setText(ParseMusicInfo.convertDurationToTime(""+0));
						music_ttime.setText(ParseMusicInfo.convertDurationToTime(""+0));
					}
					
						
						
				} else {
					if(PlayAudioFileTool.mIsPrepared){
						mCurrentTime = mPaft.getCurrentTime();
						mTotalTime = mPaft.getTotalTime();
					}
					
				}
				if(mPaft.isPlaying()){
					btn_music_play.setBackgroundResource(R.drawable.btn_music_pause);
				}else{
					btn_music_play.setBackgroundResource(R.drawable.btn_music_play);	
				}
				handler.sendEmptyMessageDelayed(Msg_Refresh, 500);
				break;

			default:
				break;
			}
		};
	};
	private Object sourceTocken = null;
	private final String TAG = "MusicPlayActivity";
	private boolean pausedByAudioFocus;
	
	public static boolean isLocal;
	private long currentTime;
	
	@Override
	protected void onCreate(android.os.Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.music_player);
		VLCApplication.getInst().addActivity(this);
		initData();
		initUI();
		
		handler.sendEmptyMessage(Msg_Refresh);
		
		getData();
		StorageListener.getInstance().setMediaPlugCallback(new MediaPlugCallback() {
			
			@Override
			public void onMediaPlug(String devPath, boolean insert) {
				// TODO Auto-generated method stub
				if(!insert){
					if(MediaSelectControl.getInst().isUsb()){
						//跳转到
						Intent intent =new Intent(MusicPlayerActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
					}
					
				}
			}
		});
		
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isLocal=MediaSelectControl.getInst().isLocal();
		//设置最终播放为音乐
		MediaSaveControl.getInst().setFinalMedia(MediaSaveControl.finalMusic);
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
		String path = null;
		try {
			 path = getIntent().getStringExtra("music_path");
			 nPosition = getIntent().getIntExtra("music_position", 1);
			 currentTime=getIntent().getLongExtra("Media_Current",0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String CPlayPath=mPaft.getCurrentPath();
		if (path.equals(CPlayPath)) {
			if (mPaft.isPlaying()) {
				
			} else {
				mPaft.play_pause();
			}
			
			return ;
		}
		if (path != null) {
			
			fToplay.path =path;
			fToplay.nPosition=(int) currentTime;
			mPaft.startPlay(fToplay);
		}
	}

	@SuppressLint("NewApi")
	public void setID3() {
		
		mCurrentTime = mPaft.getCurrentTime();
		mTotalTime = mPaft.getTotalTime();
		musicTitle = Until.PathtoTitle(mPaft.getCurrentPath());
		mBitmap = ArtworkUtils.setArtwork(VLCApplication.getAppContext(),
				mPaft.getCurrentPath());
		if(mBitmap!=null){
			album_photo.setBackground(Until.bitmap2Drawable(mBitmap));
		}else{
			album_photo.setBackgroundResource(R.drawable.ic_musiclogo);
		}
		
		if (mTitle != null) {
			mTitle.setText(Until.PathtoTitle(mPaft.getCurrentPath()) + "");
			if (mPaft.getAlbum() != null&&(!mPaft.getAlbum().isEmpty())) {
				mAlbum.setText(mPaft.getAlbum());
			} else {
				mAlbum.setText("未知专辑");
			}
			if (mPaft.getArtist() != null && (!mPaft.getArtist().isEmpty())) {
				mArtist.setText(mPaft.getArtist());
			} else {
				mArtist.setText("未知艺术家");
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent =new Intent(MusicPlayerActivity.this,MusicListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
//		super.onBackPressed();
	}
	
	private void initUI() {

		mSeekBar = (SeekBar) findViewById(R.id.music_seekbar);
		mTitle = (MarqueeTextView) findViewById(R.id.music_title);
		mArtist = (MarqueeTextView) findViewById(R.id.music_artist);
		mAlbum = (MarqueeTextView) findViewById(R.id.music_album);
		music_ctime =(TextView) findViewById(R.id.music_ctime);
		music_ttime =(TextView) findViewById(R.id.music_ttime);

		album_photo =(ImageView) findViewById(R.id.album_photo);
		btn_eq =(ImageView) findViewById(R.id.btn_eq);
		
		btn_music_list = (TextView) findViewById(R.id.btn_music_list);
		btn_music_prev = (TextView) findViewById(R.id.btn_music_prev);
		btn_music_play = (ImageButton) findViewById(R.id.btn_music_play);
		btn_music_next = (TextView) findViewById(R.id.btn_music_next);
		btn_music_mode = (TextView) findViewById(R.id.btn_music_mode);

		mSeekBar.setOnSeekBarChangeListener(this);
		btn_music_list.setOnClickListener(this);
		btn_music_prev.setOnClickListener(this);
		btn_music_play.setOnClickListener(this);
		btn_music_next.setOnClickListener(this);
		btn_music_mode.setOnClickListener(this);
		btn_eq.setOnClickListener(this);

		mTitle.setText(Until.PathtoTitle(mPaft.getCurrentPath()) + "");
		if (mPaft.getAlbum() != null) {
			mAlbum.setText(mPaft.getAlbum());
		} else {
			mAlbum.setText("未知专辑");
		}
		if (mPaft.getArtist() != null) {
			mArtist.setText(mPaft.getArtist());
		} else {
			mArtist.setText("未知艺术家");
		}
		setLoopButtonImage();
		
		mPaft.setCallBack(new MusicCallBack() {
			
			@Override
			public void newSongPlayed() {
				// TODO Auto-generated method stub
				setSeekbar(false);
				setID3();
			}

			@Override
			public void ListPosition() {
				
				
			}
		});
	};

	private void setSeekbar(boolean enable){
		if(enable){
			mSeekBar.setEnabled(true);
		}else{
			//设置seekbar
			mSeekBar.setProgress(0);
			mSeekBar.setEnabled(false);
			music_ctime.setText(ParseMusicInfo.convertDurationToTime(""+0));
			music_ttime.setText(ParseMusicInfo.convertDurationToTime(""+0));
		}		
	}
	

    private void setLoopButtonImage() {
        if (ControlMusicMode.getMusicMode() == ControlMusicMode.MODE_CIRCLE) {
            Drawable drawable = getResources().getDrawable(R.drawable.btn_music_circle);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                    .getMinimumHeight());
            btn_music_mode.setCompoundDrawables(null, drawable, null, null);
            btn_music_mode.setText(R.string.str_btn_loop);
        } else if (ControlMusicMode.getMusicMode() == ControlMusicMode.MODE_RANDOM) {
            Drawable drawable = getResources().getDrawable(R.drawable.btn_music_random);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                    .getMinimumHeight());
            btn_music_mode.setCompoundDrawables(null, drawable, null, null);
            btn_music_mode.setText(R.string.str_btn_rand);
        } else if (ControlMusicMode.getMusicMode() == ControlMusicMode.MODE_SINGLE) {
            Drawable drawable = getResources().getDrawable(R.drawable.btn_music_single);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                    .getMinimumHeight());
            btn_music_mode.setCompoundDrawables(null, drawable, null, null);
            btn_music_mode.setText(R.string.str_btn_loop_current);
        }
    }


	private void initData() {
		mPaft.vlcap.initAudioPlayer(VLCApplication
				.getAppContext());
		selectMode = ControlMusicMode.getMusicMode();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean arg2) {
		// TODO Auto-generated method stub
		if (seekTouch) {
			mPaft.seekTo(mTotalTime * progress / 1000);
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

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_music_list:
			intent =new Intent(MusicPlayerActivity.this,MusicListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(intent);
			break;
		case R.id.btn_music_prev:
			
			fToplay.path=mPaft.getCurrentPath();
			mPaft.play_Prev(fToplay);
			break;
		case R.id.btn_music_play:
			
			mPaft.play_pause();
//			if(mPaft.isPlaying()){
//				btn_music_play.setBackgroundResource(R.drawable.btn_music_pause);
//			}else{
//				btn_music_play.setBackgroundResource(R.drawable.btn_music_play);	
//			}
			break;
		case R.id.btn_music_next:
			
			fToplay.path=mPaft.getCurrentPath();
			mPaft.play_Next(fToplay);
			
			break;
		case R.id.btn_music_mode:
			if(ControlMusicMode.getMusicMode()==ControlMusicMode.MODE_CIRCLE){
				SystemProperties.set("persist.sys.music_mode",ControlMusicMode.MODE_RANDOM+"");
			}else if(ControlMusicMode.getMusicMode()==ControlMusicMode.MODE_RANDOM){
				SystemProperties.set("persist.sys.music_mode",ControlMusicMode.MODE_SINGLE+"");
			}else if(ControlMusicMode.getMusicMode()==ControlMusicMode.MODE_SINGLE){
				SystemProperties.set("persist.sys.music_mode",ControlMusicMode.MODE_CIRCLE+"");
			}
			setLoopButtonImage();
			break;
		case R.id.btn_eq:
			intent =new Intent();
			intent.putExtra("toEq",true);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent.setComponent(new ComponentName("com.dc.setting", "com.dc.setting.MainActivity"));
			startActivity(intent);

			break;
		default:
			break;
		}

	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getIntent().removeExtra("music_path");
		getIntent().removeExtra("music_position");
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		handler.removeMessages(Msg_Refresh);
		super.onDestroy();
		
		
		
	}

}
