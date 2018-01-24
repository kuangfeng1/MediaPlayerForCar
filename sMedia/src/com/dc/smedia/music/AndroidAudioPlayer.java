package com.dc.smedia.music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

import com.dc.smedia.ToPlayData;

public class AndroidAudioPlayer implements IAudioPlayFile,OnPreparedListener,OnErrorListener,OnCompletionListener {

	private MediaPlayer mplayer;
	
	private  ToPlayData ftoplay=new ToPlayData();
	private	ToPlayData nextToplay=new ToPlayData();
	private void initMusicPlayer(ToPlayData mtoplay) {
		PlayAudioFileTool.mIsPrepared=false;
		if (mplayer == null) {
			mplayer = new MediaPlayer();
		} else {
			mplayer.reset();
		}
		try {
			PlayAudioFileTool.useAZAudioPlayer=true;
			mplayer.setDataSource(mtoplay.path);
			mplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mplayer.prepareAsync();
			mplayer.setOnPreparedListener(this);
			mplayer.setOnCompletionListener(this);
			mplayer.setOnErrorListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			PlayAudioFileTool.useAZAudioPlayer=false;
//			startVlcPlayer(ftoplay.path);
		}
		
	}
	
	

	@Override
	public long getLength() {
		// TODO Auto-generated method stub
		if(mplayer!=null){
			return mplayer.getDuration();
		}
		return -1;
	}

	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		if(mplayer!=null){
			return mplayer.getCurrentPosition();
		}
		return -1;
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		if(mplayer!=null){
			return mplayer.isPlaying();
		}
		return false;
	}

	@Override
	public void startPlay(ToPlayData mftoplay) {
		// TODO Auto-generated method stub
		Log.e("tag","设置当前要播放的音乐"+ftoplay.path);
		ftoplay =mftoplay;
		
		initMusicPlayer(ftoplay);
	}
	
//	private void startVlcPlayer(String path){
//		MusicPlayerActivity.setID3();
//		
//		PlayAudioFileTool.getPaft().vlcap.startPlay(ftoplay);
//	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		if(mplayer!=null){
			mplayer.stop();
			mplayer.release();
			mplayer =null;
		}
		//清掉记忆文件
//		ftoplay.path=null;
	}

	@Override
	public void seekTo(long time) {
		// TODO Auto-generated method stub
		if(mplayer!=null){
			mplayer.seekTo((int) time);
		}
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		if(mplayer!=null){
			if(mplayer.isPlaying()){
				mplayer.pause();
			}else{
				mplayer.start();
			}
		}
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		//获取当前播放文件：
		Log.e("tag","当前播放"+ftoplay.path);
		return ftoplay.path;
	}

	@Override
	public void onCompletion(MediaPlayer mplayer) {
		// TODO Auto-generated method stub
		if (!PlayAudioFileTool.mIsPrepared) {
			return;
		}
		//执行下一曲
		String nextPath=ControlMusicMode.ResolveMusicMode(ftoplay.path);
		if(nextPath==null){
			return;
		}
		nextToplay.path=nextPath;
		
		PlayAudioFileTool.getPaft().startPlay(nextToplay);
		
	}

	@Override
	public boolean onError(MediaPlayer mplayer, int what, int extra) {
		// TODO Auto-generated method stub
		Log.d("tag", "MusicOnError - Error code: " + what + " Extra code: " + extra);
		if(!PlayAudioFileTool.mIsPrepared){
//			PlayAudioFileTool.useAZAudioPlayer=false;
//			startVlcPlayer(ftoplay.path);
		}
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mplayer) {
		// TODO Auto-generated method stub
		PlayAudioFileTool.mIsPrepared=true;
		if(mplayer!=null){
			mplayer.start();
			if(ftoplay.nPosition!=0){
				mplayer.seekTo(ftoplay.nPosition);
			}
		}
	}



	@Override
	public void clearfPlay() {
		// TODO Auto-generated method stub
		ftoplay.path=null;
	}



	



}
