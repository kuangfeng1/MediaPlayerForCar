package com.dc.smedia.music;

import java.io.File;
import java.util.ArrayList;

import org.videolan.vlc.VLCApplication;
import org.videolan.vlc.util.MyListView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

import com.dc.smedia.BaseActivity;
import com.dc.smedia.MainActivity;
import com.dc.smedia.MediaSelectControl;
import com.dc.smedia.R;
import com.dc.smedia.ToPlayData;
import com.dc.smedia.listposition.MListPosition;
import com.dc.smedia.scanner.MediaDataControl;
import com.dc.smedia.scanner.MediaSCtrol;
import com.dc.smedia.scanner.MediaScanner;
import com.dc.smedia.until.ForFile;
import com.dc.smedia.until.Until;
import com.dc.smedia.until.Until.MediaPath;

public class MusicListActivity extends BaseActivity {
//	private static ArrayList<MusicItem> mItems = new ArrayList<MusicItem>();
//	private static ArrayList<MusicItem> mLocalItems = new ArrayList<MusicItem>();
//	private MusicItem mItem;
	private static MusicListAdapter madapter;
	private static MyListView mMusicList;

	private AsyncMusicTask mTask;
	
	private ImageButton refresh;
	
	private static int nPosition=0;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.music_list);
		VLCApplication.getInst().addActivity(this);
		
		initUI();
		initData();
		
