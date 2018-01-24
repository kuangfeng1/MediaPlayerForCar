package com.dc.smedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ToPlayData implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4707415674067235135L;
	// final static String oldFile = "OLD:";
	// final static String oldLoc = "LOC:";
	// final static String OldDirFLAG = "DIR:";
	// final static String OldUriFLAG = "Uri:";
	// final static String oldFlvd_beUseFile = "flvd_beUseFile:";

	

	public String artist;
	public String album;

	// public Uri uri = null;
	public long id;
	public File file = null;
	public String path=null;
	public int nPosition = 0;
	public long nDuration=0;
	public File dirPath = null;
	public boolean flvd_beUseFile;

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o == null)
			return false;
		if (o.getClass() != ToPlayData.class)
			return false;
		ToPlayData tpd = (ToPlayData) o;
		return file.equals(tpd.file);
	}

}
