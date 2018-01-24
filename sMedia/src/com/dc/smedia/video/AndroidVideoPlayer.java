package com.dc.smedia.video;

import java.io.File;
import java.io.IOException;

import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.dc.smedia.MainActivity;
import com.dc.smedia.MediaPlayFileControl;
import com.dc.smedia.R;
import com.dc.smedia.ToPlayData;
import com.dc.smedia.until.Until;
import com.dc.smedia.until.Until.MediaPath;

public class AndroidVideoPlayer implements IVideoPlayFile,
		OnCompletionListener, OnErrorListener, OnPreparedListener {
	private SurfaceView msurface;
	public static boolean surfaceViewBeCreated;
	private static ToPlayData ftoplay;
	private MediaPlayer mplayer;
	

	@Override
	public long getCurrentPosition() {
		// TODO Auto-generated method stub
		if (mplayer != null) {
			return mplayer.getCurrentPosition();
		}
		return 0;
	}

	@Override
	public void seekTo(long i) {
		// TODO Auto-generated method stub
		if (mplayer != null) {
			mplayer.seekTo((int) i);
		}
	}

	@Override
	public long getDuration() {
		// TODO Auto-generated method stub
		if (PlayVideoFileTool.mIsPrepared) {
			if (mplayer != null) {
				return mplayer.getDuration();
			}
		}
		return 0;
	}

	@Override
	public void setSurfaceLocation(Rect r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopPlay() {
		// TODO Auto-generated method stub
		if (mplayer != null) {
			mplayer.setOnCompletionListener(null);
			// mplayer.stop();
			mplayer.reset();
			mplayer = null;
		}
//		ftoplay=null;
		
	}

	@Override
	public boolean isInited() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		if (mplayer != null) {
			return mplayer.isPlaying();
		}
		return false;
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
		msurface = sv;
		msurface.getHolder().addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				surfaceViewBeCreated = false;
				if (null != mplayer) {
					try {
						mplayer.setDisplay(null);

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				surfaceViewBeCreated = true;
				if (holder != null && mplayer != null)

					mplayer.setDisplay(holder);
				// if (savedPosition > 0) {//如果记录的数据有播放进度。
				// try {
				// mplayer.reset();
				// mplayer.setDataSource(ftoplay);
				// mplayer.setDisplay(holder);
				// mplayer.prepareAsync();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// }
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {

				Log.e("tag", "videoSurfaceview" + "width:" + width + "height"
						+ height);
			}

		});

	}

	private void startPlayAfterSurfaceCreate() {
		// 等待窗口创建完成后再start

	}

	@Override
	public boolean isIsPrepared() {
		// TODO Auto-generated method stub
		return PlayVideoFileTool.mIsPrepared;
	}

	@Override
	public void playPauseToggle(boolean forceplay) {
		// TODO Auto-generated method stub
		if (mplayer != null) {
			if (forceplay) {
				mplayer.start();
			} else {
				mplayer.pause();
			}
		}
	}

	@Override
	public void startPlay(ToPlayData mtoplay) {
		ftoplay = mtoplay;
		initVideoPlayer(ftoplay.path);

	}

	@Override
	public void setSurfaceSize(int width, int height, int visible_width,
			int visible_height, int sar_num, int sar_den) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createSurfaceView(FrameLayout mframe) {
		msurface = (SurfaceView) mframe.findViewById(R.id.ap_surface);
		// msurface.setZOrderOnTop(true);
		
		installSuerfaceView(msurface);

	}

	private void initVideoPlayer(String path) {
		if (path == null)
			return;
		if (!new File(path).exists()) {
			return;
		}
		PlayVideoFileTool.mIsPrepared = false;
		if (mplayer != null) {
			mplayer.reset();
		}

		mplayer = new MediaPlayer();
		mplayer.setOnCompletionListener(this);
		mplayer.setOnErrorListener(this);
		mplayer.setOnPreparedListener(this);

		try {
			mplayer.setDataSource(path);
			mplayer.setDisplay(msurface.getHolder());
			mplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mplayer.prepareAsync();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// PlayVideoFileTool.useWTP = PlayVideoFileTool.useVP;
			// PlayVideoFileTool.getPft().ap.hide();
			// PlayVideoFileTool.getPft().vp.startPlay(ftoplay);
		}

	}

	@Override
	public void onPrepared(MediaPlayer mplayer) {
		// TODO Auto-generated method stub
		PlayVideoFileTool.mIsPrepared = true;
		mplayer.start();
		if(ftoplay.nPosition!=0){
			mplayer.seekTo(ftoplay.nPosition);
		}

	}

	@Override
	public boolean onError(MediaPlayer mplayer, int what, int extra) {
		// TODO Auto-generated method stub
		Log.d("tag", "VideoOnError - Error code: " + what + " Extra code: "
				+ extra);
		if (!PlayVideoFileTool.mIsPrepared) {
			PlayVideoFileTool.mIsPrepared = false;
//			PlayVideoFileTool.getPft().ap.hide();
			PlayVideoFileTool.getPft().ap.stopPlay();
			
			PlayVideoFileTool.useWTP = PlayVideoFileTool.useVP;
			
			PlayVideoFileTool.getPft().vp.startPlay(ftoplay);
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mplayer) {
		// TODO Auto-generated method stub
		if (!PlayVideoFileTool.mIsPrepared) {
			return;
		}
		MediaPlayFileControl.getInst().setLoop(true);
		String path = ControlVideoMode.ResolveVideoMode(ftoplay.path);
		ftoplay.path=path;
		ftoplay.nPosition=0;
		startPlay(ftoplay);
	}

	public void hide() {
		// TODO Auto-generated method stub
		if (msurface != null) {
			msurface.setVisibility(View.INVISIBLE);
			Log.e("tag", "surfaceView存在安卓hide执行");
		}
	}

	@Override
	public void suspendPlay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resumePlay() {
		// TODO Auto-generated method stub

	}

}