//		MediaPlayFileControl.getInst().setmMCPosition(new MediaCPosition() {
//			
//			@Override
//			public void setCPosition(int position, String txtpath) {
//				// TODO Auto-generated method stub
//				if(txtpath.equals(MediaPath.lmusicpath) ||
//						txtpath.equals(MediaPath.musicpath)){
//					nPosition =position;
//				}
//			}
//		});
		
	}
	@Override
	protected void onResume() {
		super.onResume();
//		if (MediaSelectControl.getInst().isLocal()) {
//			if (MediaDataControl.getInst().mMLocalItems.size() != 0) {
//			updataMusicList();
//			}
//		} else {
//			if (MediaDataControl.getInst().mMItems.size() != 0) {
			updataMusicList();
//			}
//		}
	};

	ToPlayData ftoplay = new ToPlayData();

	private void initUI() {
		mMusicList = (MyListView) findViewById(R.id.music_list);
		refresh =(ImageButton) findViewById(R.id.refresh_music);
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!MediaSCtrol.getInst().isMusicScanState()) {
					if (mTask == null) {
						mTask = new AsyncMusicTask(MediaSelectControl.getInst().isLocal());
					}
					mTask.cancel(true);
					MediaSCtrol.getInst().setMusicScanState(true);
					MediaScanner.getInst().startScan(MediaSelectControl.getInst().isLocal());
				}
			};
		});
		mMusicList.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 暂停视频播放，然后播放音乐
				// PlayVideoFileTool.getPft().play_pause(false);
				if (MediaSelectControl.getInst().isLocal()) {
					ftoplay.path = MediaDataControl.getInst().mMLocalItems.get(position).getMusic_path();
					MListPosition.getInst().setMLPosition(position+1);
				} else {
					ftoplay.path = MediaDataControl.getInst().mMItems.get(position).getMusic_path();
					MListPosition.getInst().setMPosition(position+1);
				}
				
				Intent intent =new Intent(MusicListActivity.this,MusicPlayerActivity.class);
				intent.putExtra("music_path", ftoplay.path);
				intent.putExtra("music_position", position+1);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		});
	}

	private void initData() {
		if (madapter == null) {
			madapter = new MusicListAdapter(VLCApplication.getAppContext());
		}
		mMusicList.setAdapter(madapter);
		Until.setScroll(mMusicList, true);
		
		MediaScanner.getInst().setMusicListActivity(this);
		
//		StorageListener.getInstance().setMediaPlugCallback(new MediaPlugCallback() {
//			
//			@Override
//			public void onMediaPlug(String devPath, boolean insert) {
//				// TODO Auto-generated method stub
//				if(!insert){
//					if (MediaDataControl.getInst().mMItems != null) {
//						MediaDataControl.getInst().mMItems.clear();
//					}
//					updataMusicList();
//				}
//			}
//		});
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		Intent intent = new Intent(MusicListActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
		startActivity(intent);
	}
	
//	public void updateMusicData(File f,boolean islocal) {
//		if(islocal){
//			mItem = new MusicItem();
//			mItem.setMusic_path(f.getPath());
//			mLocalItems.add(mItem);	
//			// 追加方式写入文件
//			try {
//				ForFile.writeFileContent(MediaPath.lmusicpath, mItem.getMusic_path());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}else{
//			mItem = new MusicItem();
//			mItem.setMusic_path(f.getPath());
//			mItems.add(mItem);	
//			// 追加方式写入文件
//			try {
//				ForFile.writeFileContent(MediaPath.musicpath, mItem.getMusic_path());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		
//		
//	}
	

	public void updataMusicList() {
		if (mMusicList!=null) {
			if(MediaSelectControl.getInst().isLocal()){
				madapter.updateListView(MediaDataControl.getInst().mMLocalItems);	
				nPosition =MListPosition.getInst().getMLPosition();
			}else{
				madapter.updateListView(MediaDataControl.getInst().mMItems);
				nPosition =MListPosition.getInst().getMPosition();
			}
			madapter.notifyDataSetChanged();
			
			if(nPosition >2){
				mMusicList.setSelectionFromTop(nPosition-2, 0);
			}
			mMusicList.invalidate();
		}
	}

	public void newAsyncMusicTask(File f,boolean local) {
		mTask = new AsyncMusicTask(local);
		mTask.execute(f);
	}

	private final class AsyncMusicTask extends AsyncTask<File, Integer, String> {
		boolean islocal;
		public AsyncMusicTask(boolean local) {
			// TODO Auto-generated constructor stub
			islocal =local;
		}

		// int position;
		protected void onPreExecute() { // 运行在UI线程
			
		}

		protected String doInBackground(File... params) {// 在子线程中执行

			if (!MediaSCtrol.getInst().isMusicScanState()) {
				return null;
			}
			if (isCancelled()) {
				return null;
			}
			
			MediaDataControl.getInst().updateMusicData(params[0],islocal);
			
			

			Log.e("tag", "AsyncMediaTask:doInBackground" + params[0]);

			return "scanend";
		}

		protected void onPostExecute(String result) {// 运行在UI线程
			if (!MediaSCtrol.getInst().isMusicScanState()) {
				return;
			}
			updataMusicList();
			MediaScanner.mMusic_Task++;
			
			if (MediaScanner.mMusic_Task == MediaScanner.Music_Num) {
				cancel(true);
				MediaSCtrol.getInst().setMusicScanState(false);
				Log.e("tag",MediaScanner.mMusic_Task+":"+MediaScanner.Music_Num);
			}
			Log.e("tag", "AsyncMediaTask:onPostExecute"+MediaScanner.mMusic_Task+":"+MediaScanner.Music_Num);
		}

		protected void onProgressUpdate(Integer... values) {// 运行在UI线程

			Log.e("tag", "AsyncMediaTask:onProgressUpdate" + values);
		}

	}
	
	public void clearMusicList() {
		MediaScanner.mMusic_Task = 0;
		MediaScanner.Music_Num = 0;
		MediaScanner.Media_Num = 0;

		if(MediaSelectControl.getInst().isLocal()){
			ForFile.deleteFile(MediaPath.lmusicpath);
			if (MediaDataControl.getInst().mMLocalItems != null) {
				MediaDataControl.getInst().mMLocalItems.clear();
			} else {
				MediaDataControl.getInst().mMLocalItems = new ArrayList<MusicItem>();
			}
			if(madapter!=null){
				madapter.updateListView(MediaDataControl.getInst().mMLocalItems);
				madapter.notifyDataSetChanged();
			}	
		}else{
			ForFile.deleteFile(MediaPath.musicpath);
			if (MediaDataControl.getInst().mMItems != null) {
				MediaDataControl.getInst().mMItems.clear();
			} else {
				MediaDataControl.getInst().mMItems = new ArrayList<MusicItem>();
			}
			if(madapter!=null){
				madapter.updateListView(MediaDataControl.getInst().mMItems);
				madapter.notifyDataSetChanged();
			}
		}
	}

}
