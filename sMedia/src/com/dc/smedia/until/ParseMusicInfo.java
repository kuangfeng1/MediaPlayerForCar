package com.dc.smedia.until;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaMetadataRetriever;

import com.dc.smedia.ToPlayData;

public class ParseMusicInfo {
//	public  static Bitmap mbitmap;
//	public static String malbum;
//	public static String martist;
//	public static int mduration;

//	private static ParseMusicInfo mInst;
//	public static ParseMusicInfo getInst(){
//		if(mInst==null){
//			mInst =new ParseMusicInfo();
//		}
//		return mInst;
//	}


	public static String convertDurationToTime(String durationMs) {
		// convert ms to (05:03:21)
		String rtnString = null;

		int ms = 0;
		try {
			ms = Integer.parseInt(durationMs);
		} catch (Exception e) {

			ms = 0;
		}
		int sec = ms / 1000;
		int minute = sec / 60;
		int hour = minute / 60;
		int second = sec % 60;
		minute %= 60;

		rtnString = String.format("%02d:%02d:%02d", hour, minute, second);
		return rtnString;
	}
	static MediaMetadataRetriever mmr;
	
	@SuppressLint("NewApi")
	public static long makeVideoInfo(Context mContext, String path) {
		if(path==null ||(!new File(path).exists())){
			return 0;
		}
		long nDuration=0;
//		mbitmap = ArtworkUtils.setArtwork(mContext, ftoplay.path);
		mmr = new MediaMetadataRetriever();
		try {
				mmr.setDataSource(path);
				nDuration=Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mmr != null) {
				mmr.release();
			}
			mmr = null;
		}
		return nDuration;
	}
	
	@SuppressLint("NewApi")
	public static ToPlayData makeSongBmpMap(Context mContext, ToPlayData ftoplay) {
		if(ftoplay.path==null ||(!new File(ftoplay.path).exists())){
			return null;
		}
//		mbitmap = ArtworkUtils.setArtwork(mContext, ftoplay.path);
		mmr = new MediaMetadataRetriever();
		try {
				mmr.setDataSource(ftoplay.path);
				ftoplay.album=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
				ftoplay.artist=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
				ftoplay.nDuration=Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mmr != null) {
				mmr.release();
			}
			mmr = null;
		}
		return ftoplay;
	}
	
	
}
