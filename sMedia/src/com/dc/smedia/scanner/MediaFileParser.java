package com.dc.smedia.scanner;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 判断文件类型
 * MediaScanner helper class.
 */
public class MediaFileParser {
    // comma separated list of all file extensions supported by the media scanner
    public static String sFileExtensions;

    // Audio file types
    public static final int FILE_TYPE_MP3     = 1;
    public static final int FILE_TYPE_M4A     = 2;
    public static final int FILE_TYPE_WAV     = 3;
    public static final int FILE_TYPE_AMR     = 4;
    public static final int FILE_TYPE_AWB     = 5;
    public static final int FILE_TYPE_WMA     = 6;
    public static final int FILE_TYPE_OGG     = 7;
    public static final int FILE_TYPE_AAC     = 8;
    public static final int FILE_TYPE_FLAC     = 9;
    public static final int FILE_TYPE_AC3     = 10;
    public static final int FILE_TYPE_APE     = 11;
    public static final int FILE_TYPE_RA     = 12;
    public static final int FILE_TYPE_AIFF     = 13;
    public static final int FILE_TYPE_AU     = 14;
    public static final int FILE_TYPE_MKA     = 15;
    public static final int FILE_TYPE_MMF     = 16;
    public static final int FILE_TYPE_MP2     = 17;
    
    
    
    private static final int FIRST_AUDIO_FILE_TYPE = FILE_TYPE_MP3;
    private static final int LAST_AUDIO_FILE_TYPE = FILE_TYPE_MP2;

    // MIDI file types
    public static final int FILE_TYPE_MID     = 18;
    public static final int FILE_TYPE_SMF     = 19;
    public static final int FILE_TYPE_IMY     = 20;
    private static final int FIRST_MIDI_FILE_TYPE = FILE_TYPE_MID;
    private static final int LAST_MIDI_FILE_TYPE = FILE_TYPE_IMY;
   
    // Video file types
    public static final int FILE_TYPE_MP4     = 21;
    public static final int FILE_TYPE_M4V     = 22;
    public static final int FILE_TYPE_3GPP    = 23;
    public static final int FILE_TYPE_3GPP2   = 24;
    public static final int FILE_TYPE_WMV     = 25;
    public static final int FILE_TYPE_AVI 	  = 26;
    public static final int FILE_TYPE_MKV 	  = 27;
    public static final int FILE_TYPE_MPEG    = 28;
    public static final int FILE_TYPE_F4V     = 29;
    public static final int FILE_TYPE_FLV     = 30;
    public static final int FILE_TYPE_MOV     = 31;
    public static final int FILE_TYPE_MPG     = 32;
    public static final int FILE_TYPE_RMVB    = 33;
    public static final int FILE_TYPE_TS      = 34;
    public static final int FILE_TYPE_VOB     = 35;
    
    private static final int FIRST_VIDEO_FILE_TYPE = FILE_TYPE_MP4;
    private static final int LAST_VIDEO_FILE_TYPE = FILE_TYPE_VOB;
    
    // Image file types
    public static final int FILE_TYPE_JPEG    = 36;
    public static final int FILE_TYPE_GIF     = 37;
    public static final int FILE_TYPE_PNG     = 38;
    public static final int FILE_TYPE_BMP     = 39;
    public static final int FILE_TYPE_WBMP    = 40;
    
    
    private static final int FIRST_IMAGE_FILE_TYPE = FILE_TYPE_JPEG;
    private static final int LAST_IMAGE_FILE_TYPE = FILE_TYPE_WBMP;
   
    // Playlist file types
    public static final int FILE_TYPE_M3U     = 41;
    public static final int FILE_TYPE_PLS     = 42;
    public static final int FILE_TYPE_WPL     = 43;
    private static final int FIRST_PLAYLIST_FILE_TYPE = FILE_TYPE_M3U;
    private static final int LAST_PLAYLIST_FILE_TYPE = FILE_TYPE_WPL;

	
    
    //静态内部类
    static class MediaFileType {
    
        int fileType;
        String mimeType;
        
        MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }
    
