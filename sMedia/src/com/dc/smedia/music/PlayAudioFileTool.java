package com.dc.smedia.music;

import org.videolan.vlc.VLCApplication;

import com.dc.smedia.MediaPlayFileControl;
import com.dc.smedia.MediaSelectControl;
import com.dc.smedia.ToPlayData;
import com.dc.smedia.until.ParseMusicInfo;
import com.dc.smedia.until.Until.MediaPath;
import com.dc.smedia.video.PlayVideoFileTool;

public class PlayAudioFileTool {
	private static PlayAudioFileTool mplaytool;
	private AndroidAudioPlayer ap =new AndroidAudioPlayer();
	public VLCAudioPlayer vlcap =new VLCAudioPlayer();
	public static boolean useAZAudioPlayer = true;
	public static boolean mIsPrepared;
	private MediaPlayFileControl mc=MediaPlayFileControl.getInst();
	
	
	ToPlayData nextToplay=new ToPlayData();
	private static ToPlayData Cplay =new ToPlayData();
	ToPlayData prevToplay=new ToPlayData();
	

	
	public static PlayAudioFileTool getPaft(){
		if(mplaytool ==null){
			mplaytool =new PlayAudioFileTool();
		}
		return mplaytool;
	}
	
	

	

	
	public MusicCallBack cb;
	public interface MusicCallBack {
		public void newSongPlayed();
		public void ListPosition();
	};
	
	public void setCallBack(MusicCallBack mcb){
		cb =mcb;
	}

	public String getCurrentPath(){
		
		return getIapf().getPath();
	}
	
	public long getCurrentTime(){
		if(mIsPrepared){
			return getIapf().getTime();
		}else{
			return 0;
		}
	}

	
	public void stop(){
		getIapf().stop();
		
	}
	
	public void clearData(){
		getIapf().clearfPlay();
	}
	
	public long getTotalTime(){
		if(mIsPrepared){
			return getIapf().getLength();
		}else{
			return 0;
		}
		
//		return Cplay.nDuration;
	}
	
	public String getAlbum(){
		return Cplay.album;
	}
	
	public String getArtist(){
		return Cplay.artist;
	}
	
	public void play_Prev(ToPlayData ftoplay){
		if(ftoplay.path ==null){
			return ;
		}
		mc.setLoop(true);
		
		if(ControlMusicMode.getMusicMode()==ControlMusicMode.MODE_RANDOM){
			if(MediaSelectControl.getInst().isLocal()){
				prevToplay.path=mc.getRandomPlayData(ftoplay.path, MediaPath.lmusicpath);
			}else{
				prevToplay.path=mc.getRandomPlayData(ftoplay.path, MediaPath.musicpath);
			}
		}else{
			if(MediaSelectControl.getInst().isLocal()){
				prevToplay.path =mc.getPrevPlayData(ftoplay.path,MediaPath.lmusicpath);
			}else{
				prevToplay.path=mc.getPrevPlayData(ftoplay.path, MediaPath.musicpath);
			}
		}
			
		
		startPlay(prevToplay);
	}
	public void play_Next(ToPlayData ftoplay){
		if(ftoplay.path ==null){
			return ;
		}
		//获取当前要播发的文件路径
		mc.setLoop(true);
		//判断是否为随机播放
		
		if(ControlMusicMode.getMusicMode()==ControlMusicMode.MODE_RANDOM){
			if(MediaSelectControl.getInst().isLocal()){
				nextToplay.path=mc.getRandomPlayData(ftoplay.path, MediaPath.lmusicpath);
			}else{
				nextToplay.path=mc.getRandomPlayData(ftoplay.path, MediaPath.musicpath);
			}
			
		}else{
			if(MediaSelectControl.getInst().isLocal()){
				nextToplay.path=mc.getNextPlayData(ftoplay.path, MediaPath.lmusicpath);
			}else{
				nextToplay.path=mc.getNextPlayData(ftoplay.path, MediaPath.musicpath);
			}
		}
		
		startPlay(nextToplay);
	}
	
	public void seekTo(long time){
		getIapf().seekTo(time);
	}
	
	public void startPlay(ToPlayData ftoplay){
		if(ftoplay.path ==null){
			return ;
		}
		PlayVideoFileTool.getPft().stop();
		ap.stop();
		vlcap.stop();
		Cplay=ParseMusicInfo.makeSongBmpMap(VLCApplication.getAppContext(), ftoplay);
		//先使用原生播放器播放
		useAZAudioPlayer=true;
		getIapf().startPlay(ftoplay);
		//
		cb.newSongPlayed();
		
	}
	public interface PlayAction{
		void setID3();
	};
	private PlayAction mPlayAction;
	public void setPlayActivity(PlayAction mPlayAction){
		this.mPlayAction =mPlayAction;
	}
	
	private PlayAction getPlayActivity() {
		// TODO Auto-generated method stub
		return mPlayAction;
	}

	public boolean isPlaying(){
		return getIapf().isPlaying();
	}
	
	public void play_pause(){
		getIapf().play();
	}
	private IAudioPlayFile getIapf() {
		// TODO Auto-generated method stub
		if(useAZAudioPlayer){
			return ap;
		}else{
			return vlcap;
		}
	}


	
	
}
