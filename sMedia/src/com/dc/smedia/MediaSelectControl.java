package com.dc.smedia;

public class MediaSelectControl {
	private static MediaSelectControl mInst;
	public static MediaSelectControl getInst(){
		if(mInst ==null){
			mInst =new MediaSelectControl();
		}
		return mInst;
	}
	private boolean isLocal;
	private boolean isUsb;
	
	private int select;
	
	public int getSelect() {
		return select;
	}

	public void setSelect(int select) {
		
		this.select = select;
	}
	public final static int local=0;
	public final static int usb=1;
	public final static int aux=2;
	public final static int btmusic=3;
	

	
	
	
	public boolean isLocal() {
		return select==local?true:false;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	public boolean isUsb() {
		return select==usb?true:false;
	}
	
	public boolean isAux() {
		return select==aux?true:false;
	}
	
	public boolean isBtmusic() {
		return select==btmusic?true:false;
	}
	

	public void setUsb(boolean isUsb) {
		this.isUsb = isUsb;
	}


	
}