    private static HashMap<String, MediaFileType> sFileTypeMap 
            = new HashMap<String, MediaFileType>();
    private static HashMap<String, Integer> sMimeTypeMap 
            = new HashMap<String, Integer>();            
    static void addFileType(String extension, int fileType, String mimeType) {
        sFileTypeMap.put(extension, new MediaFileType(fileType, mimeType));
        sMimeTypeMap.put(mimeType, new Integer(fileType));
    }
    static {
        addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg");
        addFileType("M4A", FILE_TYPE_M4A, "audio/mp4");
        addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav");
        addFileType("AMR", FILE_TYPE_AMR, "audio/amr");
        addFileType("AWB", FILE_TYPE_AWB, "audio/amr-wb");
        addFileType("WMA", FILE_TYPE_WMA, "audio/x-ms-wma");    
        addFileType("OGG", FILE_TYPE_OGG, "application/ogg");
        addFileType("AAC", FILE_TYPE_AAC, "audio/aac");
        addFileType("FLAC", FILE_TYPE_FLAC, "audio/flac");
        addFileType("AC3", FILE_TYPE_AC3, "audio/mpeg");
        addFileType("APE", FILE_TYPE_APE, "audio/ape");
//        addFileType("RA", FILE_TYPE_RA, "audio/x-pn-realaudio");
        addFileType("AIFF", FILE_TYPE_AIFF, "audio/aiff");
        addFileType("AU", FILE_TYPE_AU, "audio/au");
        addFileType("MKA", FILE_TYPE_MKA, "audio/mka");
//        addFileType("MMF", FILE_TYPE_MMF, "audio/mmf");
        addFileType("MP2", FILE_TYPE_MP2, "audio/mp2");
        
//        addFileType("MID", FILE_TYPE_MID, "audio/midi");
//        addFileType("XMF", FILE_TYPE_MID, "audio/midi");
//        addFileType("RTTTL", FILE_TYPE_MID, "audio/midi");
//        addFileType("SMF", FILE_TYPE_SMF, "audio/sp-midi");
//        addFileType("IMY", FILE_TYPE_IMY, "audio/imelody");
        
//        addFileType("RMVB", FILE_TYPE_MP4, "video/mp4");
        
        addFileType("MP4", FILE_TYPE_MP4, "video/mp4");
        addFileType("M4V", FILE_TYPE_M4V, "video/mp4");
        addFileType("3GP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3GPP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3G2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("3GPP2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("WMV", FILE_TYPE_WMV, "video/x-ms-wmv");
        addFileType("AVI", FILE_TYPE_AVI, "video/avi");
        addFileType("MKV", FILE_TYPE_MKV, "video/x-matroska");
        addFileType("MPEG", FILE_TYPE_MPEG, "video/mpeg");
        addFileType("F4V", FILE_TYPE_F4V, "video/f4v");
        addFileType("FLV", FILE_TYPE_FLV, "video/flv");
        addFileType("MOV", FILE_TYPE_MOV, "video/mov");
        addFileType("MPG", FILE_TYPE_MPG, "video/mpeg");
        addFileType("RMVB", FILE_TYPE_RMVB, "video/rmvb");
        addFileType("TS", FILE_TYPE_TS, "video/ts");
//        addFileType("VOB", FILE_TYPE_VOB, "video/vob");
        
        
        addFileType("JPG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("JPEG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("GIF", FILE_TYPE_GIF, "image/gif");
        addFileType("PNG", FILE_TYPE_PNG, "image/png");
        addFileType("BMP", FILE_TYPE_BMP, "image/x-ms-bmp");
        addFileType("WBMP", FILE_TYPE_WBMP, "image/vnd.wap.wbmp");
 
        addFileType("M3U", FILE_TYPE_M3U, "audio/x-mpegurl");
        addFileType("PLS", FILE_TYPE_PLS, "audio/x-scpls");
        addFileType("WPL", FILE_TYPE_WPL, "application/vnd.ms-wpl");

        // compute file extensions list for native Media Scanner
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = sFileTypeMap.keySet().iterator();
        
        while (iterator.hasNext()) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(iterator.next());
        } 
        sFileExtensions = builder.toString();
    }
    
    private static final String UNKNOWN_STRING = "<unknown>";
    
    private static boolean isAudioFileType(int fileType) {
        return ((fileType >= FIRST_AUDIO_FILE_TYPE &&
                fileType <= LAST_AUDIO_FILE_TYPE) ||
                (fileType >= FIRST_MIDI_FILE_TYPE &&
                fileType <= LAST_MIDI_FILE_TYPE));
    }
    
    private static boolean isVideoFileType(int fileType) {
        return (fileType >= FIRST_VIDEO_FILE_TYPE &&
                fileType <= LAST_VIDEO_FILE_TYPE);
    }
    
    private static boolean isImageFileType(int fileType) {
        return (fileType >= FIRST_IMAGE_FILE_TYPE &&
                fileType <= LAST_IMAGE_FILE_TYPE);
    }
    
    private static boolean isPlayListFileType(int fileType) {
        return (fileType >= FIRST_PLAYLIST_FILE_TYPE &&
                fileType <= LAST_PLAYLIST_FILE_TYPE);
    }
    
    public static boolean isMedia(String path){
//    	if(path.indexOf(".ape")!=-1){
//    		Logg.e("tag","当前扫描到媒体"+path);
//    	}
    	
    	return isImageFileType(path) ||isVideoFileType(path) ||isAudioFileType(path);
    }
    
    private static MediaFileType getFileType(String path) {
//    	Logg.e("tag","扫描内容"+path);
        int lastDot = path.lastIndexOf(".");
        if (lastDot < 0)
            return null;
//        Logg.e("tag","扫描进入内容"+path);	
//        if(path.substring(lastDot + 1).toUpperCase().indexOf("APE")!=-1){
//        	Logg.e("tag",path.substring(lastDot + 1).toUpperCase());	
//        }
        
        return sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase());
    }
    //根据视频文件路径判断文件类型
    public static boolean isVideoFileType(String path) {  //自己增加
        MediaFileType type = getFileType(path);
        if(null != type) {
            return isVideoFileType(type.fileType);
        }
        return false;
    }
    //根据音频文件路径判断文件类型
    public static boolean isAudioFileType(String path) {  //自己增加
        MediaFileType type = getFileType(path);
        if(null != type) {
            return isAudioFileType(type.fileType);
        }
        return false;
    }
    //根据Image文件路径判断文件类型
    public static boolean isImageFileType(String path) {  //自己增加
        MediaFileType type = getFileType(path);
        if(null != type) {
            return isImageFileType(type.fileType);
        }
        return false;
    }
    //根据mime类型查看文件类型
    private static int getFileTypeForMimeType(String mimeType) {
        Integer value = sMimeTypeMap.get(mimeType);
        return (value == null ? 0 : value.intValue());
    }

}