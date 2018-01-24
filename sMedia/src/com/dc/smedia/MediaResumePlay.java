package com.dc.smedia;

import org.videolan.vlc.VLCApplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaFile.MediaFileType;
import android.os.SystemProperties;

import com.dc.smedia.listposition.MListPosition;
import com.dc.smedia.music.MusicPlayerActivity;
import com.dc.smedia.scanner.MediaFileParser;
import com.dc.smedia.until.Until.MediaPath;
import com.dc.smedia.video.VideoPlayActivity;

public class MediaResumePlay {
	Context mContext = VLCApplication.getAppContext();

	private static MediaResumePlay mInst;

	String playPath;
	long currentTime;
	String mediaPath;

	public static MediaResumePlay getInst() {
		if (mInst == null) {
			mInst = new MediaResumePlay();
		}
		return mInst;
	}

	public interface MainCallBack {
		void LoadFrag(int fragment);
	};

	private MainCallBack mMainCallBack;

	public void setMainCallBack(MainCallBack mMainCallBack) {
		this.mMainCallBack = mMainCallBack;
	}

	public void resumePlay() {
		playPath = SystemProperties.get("persist.sys.firstmusic");
		currentTime = 0;
		if (MediaSelectControl.getInst().isLocal()) {
			if(MainActivity.isLscanState){
				resolveLocal(true);
			}
			MainActivity.isLscanState = false;

		} else if (MediaSelectControl.getInst().isUsb()) {
			if(MainActivity.isUscanState){
				resolveLocal(false);
			}
			MainActivity.isUscanState = false;
		}
	}

	private void resolveLocal(boolean flag) {
		int position =MediaSaveControl.getInst().getList_Position();
		if(flag){
			mediaPath =MediaPath.sdpath;
			
		}else{
			mediaPath ="/mnt/udisk1";
		
		}
		if (MediaSaveControl.getInst().getFinalMedia() == MediaSaveControl.finalMusic) {
			
			if (MediaSaveControl.getInst().getfMusicString() != null
					&& (MediaSaveControl.getInst().getfMusicString().indexOf(mediaPath) != -1)
					&& MediaFileParser.isAudioFileType(MediaSaveControl.getInst().getfMusicString())) {
				playPath = MediaSaveControl.getInst().getfMusicString();
				currentTime = MediaSaveControl.getInst().getfMusicPosition();
			}
			if (playPath != null) {
				// 记录文件，并准备播放,跳转到播放页面
				if(flag){
					MListPosition.getInst().setMLPosition(position);
				}else{
					MListPosition.getInst().setMPosition(position);
				}
				if(MediaFileParser.isVideoFileType(playPath)){
					ToVideoPlay();
				}else{
					ToMusicPlay();
				}	
			} else {
				if (mMainCallBack != null)
					if(flag){
						mMainCallBack.LoadFrag(1);
					}else{
						mMainCallBack.LoadFrag(2);
					}
					
			}
		}else if(MediaSaveControl.getInst().getFinalMedia() == MediaSaveControl.finalVideo){
			
			if (MediaSaveControl.getInst().getfVideoString() != null
					&& (MediaSaveControl.getInst().getfVideoString().indexOf(mediaPath) != -1)
					&&MediaFileParser.isVideoFileType(MediaSaveControl.getInst().getfVideoString())) {
				playPath = MediaSaveControl.getInst().getfVideoString();
				currentTime = MediaSaveControl.getInst().getfVideoPosition();
			}
			if (playPath != null) {
				// 记录文件，并准备播放,跳转到播放页面
				if(flag){
					MListPosition.getInst().setVLPosition(position);
				}else{
					MListPosition.getInst().setVPosition(position);
				}
				if(MediaFileParser.isVideoFileType(playPath)){
					ToVideoPlay();
				}else{
					ToMusicPlay();
				}	
			} else {
				if(flag){
					mMainCallBack.LoadFrag(1);
				}else{
					mMainCallBack.LoadFrag(2);
				}
			}
		}

	}

	private void ToVideoPlay() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, VideoPlayActivity.class);
		intent.putExtra("video_path", playPath);
		intent.putExtra("Media_Current", currentTime);
		intent.putExtra("video_position", 1);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

	private void ToMusicPlay() {
		Intent intent = new Intent(mContext, MusicPlayerActivity.class);
		intent.putExtra("music_path", playPath);
		intent.putExtra("Media_Current", currentTime);
		intent.putExtra("music_position", 1);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
}
