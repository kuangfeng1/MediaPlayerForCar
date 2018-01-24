package com.dc.smedia.video;

import com.dc.smedia.ToPlayData;

import android.graphics.Rect;
import android.view.SurfaceView;
import android.widget.FrameLayout;

public interface IVideoPlayFile {
	// public void stopPlayerNative();

	public long getCurrentPosition();

	public void seekTo(long i);

	public long getDuration();

	public void setSurfaceLocation(Rect r);


	public void stopPlay();

	public boolean isInited();
	
//	public void saveCurrepPlay();

	public boolean isPlaying();

	public boolean isPlayed();

	public String getVideo_Title();
	
	public String getVideo_Path();
	//public void setPlayFile(ToPlayData tpd);

	public void installSuerfaceView(SurfaceView sv);

	public boolean isIsPrepared();
	
	public void playPauseToggle(boolean forceplay);
	
	public void startPlay(ToPlayData mtoplay);
	
	void setSurfaceSize(int width, int height, int visible_width, int visible_height, int sar_num, int sar_den);

	public void createSurfaceView(FrameLayout mframe);

	public void suspendPlay(); 
	public void resumePlay(); 
	
	public void hide();
	
}
