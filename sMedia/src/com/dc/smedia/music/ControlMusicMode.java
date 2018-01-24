package com.dc.smedia.music;

import android.os.SystemProperties;

import com.dc.smedia.MediaPlayFileControl;
import com.dc.smedia.until.Logg;
import com.dc.smedia.until.Until.MediaPath;

public class ControlMusicMode {
	public final static int MODE_ODER =0;
	public final static int MODE_SINGLE =1;
	public final static int MODE_CIRCLE =2;
	public final static int MODE_RANDOM =3;
	
	static MediaPlayFileControl  mc =MediaPlayFileControl.getInst();
	public static int getMusicMode(){
		return SystemProperties.getInt("persist.sys.music_mode", MODE_CIRCLE);
	}
	
	public static String ResolveMusicMode(String ftoplay){
		String t=null;
		String rootPath=null;
		int playmode =getMusicMode();
		Logg.e("playmode","playmode"+playmode);
		if(MusicPlayerActivity.isLocal){
			rootPath=MediaPath.lmusicpath;
		}else{
			rootPath=MediaPath.musicpath;
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
