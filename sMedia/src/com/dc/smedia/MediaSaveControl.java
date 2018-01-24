package com.dc.smedia;

import org.videolan.vlc.VLCApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dc.smedia.listposition.MListPosition;
import com.dc.smedia.music.PlayAudioFileTool;
import com.dc.smedia.until.Logg;
import com.dc.smedia.until.Until.MediaPath;
import com.dc.smedia.video.PlayVideoFileTool;

public class MediaSaveControl {
	private int finalMedia = 0;
	public final static int finalMusic = 1;
	public final static int finalVideo = 2;
	public final static int finalBtMusic = 3;

	private String fMusicString;
	private String fVideoString;
	private String fBtMusicString;

	private long fMusicPosition;
	private long fVideoPosition;
	private long fBtMusicPosition;
	
	private int List_Position;
	
	public int getList_Position() {
		return List_Position;
	}

	private int fMediaSelect;

	public int getfMediaSelect() {
		return fMediaSelect;
	}

	public void setfMediaSelect(int fMediaSelect) {
		this.fMediaSelect = fMediaSelect;
	}

	private static MediaSaveControl mInst;


	
	private SharedPreferences mpreference = VLCApplication.getAppContext()
			.getSharedPreferences("Media_Memory", Context.MODE_PRIVATE);
	Editor medit = mpreference.edit();

	public static MediaSaveControl getInst() {
		if (mInst == null) {
			mInst = new MediaSaveControl();
		}
		return mInst;
	}

	public int getFinalMedia() {
		return finalMedia;
	}

	public void setFinalMedia(int finalMedia) {
		this.finalMedia = finalMedia;
	}
	
	public String getfMusicString() {
		return fMusicString;
	}

	public void setfMusicString(String fMusicString) {
		this.fMusicString = fMusicString;
	}

	public String getfVideoString() {
		return fVideoString;
	}

	public void setfVideoString(String fVideoString) {
		this.fVideoString = fVideoString;
	}
	
	public long getfMusicPosition() {
		return fMusicPosition;
	}

	public void setfMusicPosition(long fMusicPosition) {
		this.fMusicPosition = fMusicPosition;
	}

	public long getfVideoPosition() {
		return fVideoPosition;
	}

	public void setfVideoPosition(long fVideoPosition) {
		this.fVideoPosition = fVideoPosition;
	}

	public void getData(){
		finalMedia=mpreference.getInt("Media_State", 0);
		if(finalMedia ==finalMusic){
			fMusicString=mpreference.getString("Media_Path", null);
			fMusicPosition =mpreference.getLong("Media_Current",0);
			
		}else if(finalMedia ==finalVideo){
			fVideoString=mpreference.getString("Media_Path", null);
			fVideoPosition =mpreference.getLong("Media_Current", 0);
		}
		fMediaSelect =mpreference.getInt("Media_Select",0);
		List_Position =mpreference.getInt("List_Position",0);
	}
	
	public void saveData(boolean onlySelect) {
		if (finalMedia == 0)
			return;
		
		// 保存到系统属性中
		if(!onlySelect){
			if (finalMedia == finalMusic) {
				// 查找music文件最后播放位置
				fMusicString = PlayAudioFileTool.getPaft().getCurrentPath();
				if (fMusicString == null) {
					return;
				}
				fMusicPosition = PlayAudioFileTool.getPaft().getCurrentTime();
				if(fMusicString.indexOf(MediaPath.sdpath)!=-1){
					List_Position =MListPosition.getInst().getMLPosition();
				}else{
					List_Position =MListPosition.getInst().getMPosition();
				}
				
				medit.putString("Media_Path", fMusicString);
				medit.putLong("Media_Current", fMusicPosition);
				Logg.e("MediaPlayer","MediaPlayer:"+"存储音乐文件："+fMusicString+"当前时长："+fMusicPosition);

			} else if (finalMedia == finalVideo) {
				fVideoString = PlayVideoFileTool.getPft().getCurrentPath();
				if (fVideoString == null) {
					return;
				}
				if(fVideoString.indexOf(MediaPath.sdpath)!=-1){
					List_Position =MListPosition.getInst().getVLPosition();
				}else{
					List_Position =MListPosition.getInst().getVPosition();
				}
				fVideoPosition = PlayVideoFileTool.getPft().getCurrentTime();
				
				medit.putString("Media_Path", fVideoString);
				medit.putLong("Media_Current", fVideoPosition);
				Logg.e("MediaPlayer","MediaPlayer:"+"存储视频文件："+fVideoString+"当前时长："+fVideoPosition);
				
			} else {
				// 清除记忆
//				medit.putInt("Media_State", 0);
				medit.putString("Media_Path", null);
				medit.putLong("Media_Current", 0);
			}
		}
		medit.putInt("List_Position", List_Position);
		medit.putInt("Media_State", finalMedia);
		medit.putInt("Media_Select",MediaSelectControl.getInst().getSelect());
		medit.commit();
	}
}
