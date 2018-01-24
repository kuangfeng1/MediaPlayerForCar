package com.dc.smedia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends Activity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		setAsMaxWindow();
		super.onCreate(arg0);
	}
	@SuppressLint("NewApi")
	public void setAsMaxWindow() {
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}
}
