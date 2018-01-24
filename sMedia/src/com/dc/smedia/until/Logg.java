package com.dc.smedia.until;

import android.util.Log;

/**
 * 自定义的log管理器
 * 
 * @author huangpeng
 * 
 */
public class Logg {

	public static boolean debug=true;
	public static void v(String tag, String msg) {
		if (debug) {
			Log.v(tag, msg); 
		}
	}

	public static void d(String tag, String msg) {
		if (debug) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (debug) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (debug) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (debug) {
			Log.e(tag, msg);
		}
	}

}