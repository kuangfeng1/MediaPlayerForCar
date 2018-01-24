package com.dc.smedia.music;

import com.dc.smedia.ToPlayData;

public interface IAudioPlayFile {
	long getLength();
	long getTime();
	boolean isPlaying();
	
	void stop();
	void seekTo(long time);
	void play();
	String getPath();
	void startPlay(ToPlayData fToplay);
	void clearfPlay();
}
