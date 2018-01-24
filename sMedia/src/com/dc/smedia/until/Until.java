package com.dc.smedia.until;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;

public class Until {
	
	public static class MediaPath{
		public final  static  String musicpath="/mnt/sdcard/s201music.txt";
		public final static String videopath="/mnt/sdcard/s201video.txt";
		public final static String picpath="/mnt/sdcard/s201pic.txt";
		
		public final  static  String lmusicpath="/mnt/sdcard/s201lmusic.txt";
		public final static String lvideopath="/mnt/sdcard/s201lvideo.txt";
		public final static String lpicpath="/mnt/sdcard/s201lpic.txt";
		
		public final static String sdpath="/mnt/sdcard/";
	}
	
	// 解析视频缩略图
	public static Bitmap getVideoThumbnail(String videoPath, int width, int height) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,
				MediaStore.Images.Thumbnails.MICRO_KIND);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		
		return bitmap;

	}
	
	public static void setScroll(ListView mlist,boolean b) {
			android.widget.FrameLayout.LayoutParams parama = (android.widget.FrameLayout.LayoutParams) mlist.getLayoutParams();
			if (b) {
				parama.leftMargin = 50;
				parama.topMargin = 27;
				parama.width = 835;// 800 - r.right;
				parama.height = 493;// 480 - r.bottom;

				mlist.setEnabled(true);
				mlist.setVisibility(View.VISIBLE);
			} else {
				parama.leftMargin = 1024;
				parama.topMargin = 0;
				parama.rightMargin = 0;
				parama.bottomMargin = 0;
				mlist.setEnabled(false);
				mlist.setVisibility(View.INVISIBLE);

			}

			mlist.setLayoutParams(parama);

			/*
			 * if(b){ saaPare.setVisibility(View.VISIBLE); }else{
			 * saaPare.setVisibility(View.GONE); }
			 */
//		}
	}
	
	public static class MediaBroadcast{
		public static String ShowMax="android.action.showmax";
		public static String ShowMini="android.action.showmini";
		public static String Hide="android.action.hide";
	}
	
	
	public static String PathtoTitle(String path) {
		if(path ==null){
			return null;
		}
		return path.substring(path.lastIndexOf("/") + 1);
	}

	// drawable转bitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	//bitmap转drawable
	public static Drawable bitmap2Drawable(Bitmap bitmap) {  
	        return new BitmapDrawable(bitmap);  
    }  

	public static String changeHanzi(String url) {
		char[] tp = url.toCharArray();
		String now = "";
		for (char ch : tp) {
			if (ch >= 0x4E00 && ch <= 0x9FA5 || ch == 32 || ch == 40
					|| ch == 41 || ch == 91 || ch == 93 || ch == 123
					|| ch == 125) {
				try {
					if (ch == 32) {
						now += "%20";
					} else if (ch == 91) {
						now += "%5B";
					} else if (ch == 93) {
						now += "%5D";
					} else if (ch == 40) {
						now += "%28";
					} else if (ch == 41) {
						now += "%29";
					} else if (ch == 123) {
						now += "%7b";
					} else if (ch == 125) {
						now += "%7d";
					} else {
						now += URLEncoder.encode(ch + "", "UTF-8");
					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				now += ch;
			}

		}
		return now;
	}
	
//	public static boolean isMyServiceRunning(Class<?> serviceClass) {
//		ActivityManager manager = (ActivityManager) VLCApplication
//				.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
//		for (RunningServiceInfo service : manager
//				.getRunningServices(Integer.MAX_VALUE)) {
//			if (serviceClass.getName().equals(service.service.getClassName())) {
//				return true;
//			}
//		}
//		return false;
//	}
}
