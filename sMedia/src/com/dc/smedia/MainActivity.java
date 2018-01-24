package com.dc.smedia;

import org.videolan.vlc.VLCApplication;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dc.smedia.MediaResumePlay.MainCallBack;
import com.dc.smedia.btmusic.BtMusicPlayActivity;
import com.dc.smedia.btmusic.MainApp;
import com.dc.smedia.music.MusicPlayerActivity;
import com.dc.smedia.music.PlayAudioFileTool;
import com.dc.smedia.scanner.MediaSCtrol;
import com.dc.smedia.scanner.MediaScanner;
import com.dc.smedia.until.Until.MediaPath;
import com.dc.smedia.usblistener.StorageListener;
import com.dc.smedia.usblistener.UsbAccess;
import com.dc.smedia.video.PlayVideoFileTool;

public class MainActivity extends BaseActivity implements OnClickListener {

	public Fragment fms[];
	private TextView btn_local, btn_usb, btn_aux, btn_btmusic;

	private FragmentLoading fload;
	private FragmentAux fa;
	private FragmentBtmusic fb;
	private FragmentLocal fl;
	private FragmentUsb fu;

	private View views[];

	public static boolean isLscanState = true;
	public static boolean isUscanState = true;

	public static MainActivity mi = null;

	private final static int Msg_Current = 100;

	private StorageManager mStorageManager;
	private int position;

	Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Msg_Current:
				mhandler.removeMessages(Msg_Current);
				MediaResumePlay.getInst().resumePlay();
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MainApp.addActivity(this);
		mi = this;
		initUI();
		if (mStorageManager == null) {
			mStorageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
		}
		mStorageManager.registerListener(StorageListener.getInstance());
		
		initData();
		
