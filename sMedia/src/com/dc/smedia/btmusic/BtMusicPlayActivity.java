package com.dc.smedia.btmusic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.autochips.bluetooth.LocalBluetoothProfileManager;
import com.autochips.bluetooth.LocalBluetoothProfileManager.BTProfile;
import com.autochips.bluetooth.avrcpct.BluetoothAvrcpCtPlayerManage;
import com.autochips.bluetooth.avrcpct.BluetoothAvrcpCtService;
import com.dc.smedia.BaseActivity;
import com.dc.smedia.R;

public class BtMusicPlayActivity extends BaseActivity implements
		OnClickListener {
	TextView btn_btmusic_prev, btn_btmusic_next, btn_btdevice;
	ImageButton btn_btmusic_play;
	TextView btmusic_title, btmusic_artist, btmusic_album;

	private volatile boolean isRds = false;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setAsMaxWindow();
		setContentView(R.layout.btmusic_player);
		Bluetooth.getInstance(getBaseContext());
		initUI();
		initData();
	}

	@SuppressLint("NewApi")
	public void setAsMaxWindow() {
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	@SuppressLint("NewApi")
	private void initUI() {
		btn_btmusic_prev = (TextView) findViewById(R.id.btn_btmusic_prev);
		btn_btmusic_play = (ImageButton) findViewById(R.id.btn_btmusic_play);
		btn_btmusic_next = (TextView) findViewById(R.id.btn_btmusic_next);
		btn_btdevice = (TextView) findViewById(R.id.btn_btdevice);

		btmusic_title = (TextView) findViewById(R.id.btmusic_title);
		btmusic_artist = (TextView) findViewById(R.id.btmusic_artist);
		btmusic_album = (TextView) findViewById(R.id.btmusic_album);

		btn_btmusic_prev.setOnClickListener(this);
		btn_btmusic_play.setOnClickListener(this);
		btn_btmusic_next.setOnClickListener(this);

		btn_btdevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("com.autochips.bluetooth",
						"com.autochips.bluetooth.ActivityMain"));
				startActivity(intent);
			}
		});

		if (Bluetooth.getInstance().getmediaTitle().isEmpty()) {
			btmusic_title.setText(R.string.media_title);
		} else {
			btmusic_title.setText(Bluetooth.getInstance().getmediaTitle());
		}
		if (Bluetooth.getInstance().getmediaArtist().isEmpty()) {
			btmusic_artist.setText(R.string.media_artist);
		} else {
			btmusic_artist.setText(Bluetooth.getInstance().getmediaArtist());
		}
		if (Bluetooth.getInstance().getmediaAlbum().isEmpty()) {
			btmusic_album.setText(R.string.media_album);
		} else {
			btmusic_album.setText(Bluetooth.getInstance().getmediaAlbum());
		}
	}

	@SuppressLint("NewApi")
	private void initData() {
		// TODO Auto-generated method stub

		BluetoothReceiver.registerNotifyHandler(uiHandler);
		manager_avrcp = BluetoothAvrcpCtPlayerManage
				.getInstance(getBaseContext());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (!Bluetooth.getInstance().isA2DPconnected()) {
			Bluetooth.getInstance().connectA2DP();
		} else if (!Bluetooth.getInstance().isAVRCPconnected()) {
			Bluetooth.getInstance().connectAVRCP();
		}
		super.onResume();

	}

	private Handler uiHandler = new Handler() {
		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == BluetoothReceiver.MSG_BT_STATUS_NOTIFY) {
				Intent intent = (Intent) msg.obj;
				String recievedAction = intent.getAction();
				if (recievedAction
						.equals(BluetoothAvrcpCtPlayerManage.ACTION_MEDIA_DATA_UPDATE)) {
					String mediaTitle = intent
							.getStringExtra(BluetoothAvrcpCtPlayerManage.MEDIA_TITLE);
					String mediaAritist = intent
							.getStringExtra(BluetoothAvrcpCtPlayerManage.MEDIA_ARTIST);
					String mediaAlbum = intent
							.getStringExtra(BluetoothAvrcpCtPlayerManage.MEDIA_ALBUM);
					if (mediaTitle == null || mediaTitle.isEmpty()) {
						btmusic_title.setText(R.string.media_title);
					} else {
						btmusic_title.setText(mediaTitle);
					}

					if (mediaAritist == null || mediaAritist.isEmpty()) {
						btmusic_artist.setText(R.string.media_artist);
					} else {
						btmusic_artist.setText(mediaAritist);
					}
					if (mediaAlbum == null || mediaAlbum.isEmpty()) {
						btmusic_album.setText(R.string.media_album);
					} else {
						btmusic_album.setText(mediaAlbum);
					}
				} else if (recievedAction
						.equals(BluetoothAvrcpCtPlayerManage.ACTION_PLAYBACK_DATA_UPDATE)) {
					int mediaLength = intent.getIntExtra(
							BluetoothAvrcpCtPlayerManage.MEDIA_LENGTH, 0);
					int mediaPosition = intent.getIntExtra(
							BluetoothAvrcpCtPlayerManage.MEDIA_POSITION, 0);
					// mMusicLong = mediaLength;
					// mPlayingTime = mediaPosition;
					byte play_status = intent.getByteExtra(
							BluetoothAvrcpCtPlayerManage.PLAYBACK_STATUS,
							(byte) 0);
					switch (play_status) {
					case BluetoothAvrcpCtPlayerManage.PLAYING:
						// pausebutton.setVisibility(View.VISIBLE);
						// playbutton.setVisibility(View.GONE);
						btn_btmusic_play
								.setBackgroundResource(R.drawable.btn_music_pause);
						break;

					case BluetoothAvrcpCtPlayerManage.PAUSED:
					case BluetoothAvrcpCtPlayerManage.STOPPED:
						// pausebutton.setVisibility(View.GONE);
						// playbutton.setVisibility(View.VISIBLE);
						btn_btmusic_play
								.setBackgroundResource(R.drawable.btn_music_play);
						break;
					}
					// if (mediaLength == 0) {
					// updateProgress(cmdGetPlayerStatus,mDefaultMusicLong,
					// mDefaultPlayingTime);
					// updateTimeText(mDefaultMusicLong,
					// mDefaultPlayingTime,(byte) 0);
					// } else {
					// updateProgress(cmdGetPlayerStatus,mediaLength,
					// mediaPosition);
					// updateTimeText(mediaLength, mediaPosition,play_status);
					// }
				} else if (recievedAction
						.equals(BluetoothAvrcpCtService.ACTION_BTMUSIC_INTERACTIVE)) {
					int param = intent.getIntExtra(
							BluetoothAvrcpCtService.EXTRA_BTMUSIC_INTERACTIVE,
							0);
					if (param == 15) {
						isRds = true;
					} else if (param == 0) {
						isRds = false;
					}
				} else if (recievedAction
						.equals(LocalBluetoothProfileManager.ACTION_PROFILE_STATE_UPDATE)) {
					BTProfile profilename = (BTProfile) intent
							.getSerializableExtra(LocalBluetoothProfileManager.EXTRA_PROFILE);
					int profilestate = intent.getIntExtra(
							LocalBluetoothProfileManager.EXTRA_NEW_STATE,
							LocalBluetoothProfileManager.STATE_DISCONNECTED);
					if (profilename == null) {
						return;
					}
					if (profilename
							.equals(LocalBluetoothProfileManager.BTProfile.Bluetooth_A2DP_SINK)) {
						switch (profilestate) {
						case LocalBluetoothProfileManager.STATE_DISCONNECTED:
						case LocalBluetoothProfileManager.STATE_DISABLED:
							btmusic_title.setText(R.string.media_title);
							btmusic_artist.setText(R.string.media_artist);
							btmusic_album.setText(R.string.media_album);
							break;
						}
					}
				}

			}
		}
	};
	BluetoothAvrcpCtPlayerManage manager_avrcp;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// if (!Bluetooth.getInstance().isA2DPconnected()) {
		// return;
		// }
		// if (isRds) {
		// return ;
		// }
		switch (v.getId()) {
		case R.id.btn_btmusic_prev:
//			manager_avrcp
//					.sendAvrcpCommand(BluetoothAvrcpCtPlayerManage.CMD_PREV);
			 Bluetooth.getInstance().sendAvrcpCommand(BluetoothAvrcpCtPlayerManage.CMD_PREV);
			break;
		case R.id.btn_btmusic_play:
			if (Bluetooth.getInstance().isA2DPPlaying()) {
//				manager_avrcp
//						.sendAvrcpCommand(BluetoothAvrcpCtPlayerManage.CMD_PAUSE);
				 Bluetooth.getInstance().sendAvrcpCommand(BluetoothAvrcpCtPlayerManage.CMD_PAUSE);
			} else {
//				manager_avrcp
//						.sendAvrcpCommand(BluetoothAvrcpCtPlayerManage.CMD_PLAY);
				 Bluetooth.getInstance().sendAvrcpCommand(BluetoothAvrcpCtPlayerManage.CMD_PLAY);
			}
			break;
		case R.id.btn_btmusic_next:
//			manager_avrcp
//					.sendAvrcpCommand(BluetoothAvrcpCtPlayerManage.CMD_NEXT);
			 Bluetooth.getInstance().sendAvrcpCommand(BluetoothAvrcpCtPlayerManage.CMD_NEXT);
			break;

		default:
			break;
		}
	}
}
