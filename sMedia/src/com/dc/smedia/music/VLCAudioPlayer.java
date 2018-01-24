package com.dc.smedia.music;



import java.io.UnsupportedEncodingException;

import org.videolan.vlc.MediaLibrary;
import org.videolan.vlc.audio.AudioServiceController;

import android.content.Context;
import android.os.Handler;

import com.dc.smedia.ToPlayData;
import com.dc.smedia.until.Logg;
import com.dc.smedia.until.Until;

public class VLCAudioPlayer implements IAudioPlayFile{
	
	private  AudioServiceController mAudioController;
	MediaLibrary mMediaLibrary;
	
	
	private PlayAudioFileTool.MusicCallBack cb;

	Context context;
	public static String Music_path;


	public void initAudioPlayer(Context context){
		this.context =context;
		AudioServiceController.getInstance().bindAudioService(context);

		mAudioController = AudioServiceController.getInstance();
        mMediaLibrary = MediaLibrary.getInstance();

	}

	@Override
	public long getLength() {
		// TODO Auto-generated method stub
		if(mAudioController==null)
			return -1;
		int time=mAudioController.getLength();
//		Logg.e("tag","VLC总共时间"+time);
		return time;
	}

	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		
		if(mAudioController!=null){
			int time =mAudioController.getTime();
//			Logg.e("tag","VLC当前播放时间"+time);
			return time ;
		}
		
		return 0;
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		if(mAudioController!=null){
			if(mAudioController.isPlaying()){
				handler.removeCallbacks(run);
				mAudioController.pause();
				
				Logg.e("tag","VLCPAUSE");
			}else{
				mAudioController.play();
				Logg.e("tag","VLCPLAY");
			}
		}
	}

	Handler handler=new Handler();
	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		if(mAudioController==null)
			return false;
		return mAudioController.isPlaying();
	}

	String t =null;
	@Override
	public void startPlay(ToPlayData ftoplay) {
		// TODO Auto-generated method stub
		
		String fileName =ftoplay.path;
		Music_path= fileName;
		fileName=fileName.replaceAll("\\+","%20");
		t=Until.changeHanzi(fileName);
//		t=getURLEncoderString(fileName);
		t="file://"+t;
		mAudioController =AudioServiceController.getInstance();
		if(mAudioController==null){
			
			return;
		}	
		if(AudioServiceController.mIsBound){
			mAudioController.load(t,true);	
		}
		handler.postDelayed(run, 1000);
		
		if(cb!=null){
			cb.newSongPlayed();
		}
	}
	
	Runnable run=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!isPlaying()){
				try{
					mAudioController.load(t,true);	
				}catch(Exception e){
					e.printStackTrace();
				}
				Logg.e("tag","VLCSTARTPLAY");
			}
		}
	};

	private final static String ENCODE = "GBK"; 
	public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

	@Override
	public void stop() {
		// TODO Auto-generated method stub
//		AudioServiceController.getInstance().unbindAudioService(MainActivity.mi.getApplicationContext());
		if(mAudioController!=null){
			mAudioController.stop();
			mAudioController=null;
		}
	}

	@Override
	public void seekTo(long time) {
		if(mAudioController ==null)
			return ;
		if(mAudioController.isPlaying()){
			mAudioController.setTime(time);
		}else{
			mAudioController.play();
			mAudioController.setTime(time);
		}
		
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return Music_path;
	}

	@Override
	public void clearfPlay() {
		// TODO Auto-generated method stub
		Music_path=null;
	}


//	@Override
//	public void setNotifyHander(zhw.media.player.PlayAudioFileTool.CallBack cbSet) {
//		// TODO Auto-generated method stub
//		this.cb =(CallBack)cbSet;
//		
//	}




}