		MediaResumePlay.getInst().setMainCallBack(new MainCallBack() {
			
			@Override
			public void LoadFrag(int fragment) {
				// TODO Auto-generated method stub
				LoadFragment(fms[fragment]);
			}
		});

	}
	
	


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// 保存当前播放媒体信息，方便下次跳转。
		// 存储当前保存的文件是Local还是usb
		if (MediaSelectControl.getInst().isLocal()
				|| MediaSelectControl.getInst().isUsb()) {
			MediaSaveControl.getInst().saveData(false);
		}
		VLCApplication.getInst().exit();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		position = savedInstanceState.getInt("position");
		MediaSelectControl.getInst().setSelect(
				position);	
	
		LoadFragment(fms[position]);
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putInt("position", MediaSelectControl.getInst().getSelect());
	}

	private void initData() {
		//恢复记忆数据
		
		MediaSaveControl.getInst().getData();
		
		
		
		MediaSelectControl.getInst().setSelect(
				MediaSaveControl.getInst().getfMediaSelect());	
		
		if (MediaSelectControl.getInst().isLocal()) {

			if (isLscanState) {
				MediaSCtrol.getInst().setMusicScanState(true);
				MediaSCtrol.getInst().setVideoScanState(true);
				MediaSCtrol.getInst().setPhotoScanState(true);
				MediaScanner.getInst().startScan(true);

				LoadFragment(fload);
				mhandler.sendEmptyMessageDelayed(Msg_Current, 2000);
			} else {
				LoadFragment(fl);
			}
		} else if (MediaSelectControl.getInst().isUsb()) {
			if (isUscanState
					&& StorageListener.getInstance().checkStorageExist(
							getBaseContext())) {
				MediaSCtrol.getInst().setMusicScanState(true);
				MediaSCtrol.getInst().setVideoScanState(true);
				MediaSCtrol.getInst().setPhotoScanState(true);
				MediaScanner.getInst().startScan(false);

				LoadFragment(fload);
				mhandler.sendEmptyMessageDelayed(Msg_Current, 2000);
			} else {
				LoadFragment(fu);
			}
		} else if (MediaSelectControl.getInst().isAux()) {
			LoadFragment(fa);
		} else if (MediaSelectControl.getInst().isBtmusic()) {
			LoadFragment(fb);
		}

		setViewSelect(views[MediaSelectControl.getInst().getSelect()]);

	}

	public void isUsbInsert() {
//		MediaSelectControl.getInst().setSelect(MediaSelectControl.usb);
//		MediaSCtrol.getInst().setMusicScanState(true);
//		MediaSCtrol.getInst().setVideoScanState(true);
//		MediaSCtrol.getInst().setPhotoScanState(true);
//		MediaScanner.getInst().startScan(false);
//
//		LoadFragment(fload);
//		mhandler.sendEmptyMessageDelayed(Msg_Current, 2000);
//		setViewSelect(btn_usb);
		btn_usb.performClick();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (MediaSelectControl.getInst().isBtmusic()) {
			LoadFragment(fb);
		} else {
			if (!isLscanState && MediaSelectControl.getInst().isLocal()) {
				LoadFragment(fl);
			} else if (!isUscanState && MediaSelectControl.getInst().isUsb()) {
				LoadFragment(fu);
			}
		}

	}

	private void initUI() {
		fload = new FragmentLoading();
		fl = new FragmentLocal();
		fu = new FragmentUsb();
		fa = new FragmentAux();
		fb = new FragmentBtmusic();

		btn_local = (TextView) findViewById(R.id.btn_local);
		btn_usb = (TextView) findViewById(R.id.btn_usb);
		btn_aux = (TextView) findViewById(R.id.btn_aux);
		btn_btmusic = (TextView) findViewById(R.id.btn_btmusic);

		btn_local.setOnClickListener(this);
		btn_usb.setOnClickListener(this);
		btn_aux.setOnClickListener(this);
		btn_btmusic.setOnClickListener(this);

		fms = new Fragment[] { fload, fl, fu, fa, fb };
		views = new View[] { btn_local, btn_usb, btn_aux, btn_btmusic };

	}

	// public void setUsbState() {
	// if (MediaSelectControl.getInst().isLocal()) {
	// isLscanState = false;
	// LoadFragment(fl);
	// } else if (MediaSelectControl.getInst().isUsb()) {
	// isUscanState = false;
	// LoadFragment(fu);
	// }
	// }

	@SuppressLint("NewApi")
	public void LoadFragment(Fragment fm) {
		try {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			for (Fragment fmtemp : fms) {
				if (fmtemp.isAdded() && !fmtemp.isHidden()) {
					ft.hide(fmtemp);
				}
			}
			if (!fm.isAdded()) {
				ft.add(R.id.fm, fm, fm.toString());
			}
			ft.show(fm);
			ft.commitAllowingStateLoss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setViewSelect(View select) {
		for (View n : views) {
			if (n.isSelected()) {
				n.setSelected(false);
			}
		}
		select.setSelected(true);
	}

	private int getViewSelect() {
		int i = 0;
		for (View n : views) {
			i++;
			if (n.isSelected()) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public void onClick(View v) {
		setViewSelect(v);

		switch (v.getId()) {
		case R.id.btn_local:
			MediaSelectControl.getInst().setSelect(MediaSelectControl.local);
			MediaSaveControl.getInst().saveData(true);
			if (isLscanState) {
				MediaSaveControl.getInst().setFinalMedia(MediaSaveControl.finalMusic);
				LoadFragment(fload);
				MediaSCtrol.getInst().setMusicScanState(true);
				MediaSCtrol.getInst().setVideoScanState(true);
				MediaSCtrol.getInst().setPhotoScanState(true);
				MediaScanner.getInst().startScan(true);
				mhandler.sendEmptyMessageDelayed(Msg_Current, 2000);
			} else {
				LoadFragment(fl);
			}

			break;
		case R.id.btn_usb:
			
			MediaSelectControl.getInst().setSelect(MediaSelectControl.usb);
			MediaSaveControl.getInst().saveData(true);

			if (isUscanState
					&& StorageListener.getInstance().checkStorageExist(
							getBaseContext())) {
				MediaSaveControl.getInst().setFinalMedia(MediaSaveControl.finalMusic);
				LoadFragment(fload);
				MediaSCtrol.getInst().setMusicScanState(true);
				MediaSCtrol.getInst().setVideoScanState(true);
				MediaSCtrol.getInst().setPhotoScanState(true);
				MediaScanner.getInst().startScan(false);
				mhandler.sendEmptyMessageDelayed(Msg_Current, 2000);
			} else {
				LoadFragment(fu);
			}

			break;
		case R.id.btn_aux:
			PlayVideoFileTool.getPft().stop();
			PlayAudioFileTool.getPaft().stop();
			MediaSelectControl.getInst().setSelect(MediaSelectControl.aux);
			MediaSaveControl.getInst().saveData(true);
			LoadFragment(fa);
			
			break;
		case R.id.btn_btmusic:
			PlayVideoFileTool.getPft().stop();
			PlayAudioFileTool.getPaft().stop();
			if (MediaSelectControl.getInst().isBtmusic())
				return;
			MediaSelectControl.getInst().setSelect(MediaSelectControl.btmusic);
			MediaSaveControl.getInst().saveData(true);
			// 直接跳转到蓝牙音乐界面
			// LoadFragment(fb);
			Intent intent = new Intent(this, BtMusicPlayActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(intent);

			break;
		default:
			break;
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		mStorageManager.unregisterListener(StorageListener.getInstance());
		mhandler.removeMessages(Msg_Current);
	}
}
