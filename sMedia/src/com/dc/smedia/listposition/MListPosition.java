package com.dc.smedia.listposition;

import com.dc.smedia.until.Until.MediaPath;

import android.net.sip.SimpleSessionDescription.Media;

public class MListPosition {
	private static MListPosition mInst;
	public static MListPosition getInst(){
		if(mInst ==null){
			mInst =new MListPosition();
		}
		return mInst;
	}
	
	private int MPosition=0;
	private int VPosition=0;
	private int PPosition=0;
	
	private int MLPosition=0;
	private int VLPosition=0;
	private int PLPosition=0;
	
	public int getMLPosition() {
		return MLPosition;
	}
	public void setMLPosition(int mLPosition) {
		MLPosition = mLPosition;
	}
	public int getVLPosition() {
		return VLPosition;
	}
	public void setVLPosition(int vLPosition) {
		VLPosition = vLPosition;
	}
	public int getPLPosition() {
		return PLPosition;
	}
	public void setPLPosition(int pLPosition) {
		PLPosition = pLPosition;
	}

	
	
	public int getMPosition() {
		return MPosition;
	}
	public void setMPosition(int mPosition) {
		MPosition = mPosition;
	}
	public int getVPosition() {
		return VPosition;
	}
	public void setVPosition(int vPosition) {
		VPosition = vPosition;
	}
	public int getPPosition() {
		return PPosition;
	}
	public void setPPosition(int pPosition) {
		PPosition = pPosition;
	}
	public void setCPosition(int line, String txtpath) {
		if(txtpath.equals(MediaPath.lmusicpath)){
			setMLPosition(line);
		}else if(txtpath.equals(MediaPath.musicpath)){
			setMPosition(line);
		}else if(txtpath.equals(MediaPath.lvideopath)){
			setVLPosition(line);
		}else if(txtpath.equals(MediaPath.videopath)){
			setVPosition(line);
		}else if(txtpath.equals(MediaPath.lpicpath)){
			setPLPosition(line);
		}else if(txtpath.equals(MediaPath.picpath)){
			setPPosition(line);
		}
		
	}
	
	//
	
	
}
