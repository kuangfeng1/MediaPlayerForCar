package com.dc.smedia.scanner;

import android.content.SharedPreferences.Editor;

public class MediaSCtrol {
	private static MediaSCtrol mInst;
	public static MediaSCtrol getInst(){
		if(mInst ==null){
			mInst =new MediaSCtrol();
		}
		return mInst;
	}
	private boolean MediaScanState;
	private boolean VideoScanState;
	private boolean MusicScanState;
	private boolean PhotoScanState;
	
	public boolean isVideoScanState() {
		return VideoScanState;
	}

	public void setVideoScanState(boolean videoScanState) {
		VideoScanState = videoScanState;
	}

	public boolean isMusicScanState() {
		return MusicScanState;
	}

	public void setMusicScanState(boolean musicScanState) {
		MusicScanState = musicScanState;
	}

	public boolean isPhotoScanState() {
		return PhotoScanState;
	}

	public void setPhotoScanState(boolean photoScanState) {
		PhotoScanState = photoScanState;
	}


	public boolean isScanState() {
		return MediaScanState;
	}

	public void setScanState(boolean scanState) {
		MediaScanState = scanState;
	}
	
	private Editor medit;
	public void SaveData() {
//		SharedPreferences mpreference = MainActivity.mi.getSharedPreferences(
//				"Media_Memory", Context.MODE_PRIVATE);
//		medit = mpreference.edit();

//		if (nLastVideoOrAduioState == N_AUDIO) {
//			String apath = getPaft().getPath();
//			if (apath == null) {
//				return;
//			}
//			int position = getPaft().getCurrentPosition();
//			CRC32 mcrc32 = new CRC32();
//			mcrc32.update((apath).getBytes());
//
//			medit.putInt("Media_State", N_AUDIO);
//			medit.putString("Media_Path", getPaft().getPath());
//			medit.putInt("Media_Current", getPaft().getCurrentPosition());
//			medit.putString("Media_Artist", getPaft().getArtisit());
//			medit.putString("Media_Album", getPaft().getAlbum());
//			medit.putLong("Media_Id", getPaft().getId());
//			medit.putFloat("Media_Adjust", mcrc32.getValue());
//			Logg.e("tag", "音乐保存校验值" + mcrc32.getValue());
//			Logg.e("tag", "N_AUDIO记录播放时间" + position);
//			Logg.e("tag", "N_AUDIO记录播放文件" + apath);
//		} else if (nLastVideoOrAduioState == N_VIDEO) {
//			int position = getPft().getCurrentPosition();
//			String video_path = getPft().getVideo_Path();
//			if (video_path == null) {
//				return;
//			}
//			CRC32 mcrc32 = new CRC32();
//			mcrc32.update((video_path).getBytes());
//			medit.putInt("Media_State", N_VIDEO);
//			medit.putString("Media_Path", video_path);
//			medit.putInt("Media_Current", position);
//			medit.putLong("Media_Id", getPft().getId());
//			medit.putFloat("Media_Adjust", mcrc32.getValue());
//			Logg.e("tag", "N_VIDEO记录播放时间" + position);
//			Logg.e("tag", "N_VIDEO记录播放文件" + video_path);
//		} else {
//			CRC32 mcrc32 = new CRC32();
//			mcrc32.update(("" + 0).getBytes());
//			medit.putInt("Media_State", 0);
//			medit.putString("Media_Path", null);
//			medit.putInt("Media_Current", 0);
//			medit.putLong("Media_Id", 0);
//			medit.putFloat("Media_Adjust", mcrc32.getValue());
//			Logg.e("tag", "保存为空");
//		}
//		medit.commit();
//		Logg.e("tag", "保存成功");
	}
	

}
