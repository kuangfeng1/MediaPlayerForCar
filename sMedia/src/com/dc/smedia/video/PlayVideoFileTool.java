package com.dc.smedia.video;

import org.videolan.vlc.VLCApplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;

import com.dc.smedia.MediaPlayFileControl;
import com.dc.smedia.MediaSelectControl;
import com.dc.smedia.ToPlayData;
import com.dc.smedia.music.PlayAudioFileTool;
import com.dc.smedia.music.PlayAudioFileTool.MusicCallBack;
import com.dc.smedia.music.PlayAudioFileTool.PlayAction;
import com.dc.smedia.until.Until.MediaPath;


public class PlayVideoFileTool {
	private static PlayVideoFileTool mplaytool;
	AndroidVideoPlayer ap=new AndroidVideoPlayer();
	VitamioPlayer vp=new VitamioPlayer();
	
	public static final int useAP = 0;
	public static final int useVP = 1;
	public static final int useVLCP = 2;
	public static int useWTP = 0;
	
	private MediaPlayFileControl mc=MediaPlayFileControl.getInst();
	
	private final static int Msg_SurfaceView=1;
	
	private static ToPlayData ftoplay;
	private ToPlayData cplay=new ToPlayData();
	
	public static boolean mIsPrepared =false;	
	Handler mhandler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Msg_SurfaceView:
				
				mhandler.removeMessages(Msg_SurfaceView);
				getIvpf().startPlay((ToPlayData)msg.obj);	
				if(cb!=null)
				cb.newSongPlayed();
				break;

			default:
				break;
			}
		};
	};
	
	public static PlayVideoFileTool getPft(){
		if(mplaytool ==null){
			mplaytool =new PlayVideoFileTool();
		}
		return mplaytool;
	}
	
	
	public VideoCallBack cb;
	public interface VideoCallBack {
		public void newSongPlayed();
	};
	
	public void setCallBack(VideoCallBack mcb){
		cb =mcb;
	}

	
	public IVideoPlayFile getIvpf(){
		switch (useWTP) {
		case useAP:
			vp.stopPlay();
			return ap;
		case useVP:
			
			ap.stopPlay();
			return vp;
		case useVLCP:
			
			break;
		default:
			break;
		}
		return ap;
	}
	Intent intent;
	public void setVideoState(String action){
		intent =new Intent();
		intent.setAction(action);
		VLCApplication.getAppContext().sendBroadcast(intent);
	}
	
	public void play_Prev(String path){
		if(path ==null){
			return ;
		}
		mc.setLoop(true);
		String mPath;
		if(MediaSelectControl.getInst().isLocal()){
			if(ControlVideoMode.getVideoMode() ==ControlVideoMode.MODE_RANDOM){
				mPath=mc.getRandomPlayData(path,MediaPath.lvideopath);
			}else{
				mPath=mc.getPrevPlayData(path,MediaPath.lvideopath);
			}
		}else{
			if(ControlVideoMode.getVideoMode() ==ControlVideoMode.MODE_RANDOM){
				mPath=mc.getRandomPlayData(path,MediaPath.videopath);
			}else{
				mPath=mc.getPrevPlayData(path,MediaPath.videopath);
			}
				
		}
		cplay.path=mPath;
		cplay.nPosition=0;
		
		startPlay(cplay);
	}
	
	public void play_Next(String path){
		if(path ==null){
			return ;
		}
		String ftoplay;
		//获取当前要播发的文件路径
		mc.setLoop(true);
		if(MediaSelectControl.getInst().isLocal()){
			if(ControlVideoMode.getVideoMode() ==ControlVideoMode.MODE_RANDOM){
				ftoplay=mc.getRandomPlayData(path,MediaPath.lvideopath);
			}else{
				ftoplay=mc.getNextPlayData(path,MediaPath.lvideopath);
			}
			
		}else {
			if(ControlVideoMode.getVideoMode() ==ControlVideoMode.MODE_RANDOM){
				ftoplay=mc.getRandomPlayData(path,MediaPath.videopath);
			}else{
				ftoplay=mc.getNextPlayData(path,MediaPath.videopath);	
			}
			
		}
		cplay.path=ftoplay;
		cplay.nPosition =0;
		startPlay(cplay);
	}
	
	Message message ;
	FrameLayout mframe;
	public void setSurface(FrameLayout mFrame){
		mframe=mFrame;
	}
	public void startPlay(ToPlayData mtoplay){
		if(mtoplay.path==null)
			return ;
		ftoplay =mtoplay;
		//启动服务
//		startSurfaceService();
//		createPlayer();
		PlayAudioFileTool.getPaft().stop();
		
		ap.stopPlay();
		vp.stopPlay();
		useWTP=useAP;
		
		
//		if(getIvpf().isIsPrepared()){
		if(false){
			getIvpf().startPlay(ftoplay);	
		}else{
			message= Message.obtain();
			message.obj=ftoplay;
			message.what = Msg_SurfaceView;  
			mhandler.sendMessageDelayed(message, 500);
		}
	}

	
	//启动surfaceview服务
//	private void startSurfaceService(){
//		if(!Until.isMyServiceRunning(PlayVideoService.class)){
//			try{
//				Intent intent =new Intent("com.dc.media.videoserver.PlayVideoService");
//				VLCApplication.getAppContext().startService(intent);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//	}
	
	public void stop(){
		getIvpf().stopPlay();
	}

	public void createPlayer(){
		ap.createSurfaceView(mframe);
		vp.createSurfaceView(mframe);
	}

	public long getCurrentTime() {
		// TODO Auto-generated method stub
		return getIvpf().getCurrentPosition();
	}

	public String getCurrentPath() {
		// TODO Auto-generated method stub
		return getIvpf().getVideo_Path();
	}

	public void play_pause(boolean forceplay) {
		// TODO Auto-generated method stub
		getIvpf().playPauseToggle(forceplay);
	}

	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return getIvpf().isPlaying();
	}

	public long getTotalTime() {
		// TODO Auto-generated method stub
		return getIvpf().getDuration();
	}

	public void seekTo(long l) {
		// TODO Auto-generated method stub
		getIvpf().seekTo(l);
	}

	private PlayAction mPlayAction;
	public void setPlayActivity(PlayAction mPlayAction){
		this.mPlayAction =mPlayAction;
	}
	
	private PlayAction getPlayActivity() {
		// TODO Auto-generated method stub
		return mPlayAction;
	}


}




