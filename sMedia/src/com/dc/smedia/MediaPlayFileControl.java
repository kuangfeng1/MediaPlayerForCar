package com.dc.smedia;

import java.io.File;
import java.io.IOException;

import com.dc.smedia.listposition.MListPosition;
import com.dc.smedia.until.Logg;
import com.dc.smedia.until.ReadSelectedLine;

public class MediaPlayFileControl {
	private static MediaPlayFileControl mInst;
//	public final static String musicpath = "/sdcard/dcdata/s201.txt";
//	public final static String videopath = "/sdcard/dcdata/s201video.txt";
	private boolean isLoop;

	
//	private MediaCPosition mMCPosition;
//	public interface MediaCPosition{
//		void setCPosition(int position,String txtpath);
//	}
//	public void setmMCPosition(MediaCPosition mMCPosition) {
//		this.mMCPosition = mMCPosition;
//	}
	
	
	public static MediaPlayFileControl getInst() {
		if (mInst == null) {
			mInst = new MediaPlayFileControl();
		}
		return mInst;
	}

	

	public boolean isLoop() {
		return isLoop;
	}

	public void setLoop(boolean isLoop) {
		this.isLoop = isLoop;
	}

	public String getNextPlayData(String ftoplay,String txtpath) {
		// ͨ����ǰ�����ļ�������һ���ļ�
		try {
			int line = ReadSelectedLine.getCurrentLines(ftoplay, txtpath);
			if (line == -1) {
				return null;
			}
			if (line < ReadSelectedLine.getTotalLines(new File(txtpath))) {
				line++;
			} else {
				if (isLoop()) {
					line = 1;
				} else {
					return null;
				}

			}
			MListPosition.getInst().setCPosition(line,txtpath);
//			if(mMCPosition!=null)
//			mMCPosition.setCPosition(line,txtpath);
			
			
			// ��ȡ��ǰ����ģʽ
			String t = ReadSelectedLine.readAppointedLineNumber(new File(
					txtpath), line);
			return t;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getRandomPlayData(String ftoplay,String txtpath) {
		// ͨ����ǰ�����ļ�������һ���ļ�
		try {
			int line = ReadSelectedLine.getCurrentLines(ftoplay, txtpath);
			int totalline=(ReadSelectedLine.getTotalLines(new File(txtpath)));
			int randomline=(int) (Math.random() *totalline+1);
			
			MListPosition.getInst().setCPosition(randomline,txtpath);
//			if(mMCPosition!=null)
//			mMCPosition.setCPosition(randomline,txtpath);
			// ��ȡ��ǰ����ģʽ
			String t = ReadSelectedLine.readAppointedLineNumber(new File(
					txtpath), randomline);
//			Logg.e("tag","��ȡ��ǰ���Ÿ���"+t+"������"+totalline+"��ǰ�������"+randomline);
			return t;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getPrevPlayData(String ftoplay,String txtpath) {
		// ͨ����ǰ�����ļ�������һ���ļ�
		try {
			int line = ReadSelectedLine.getCurrentLines(ftoplay, txtpath);
			if (line == -1) {
				return null;
			}
			if (line > 1) {
				line--;
			} else {
				if (isLoop()) {
					line = ReadSelectedLine.getTotalLines(new File(txtpath));
				} else {
					return null;
				}
			}
			MListPosition.getInst().setCPosition(line,txtpath);
//			if(mMCPosition!=null)
//			mMCPosition.setCPosition(line,txtpath);
			// ��ȡ��ǰ����ģʽ
			String t = ReadSelectedLine.readAppointedLineNumber(new File(
					txtpath), line);
			return t;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void restoreFile(){
		
	}
	

}
