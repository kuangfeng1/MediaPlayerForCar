package com.dc.smedia.usblistener;

import java.io.File;

import android.os.StatFs;
import android.os.SystemProperties;

public class UsbAccess {
	// public final static String dir = "/storage/usb_storage1";
	public static String dir = null;

	private final static String MP3_DEBUG_DIR = "/storage/emulated/0/dcmp3";

	public static String getDir() {
		if(dir ==null){
			dir=SystemProperties.get("persist.sys.usb_storage", "/mnt/udisk1");
		}
		return dir;
	}

	public static String getScanDir() {
		File mfile = new File(MP3_DEBUG_DIR);
		if (mfile.exists()) {
			return MP3_DEBUG_DIR;
		}
		if (isUsbExist()) {
			File m1 = new File(getDir());
			if (m1.exists()) {
				return getDir();
			}
			return null;
		}
		return null;
	}

	public static boolean isExistUsb() {
		return getScanDir() != null;
	}

	public static boolean isUsbExist() {
		if (getUSBStorage() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static long getUSBStorage() {
		// 这样写同样适合于SD卡挂载。
		try {
			File path = new File(getDir());
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			long availableBlocks = stat.getAvailableBlocks();
			// String usedSize = Formatter.formatFileSize(context,
			// (totalBlocks-availableBlocks) * blockSize);
			// String availableSize = Formatter.formatFileSize(context,
			// availableBlocks * blockSize);
			return totalBlocks - availableBlocks;// 空间:已使用/可用的
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
