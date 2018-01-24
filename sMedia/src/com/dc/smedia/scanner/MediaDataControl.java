package com.dc.smedia.scanner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.dc.smedia.music.MusicItem;
import com.dc.smedia.music.PlayAudioFileTool;
import com.dc.smedia.photo.PhotoItem;
import com.dc.smedia.until.ForFile;
import com.dc.smedia.until.Until.MediaPath;
import com.dc.smedia.video.VideoItem;

public class MediaDataControl {
	private static MediaDataControl mInst;
	
	private VideoItem mVItem;
	public  ArrayList<VideoItem> mVLocalItems =new ArrayList<VideoItem>();
	public  ArrayList<VideoItem> mVItems =new ArrayList<VideoItem>();
	
	private MusicItem mMItem;
	public  ArrayList<MusicItem> mMLocalItems =new ArrayList<MusicItem>();
	public  ArrayList<MusicItem> mMItems =new ArrayList<MusicItem>();
	
	private PhotoItem mPItem;
	public  ArrayList<PhotoItem> mPLocalItems =new ArrayList<PhotoItem>();
	public  ArrayList<PhotoItem> mPItems =new ArrayList<PhotoItem>();
	
	public static MediaDataControl getInst() {
		if (mInst == null) {
			mInst = new MediaDataControl();
		}
		return mInst;
	}
	
//	public int searchMusicPosition(ArrayList<MusicItem> mItems ,String playpath){
//		for(int position=0;position<mItems.size();position++){
//			 if(mItems.get(position).getMusic_path().equals(PlayAudioFileTool.getPaft().getCurrentPath())){
//				 return position;
//			 }
//		}
//		return -1;
//		
//	}
//	
	public void updateVideoData(File f,boolean islocal) {
		mVItem = new VideoItem();
		mVItem.setVideo_path(f.getPath());
		if(islocal){
			mVLocalItems.add(mVItem);
			try {
				ForFile.writeFileContent(MediaPath.lvideopath, mVItem.getVideo_path());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			mVItems.add(mVItem);
			try {
				ForFile.writeFileContent(MediaPath.videopath, mVItem.getVideo_path());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void updateMusicData(File f,boolean islocal) {
		if(islocal){
			mMItem = new MusicItem();
			mMItem.setMusic_path(f.getPath());
			mMLocalItems.add(mMItem);	
			// 追加方式写入文件
			try {
				ForFile.writeFileContent(MediaPath.lmusicpath, mMItem.getMusic_path());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			mMItem = new MusicItem();
			mMItem.setMusic_path(f.getPath());
			mMItems.add(mMItem);	
			// 追加方式写入文件
			try {
				ForFile.writeFileContent(MediaPath.musicpath, mMItem.getMusic_path());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	
	public void updatePhotoData(File f,boolean islocal) {
		mPItem = new PhotoItem();
		mPItem.setPhoto_path(f.getPath());
		if(islocal){
			mPLocalItems.add(mPItem);
			try {
				ForFile.writeFileContent(MediaPath.lpicpath, mPItem.getPhoto_path());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			mPItems.add(mPItem);
			try {
				ForFile.writeFileContent(MediaPath.picpath, mPItem.getPhoto_path());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 追加方式写入文件
		
	}
	
}
