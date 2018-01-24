package com.dc.smedia.video;

import android.os.SystemProperties;

import com.dc.smedia.MediaPlayFileControl;
import com.dc.smedia.until.Logg;
import com.dc.smedia.until.Until.MediaPath;

public class ControlVideoMode {
	public final static int MODE_ODER =0;
	public final static int MODE_SINGLE =1;
	public final static int MODE_CIRCLE =2;
	public final static int MODE_RANDOM =3;
	
	static MediaPlayFileControl  mc =MediaPlayFileControl.getInst();
	public static int getVideoMode(){
		return SystemProperties.getInt("persist.sys.video_mode", MODE_CIRCLE);
	}
	
	public static String ResolveVideoMode(String ftoplay){
		String t=null;
		String rootPath=null;
		int playmode =getVideoMode();
		Logg.e("playmode","videoplaymode"+playmode);
		if(VideoPlayActivity.isLocal){
			rootPath=MediaPath.lvideopath;
		}else{
			rootPath=MediaPath.videopath;
		}
		if(playmode == MODE_ODER){	
			mc.setLoop(false);
			t = mc.getNextPlayData(ftoplay,rootPath);
			if (t != null) {
				return t;
			}
		}else if(playmode == MODE_SINGLE){
			
			return ftoplay;
		}else if(playmode == MODE_CIRCLE){
			mc.setLoop(true);
			t = mc.getNextPlayData(ftoplay,rootPath);
			if (t != null) {
				return t;
			}
		}else if(playmode == MODE_RANDOM){
			t = mc.getRandomPlayData(ftoplay,rootPath);
			if (t != null) {
				
				return t;
			}
		}
		return t;
		
		
	}
	
}
