package com.dc.smedia.usblistener;

import static com.dc.smedia.usblistener.MusicConstant.UDISK2_PATH;
import static com.dc.smedia.usblistener.MusicConstant.UDISK3_PATH;
import static com.dc.smedia.usblistener.MusicConstant.UDISK4_PATH;
import static com.dc.smedia.usblistener.MusicConstant.UDISK5_PATH;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageEventListener;
import android.util.Log;

import com.autochips.storage.EnvironmentATC;
import com.dc.smedia.MainActivity;
import com.dc.smedia.MediaSaveControl;
import com.dc.smedia.MediaSelectControl;
import com.dc.smedia.music.MusicPlayerActivity;
import com.dc.smedia.music.PlayAudioFileTool;
import com.dc.smedia.scanner.MediaDataControl;
import com.dc.smedia.scanner.MediaSCtrol;
import com.dc.smedia.video.PlayVideoFileTool;
import com.dc.smedia.video.VideoPlayActivity;

public class StorageListener extends StorageEventListener {
	private static StorageListener mInstance;
	SharedPreferences mpreference1;
	Editor editor;
	Handler h = new Handler();
	Intent intent;
	private EnvironmentATC mEnv;

	public static StorageListener getInstance() {
		if (mInstance == null) {
			mInstance = new StorageListener();
		}
		return mInstance;
	}

	public boolean checkStorageExist(Context context) {
		if (mEnv == null) {
			mEnv = new EnvironmentATC(context);
		}

		String udisk1State = mEnv.getStorageState(MusicConstant.UDISK1_PATH);
		String udisk2State = mEnv.getStorageState(UDISK2_PATH);
		String udisk3State = mEnv.getStorageState(UDISK3_PATH);
		String udisk4State = mEnv.getStorageState(UDISK4_PATH);
		String udisk5State = mEnv.getStorageState(UDISK5_PATH);
		if (udisk1State.equals(Environment.MEDIA_MOUNTED)
				|| udisk2State.equals(Environment.MEDIA_MOUNTED)
				|| udisk3State.equals(Environment.MEDIA_MOUNTED)
				|| udisk4State.equals(Environment.MEDIA_MOUNTED)
				|| udisk5State.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}

		return false;
	}
	public interface MediaPlugCallback{
		void onMediaPlug(String devPath, boolean insert);
	};
	private MediaPlugCallback mediaPlugCallback;
	public void setMediaPlugCallback(MediaPlugCallback mediaPlugCallback){
		this.mediaPlugCallback = mediaPlugCallback;
	}

	@Override
	public void onStorageStateChanged(String path, String oldState,
			String newState) {
		super.onStorageStateChanged(path, oldState, newState);
		if (path.equals(UsbAccess.getDir())) {
			Log.i("usb", path + ":" + newState + "--->" + "newState");
	
			if (newState.equals(Environment.MEDIA_UNMOUNTED) 
					|| newState.equals(Environment.MEDIA_REMOVED)
					|| newState.equals(Environment.MEDIA_BAD_REMOVAL)) {
				// 记忆数据
				if (MediaSelectControl.getInst().isLocal()
						|| MediaSelectControl.getInst().isUsb()) {
					MediaSaveControl.getInst().saveData(false);
				}
				if(mediaPlugCallback!=null){
					mediaPlugCallback.onMediaPlug(path, false);
				}
				// 清除列表数据
				MediaDataControl.getInst().mMItems.clear();
				MediaDataControl.getInst().mVItems.clear();
				MediaDataControl.getInst().mPItems.clear();
				
				//所有关于usb播放暂停
				if(!VideoPlayActivity.isLocal){
					PlayVideoFileTool.getPft().stop();
				}
				if(!MusicPlayerActivity.isLocal){
					//清除临时文件
					PlayAudioFileTool.getPaft().clearData();
					PlayAudioFileTool.getPaft().stop();
				}
				//清除列表内容
				
				

			} else if (newState.equals(Environment.MEDIA_MOUNTED)) {
				MainActivity.isUscanState=true;
				MediaSaveControl.getInst().getData();
				//跳转到usb
				MainActivity.mi.isUsbInsert();
				
			}
		}
	}

}
