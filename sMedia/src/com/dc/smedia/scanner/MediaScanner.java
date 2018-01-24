package com.dc.smedia.scanner;

import java.io.File;

import org.videolan.vlc.VLCApplication;

import android.content.Intent;
import android.os.SystemProperties;
import android.util.Log;

import com.dc.smedia.MainActivity;
import com.dc.smedia.music.MusicListActivity;
import com.dc.smedia.music.MusicPlayerActivity;
import com.dc.smedia.photo.PhotoListActivity;
import com.dc.smedia.until.Until.MediaPath;
import com.dc.smedia.usblistener.UsbAccess;
import com.dc.smedia.video.VideoListActivity;


public class MediaScanner {
	public static int Video_Num;
	public static int Music_Num;
	public static int Photo_Num;
	public static int Media_Num;
	
	public static int mVideo_Task;
	public static int mMusic_Task;
	public static int mPhoto_Task;
	
	private static MusicListActivity MLActivity =new MusicListActivity();
	private static VideoListActivity VLActivity=new VideoListActivity();
	private static PhotoListActivity PLActivity =new PhotoListActivity();
	
	private static MediaScanner mInst;
	public static MediaScanner getInst(){
		if(mInst ==null){
			mInst =new MediaScanner();
		}
		return mInst;
	}
	
	public void setMusicListActivity(MusicListActivity MLActivity){
		this.MLActivity =MLActivity;
	}
	public void setVLActivity(VideoListActivity vLActivity) {
		VLActivity = vLActivity;
	}
	
	private static boolean isVideoFile(String path) {
		// TODO Auto-generated method stub
		return MediaFileParser.isVideoFileType(path);

	}
	private static boolean isMusicFile(String path) {
		// TODO Auto-generated method stub
		return MediaFileParser.isAudioFileType(path);

	}
	
	private static boolean isPhotoFile(String path) {
		// TODO Auto-generated method stub
		return MediaFileParser.isImageFileType(path);

	}

	public void startScan(boolean local){
		Video_Num =0;
		Music_Num=0;
		Photo_Num=0;
		Media_Num=0;
		
		mVideo_Task=0;
		mMusic_Task=0;
		mPhoto_Task=0;
		if(MLActivity!=null){
			if(MediaSCtrol.getInst().isMusicScanState()){
				MLActivity.clearMusicList();
			}
			
		}
		if(VLActivity!=null){
			if(MediaSCtrol.getInst().isVideoScanState()){
				VLActivity.clearVideoList();
			}
			
		}
		if(PLActivity !=null){
			if(MediaSCtrol.getInst().isPhotoScanState()){
				PLActivity.clearPhotoList();
			}
		}
		
//		EventListData.MEDIA_VIDEO_LIST_inst.clearVideoList();
		String rootpath =MediaPath.sdpath;
		if(local){
			MainActivity.isLscanState=true;
			rootpath =MediaPath.sdpath;
			
		}else{
			MainActivity.isUscanState=true;
			rootpath =UsbAccess.getDir();
		}
		ScanFile(rootpath,local);
	}

	
	
	public void ScanFile(String rootpath,boolean local) {
		File files = new File(rootpath);
		
		File[] file = files.listFiles();
		try {
			for (File f : file) {
				if (f.isDirectory()) {
					ScanFile(f.getAbsolutePath(),local);
				} else {
					if (isVideoFile(f.getPath())) {
						Video_Num++;
						Media_Num++;
						VLActivity.newAsyncVideoTask(f,local);
					}else if(isMusicFile(f.getPath())){
						Music_Num++;
						Media_Num++;
						//只记录文件，扫描到的第一个文件
						if(Music_Num==1){
							MemMusic(f.getPath());
						}
						Log.e("tag","music num:"+Music_Num);
						MLActivity.newAsyncMusicTask(f,local);
					}else if(isPhotoFile(f.getPath())){
						Photo_Num++;
						Media_Num++;
						
						PLActivity.newAsyncPhotoTask(f,local);
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void MemMusic(String path) {
		SystemProperties.set("persist.sys.firstmusic",path);
		Log.e("tag","设置当前首个扫描到的曲目"+path);
	}


	
}
