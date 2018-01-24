package com.dc.smedia.photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.dc.smedia.MediaPlayFileControl;
import com.dc.smedia.MediaSelectControl;
import com.dc.smedia.ToPlayData;
import com.dc.smedia.music.PlayAudioFileTool.MusicCallBack;
import com.dc.smedia.until.Logg;
import com.dc.smedia.until.Until;
import com.dc.smedia.until.Until.MediaPath;


public class PlayPicTool {
	private static PlayPicTool mplaytool;
	private MediaPlayFileControl mc=MediaPlayFileControl.getInst();
	public static PlayPicTool getPift(){
		if(mplaytool ==null){
			mplaytool =new PlayPicTool();
		}
		return mplaytool;
	}
	Bitmap toDraw;
	private Bitmap image = null;
	public int level = 0;//
	int xOffSet = 0;//
	int yOffSet = 0;//
	int degrees = 0;
	private Bitmap resizedBitmap;
	private static String ftoPlay;
//	private RECT surfaceRect = new RECT();
//	
//	
//	public void setSurfaceLocation() {
//		// TODO Auto-generated method stub
////		this.surfaceRect = r;
//		surfaceRect.left = 469;
//		surfaceRect.top = 144;
//		surfaceRect.bottom = 326;
//		surfaceRect.right = 665;
//
//	}
	public void StartPlay(ToPlayData toplay) {
		// TODO Auto-generated method stub
		if (image != null) {
			image.recycle();
			image = null;
		}
		if (resizedBitmap != null) {
			resizedBitmap.recycle();
			resizedBitmap = null;
		}
		clearRollScal();

		FileInputStream fis;
		try {
			if (toplay == null) {
				return;
			}
			ftoPlay=toplay.path;
			fis = new FileInputStream(ftoPlay);
			image = BitmapFactory.decodeStream(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void clearRollScal() {
		degrees = 0;
		level = 0;
		xOffSet = yOffSet = 0;
		if (resizedBitmap != null) {
			// resizedBitmap.recycle();
			resizedBitmap = null;
		}
	}

	// 0 org size
	// 1 2 bei
	// 2 4 倍
	// -1 1/2
	// -2 1/4

	int getNewValue(int w) {
		switch (level) {
		case -2:
			return w / 4;
		case -1:
			return w / 2;
		case 0:
			return w;
		case 1:
			return 2 * w;
		case 2:
			return 4 * w;
		}
		return w;
	}
	
//	@SuppressLint("NewApi")
//	public void drawPic(Canvas c) {
//		// TODO Auto-generated method stub
//		if (image == null) {
//			return;
//		}
//		if (!isShowFull()) {
//			setSurfaceLocation();
//			Rect rtarget = new Rect();
//			rtarget.left = surfaceRect.left - 10;
//			rtarget.top = surfaceRect.top;
//			rtarget.right = surfaceRect.right - 20;
//			rtarget.bottom = surfaceRect.bottom;
//			Rect src = new Rect();
//			src.left = src.top = 0;
//			src.right = image.getWidth();
//			src.bottom = image.getHeight();
//			Paint p = new Paint();
//			c.drawBitmap(image, src, rtarget, p);
//		} else {
//			// 全屏幕;按照原始大小现实
//			// 要求如果超过800*480，那么就显示成800*480的，如果小于那么就原始大小实现
//			try {
//				int w = 0, h = 0;
//				toDraw = this.image;
////				toDraw = this.image.copy(Bitmap.Config.ARGB_8888, true);
////				toDraw = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(),
////						null, true);
//				// 判断图片是否可变
//				if (resizedBitmap != null) {
//					// 手动调节大小时不调整固定大小图片
//					toDraw = resizedBitmap;
//					w = getNewValue(toDraw.getWidth());
//					h = getNewValue(toDraw.getHeight());
//				} else if (toDraw.isMutable()) {
//					w = getNewValue(toDraw.getWidth());
//					h = getNewValue(toDraw.getHeight());
//					if (w > 800) {
//						w = 800;
//						toDraw.setWidth(800);
//					}
//					if (h > 480) {
//						h = 480;
//						toDraw.setHeight(480);
//					}
//				} else {
////					w = getNewValue(toDraw.getWidth());
////					h = getNewValue(toDraw.getHeight());
//					
////					return ;
//				}
//				Rect rtarget = new Rect();
//				rtarget.left = surfaceRect.left ;
//				rtarget.top = surfaceRect.top;
//				rtarget.right = surfaceRect.right ;
//				rtarget.bottom = surfaceRect.bottom;
//				Rect src = new Rect();
//				src.left = src.top = 0;
//				src.right = toDraw.getWidth();
//				src.bottom = toDraw.getHeight();
//				Paint p = new Paint();
//				c.drawBitmap(toDraw, src, rtarget, p);
//				Logg.e("tag","toDrawBitmapWidth"+toDraw.getWidth()+"toDrawBitmapHeight"+toDraw.getHeight());
////				Rect rtarget = new Rect();
////				rtarget.left = (800 - w) / 2;
////				rtarget.top = (480 - h) / 2;
////				rtarget.right = rtarget.left + w;
////				rtarget.bottom = rtarget.top + h;
////				rtarget.left += xOffSet;
////				rtarget.right += xOffSet;
////				rtarget.top += yOffSet;
////				rtarget.bottom += yOffSet;
////				Rect src = new Rect();
////				src.left = src.top = 0;
////				src.right = toDraw.getWidth();
////				src.bottom = toDraw.getHeight();
////				Paint p = new Paint();
////				c.drawBitmap(toDraw, src, rtarget, p);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}
//	}

	private boolean isShowFull() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getCurrentPath(){
		return ftoPlay;
	}
	
	public PhotoCallBack cb;
	public interface PhotoCallBack {
		public void newPhotoPlayed(String path);
	};
	
	public void setCallBack(PhotoCallBack mcb){
		cb =mcb;
	}
	
	public Bitmap StartPlay(String path) {
		// TODO Auto-generated method stub
		if(path ==null)
			return null;
		
		FileInputStream fis;
		if (!new File(path).exists()) {
			return null;
		}
		if(cb!=null){
			cb.newPhotoPlayed(Until.PathtoTitle(path));
		}
		
		ftoPlay =path;
		try {
			fis = new FileInputStream(path);
			Bitmap photo_image = BitmapFactory.decodeStream(fis);
			fis.close();
			return photo_image;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	public String getPrevPath(String path) {
		// TODO Auto-generated method stub
		if(path ==null){
			return null;
		}
		mc.setLoop(true);
		String mPath;
		if(MediaSelectControl.getInst().isLocal()){
			mPath=mc.getPrevPlayData(path,MediaPath.lpicpath);
		}else{
			mPath=mc.getPrevPlayData(path,MediaPath.picpath);
		}
		return mPath;
	}

	public String getNextPath(String path) {
		if(path ==null){
			return null;
		}
		mc.setLoop(true);
		String mPath;
		if(MediaSelectControl.getInst().isLocal()){
			mPath=mc.getPrevPlayData(path,MediaPath.lpicpath);
		}else{
			mPath=mc.getPrevPlayData(path,MediaPath.picpath);
		}
		return mPath;
	}

}
