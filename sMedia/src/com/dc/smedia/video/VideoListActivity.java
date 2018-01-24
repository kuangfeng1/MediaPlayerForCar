package com.dc.smedia.video;

import java.io.File;
import java.util.ArrayList;

import org.videolan.vlc.VLCApplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.dc.smedia.BaseActivity;
import com.dc.smedia.MainActivity;
import com.dc.smedia.MediaSelectControl;
import com.dc.smedia.R;
import com.dc.smedia.ToPlayData;
import com.dc.smedia.btmusic.MainApp;
import com.dc.smedia.listposition.MListPosition;
import com.dc.smedia.scanner.MediaDataControl;
import com.dc.smedia.scanner.MediaSCtrol;
import com.dc.smedia.scanner.MediaScanner;
import com.dc.smedia.until.ForFile;
import com.dc.smedia.until.Until;
import com.dc.smedia.until.Until.MediaPath;

public class VideoListActivity extends BaseActivity {
	
//	private static ArrayList<VideoItem> mItems =new ArrayList<VideoItem>();
//	private static ArrayList<VideoItem> mLocalItems =new ArrayList<VideoItem>();
//	private VideoItem mItem;
	private static VideoListAdapter madapter;
	private static ListView mVideoList;
	
	private AsyncVideoTask mTask;
	ToPlayData ftoplay=new ToPlayData();
	private ImageButton refresh_video;
	private static int nPosition ;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.video_list);
		VLCApplication.getInst().addActivity(this);
		initUI();
		initData();
		
		MediaScanner.getInst().setVLActivity(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (MediaSelectControl.getInst().isLocal()) {
			if (MediaDataControl.getInst().mVLocalItems.size() != 0) {
				updataVideoList();
			}
		} else {
			if (MediaDataControl.getInst().mVItems.size() != 0) {
				updataVideoList();
			}
		}
	}
	@SuppressLint("NewApi")
	private void initUI() {
		mVideoList = (ListView)findViewById(R.id.video_list);
		refresh_video =(ImageButton) findViewById(R.id.refresh_video);
		refresh_video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MediaSCtrol.getInst().setVideoScanState(true);
				MediaScanner.getInst().startScan(MediaSelectControl.getInst().isLocal());
			}
		});
		mVideoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				//暂停视频播放，然后播放音乐
				String playpath;
				if(MediaSelectControl.getInst().isLocal()){
					playpath =MediaDataControl.getInst().mVLocalItems.get(position).getVideo_path();
					MListPosition.getInst().setVLPosition(position+1);
				}else{
					playpath =MediaDataControl.getInst().mVItems.get(position).getVideo_path();
					MListPosition.getInst().setVPosition(position+1);
				}
				Intent intent =new Intent(VideoListActivity.this,VideoPlayActivity.class);
				intent.putExtra("video_path", playpath);
				intent.putExtra("video_position", position+1);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				
			}
		});
	}
	private void initData() {
		if (madapter == null) {
			madapter = new VideoListAdapter(VLCApplication.getAppContext());
		}
		mVideoList.setAdapter(madapter);	
		Until.setScroll(mVideoList,true);
		
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//			super.onBackPressed();
		Intent intent = new Intent(VideoListActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivity(intent);
	}
	
	public void updataVideoList(){
		//如果不在当前界面那么不执行刷新
		if(mVideoList!=null){
			if(MediaSelectControl.getInst().isLocal()){
				madapter.updateListView(MediaDataControl.getInst().mVLocalItems);
				nPosition =MListPosition.getInst().getVLPosition();
			}else{
				madapter.updateListView(MediaDataControl.getInst().mVItems);
				nPosition =MListPosition.getInst().getVPosition();
			}
			if(nPosition>2){
				mVideoList.setSelectionFromTop(nPosition-2, 0);
			}
			madapter.notifyDataSetChanged();
			mVideoList.invalidate();
		}
		
	}
	
	public  void newAsyncVideoTask(File f,boolean local){
		mTask= new AsyncVideoTask(local);
		mTask.execute(f);
	}
//	public void updateVideoData(File f,boolean islocal) {
//		mItem = new VideoItem();
//		mItem.setVideo_path(f.getPath());
////		mItem.setVideo_photo(getVideoThumbnail(f.getPath(), 100, 100));
//		if(islocal){
//			mLocalItems.add(mItem);
//			try {
//				ForFile.writeFileContent(MediaPath.lvideopath, mItem.getVideo_path());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}else{
//			mItems.add(mItem);
//			try {
//				ForFile.writeFileContent(MediaPath.videopath, mItem.getVideo_path());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}


	public void clearVideoList() {
		MediaScanner.mVideo_Task = 0;
		MediaScanner.Video_Num = 0;
		MediaScanner.Media_Num=0;
		if(MediaSelectControl.getInst().isLocal()){
			ForFile.deleteFile(MediaPath.lvideopath);
			if (MediaDataControl.getInst().mVLocalItems != null) {
				MediaDataControl.getInst().mVLocalItems.clear();
			} else {
				MediaDataControl.getInst().mVLocalItems = new ArrayList<VideoItem>();
			}
			if(madapter!=null){
				madapter.updateListView(MediaDataControl.getInst().mVLocalItems);
				madapter.notifyDataSetChanged();
			}	
		}else{
			ForFile.deleteFile(MediaPath.videopath);
			if (MediaDataControl.getInst().mVItems != null) {
				MediaDataControl.getInst().mVItems.clear();
			} else {
				MediaDataControl.getInst().mVItems = new ArrayList<VideoItem>();
			}
			if(madapter!=null){
				madapter.updateListView(MediaDataControl.getInst().mVItems);
				madapter.notifyDataSetChanged();
			}
		}
	}
	private final class AsyncVideoTask extends AsyncTask<File, Integer, String> {
		boolean islocal;
		public AsyncVideoTask(boolean local) {
			// TODO Auto-generated constructor stub
			islocal =local;
		}

		// int position;
		protected void onPreExecute() { // 运行在UI线程

		}

		protected String doInBackground(File... params) {// 在子线程中执行

			if(!MediaSCtrol.getInst().isVideoScanState()){
				return null;
			}
			
			if (isCancelled()) {
				return null;
			}
			
			MediaDataControl.getInst().updateVideoData(params[0],islocal);
			
			
			Log.e("tag", "AsyncMediaTask:doInBackground"+params[0]);

			return "scanend";
		}

		protected void onPostExecute(String result) {// 运行在UI线程
			if(!MediaSCtrol.getInst().isVideoScanState()){
				return ;
			}
			updataVideoList();
			MediaScanner.mVideo_Task++;
			if(MediaScanner.mVideo_Task == MediaScanner.Video_Num){
				cancel(true);
				MediaSCtrol.getInst().setVideoScanState(false);
			}
			Log.e("tag", "AsyncMediaTask:onPostExecute");
		}

		protected void onProgressUpdate(Integer... values) {// 运行在UI线程
			
			
			Log.e("tag", "AsyncMediaTask:onProgressUpdate" + values);
		}

	}


}


