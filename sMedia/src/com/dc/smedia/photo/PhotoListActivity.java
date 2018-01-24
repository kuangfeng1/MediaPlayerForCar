package com.dc.smedia.photo;

import java.io.File;
import java.util.ArrayList;

import org.videolan.vlc.VLCApplication;

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
import android.widget.TextView;

import com.dc.smedia.BaseActivity;
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

public class PhotoListActivity extends BaseActivity {
	private static PicListAdapter madapter;
	private static ListView mPhotoList;
	ToPlayData ftoplay=new ToPlayData();
	private AsyncPicTask mTask;
	ImageButton refresh_photo;
	TextView no_photo;
	private int nPosition=0;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.photo_list);
		VLCApplication.getInst().addActivity(this);
		initUI();
		initData();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (MediaSelectControl.getInst().isLocal()) {
			if (MediaDataControl.getInst().mMLocalItems.size() != 0) {
				updataPhotoList();
				no_photo.setVisibility(View.GONE);
			}else{
				no_photo.setVisibility(View.VISIBLE);
				
			}
		} else {
			if (MediaDataControl.getInst().mMItems.size() != 0) {
				updataPhotoList();
				no_photo.setVisibility(View.GONE);
			}else{
				no_photo.setVisibility(View.VISIBLE);
			}
		}
	}

	private void initUI() {
		no_photo =(TextView) findViewById(R.id.no_photo);
		mPhotoList =(ListView) findViewById(R.id.photo_list);
		refresh_photo =(ImageButton) findViewById(R.id.refresh_photo);
		
		mPhotoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				//暂停视频播放，然后播放音乐
//				PlayVideoFileTool.getPft().play_pause(false);
				if(MediaSelectControl.getInst().isLocal()){
					ftoplay.path=MediaDataControl.getInst().mPLocalItems.get(position).getPhoto_path();
					MListPosition.getInst().setPLPosition(position+1);
				}else{
					ftoplay.path=MediaDataControl.getInst().mPItems.get(position).getPhoto_path();
					MListPosition.getInst().setPPosition(position+1);
				}
				Intent intent =new Intent(PhotoListActivity.this,PhotoPlayActivity.class);
				intent.putExtra("photo_path", ftoplay.path);
				intent.putExtra("photo_position", position+1);
				startActivity(intent);
			}
		});
		
		refresh_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MediaScanner.getInst().startScan(MediaSelectControl.getInst().isLocal());
			}
		});
	}

	private void initData() {
		if (madapter == null) {
			madapter = new PicListAdapter(VLCApplication.getAppContext());
		}
		mPhotoList.setAdapter(madapter);
		Until.setScroll(mPhotoList, true);
	}
	
	public void updataPhotoList() {
		if (mPhotoList!=null) {
			if(MediaSelectControl.getInst().isLocal()){
				madapter.updateListView(MediaDataControl.getInst().mPLocalItems);
				nPosition =MListPosition.getInst().getPLPosition();
			}else{
				madapter.updateListView(MediaDataControl.getInst().mPItems);
				nPosition =MListPosition.getInst().getPPosition();
			}
			if(nPosition>2){
				mPhotoList.setSelectionFromTop(nPosition-2, 0);
			}
			madapter.notifyDataSetChanged();
			mPhotoList.invalidate();
		}
	}
	
	
	public void clearPhotoList() {
		MediaScanner.mPhoto_Task = 0;
		MediaScanner.Photo_Num = 0;
		MediaScanner.Media_Num = 0;

		if(MediaSelectControl.getInst().isLocal()){
			ForFile.deleteFile(MediaPath.lpicpath);
			if (MediaDataControl.getInst().mPLocalItems != null) {
				MediaDataControl.getInst().mPLocalItems.clear();
			} else {
				MediaDataControl.getInst().mPLocalItems = new ArrayList<PhotoItem>();
			}
			if(madapter!=null){
				madapter.updateListView(MediaDataControl.getInst().mPLocalItems);
				madapter.notifyDataSetChanged();
			}	
		}else{
			ForFile.deleteFile(MediaPath.picpath);
			if (MediaDataControl.getInst().mPItems != null) {
				MediaDataControl.getInst().mPItems.clear();
			} else {
				MediaDataControl.getInst().mPItems = new ArrayList<PhotoItem>();
			}
			if(madapter!=null){
				madapter.updateListView(MediaDataControl.getInst().mPItems);
				madapter.notifyDataSetChanged();
			}
		}
		
	}

	
	
	public void newAsyncPhotoTask(File f,boolean local) {
		mTask = new AsyncPicTask(local);
		mTask.execute(f);
	}
	
	private final class AsyncPicTask extends AsyncTask<File, Integer, String> {
		boolean islocal;
		public AsyncPicTask(boolean local) {
			// TODO Auto-generated constructor stub
			islocal =local;
		}

		// int position;
		protected void onPreExecute() { // 运行在UI线程
			
		}

		protected String doInBackground(File... params) {// 在子线程中执行

			if (!MediaSCtrol.getInst().isPhotoScanState()) {
				return null;
			}

			if (isCancelled()) {
				return null;
			}
			MediaDataControl.getInst().updatePhotoData(params[0],islocal);

			Log.e("tag", "AsyncMediaTask:doInBackground" + params[0]);

			return "scanend";
		}

		protected void onPostExecute(String result) {// 运行在UI线程
			if (!MediaSCtrol.getInst().isPhotoScanState()) {
				return;
			}
			updataPhotoList();
			MediaScanner.mPhoto_Task++;
			if (MediaScanner.mPhoto_Task == MediaScanner.Photo_Num) {
				cancel(true);
				MediaSCtrol.getInst().setPhotoScanState(false);
				Log.e("tag",MediaScanner.mPhoto_Task+":"+MediaScanner.Photo_Num);
			}
			Log.e("tag", "AsyncMediaTask:onPostExecute"+MediaScanner.mPhoto_Task+":"+MediaScanner.Photo_Num);
		}

		protected void onProgressUpdate(Integer... values) {// 运行在UI线程

			Log.e("tag", "AsyncMediaTask:onProgressUpdate" + values);
		}

	}

}
