package com.dc.smedia.until; 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
public class ReadSelectedLine{  
	
//	static String CopyUsbSerial="/storage/usb_storage/copySerial/serialList.txt";
//	String BaseUsbSerial="/storage/usb_storage/copySerial/baseserial.txt";
	
    // 读取文件指定行。  
    public static String readAppointedLineNumber(File sourceFile, int lineNumber) {  
        FileReader in =null;
        LineNumberReader reader = null;
		try {
			in = new FileReader(sourceFile);
			reader = new LineNumberReader(in);  
	        String s = "";  
	        if (lineNumber <= 0 || lineNumber > getTotalLines(sourceFile)) {  
	            System.out.println("不在文件的行数范围(1至总行数)之内。");   
	            return null;
	        }  
	        int lines = 0;  
	        while (s != null) {  
	            lines++;  
	            s = reader.readLine();  
	            if((lines - lineNumber) == 0) {  
	             //通过行数查找对应文本
	             System.out.println(s);
	             reader.close();  
	             in.close();  
	             return s; 
	            }  
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(in !=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}  
       
		return null;  
    }  
    
    static FileInputStream fis ;
	static InputStreamReader isr;
	static BufferedReader br ;
	
    //通过文本查找对应行数
    public static int getCurrentLines(String current,String txtpath) throws IOException{
    	if(current==null){
    		return -1;
    	}
    	FileReader in =null;
        LineNumberReader reader = null;
		try {
			in = new FileReader(txtpath);
			reader = new LineNumberReader(in);  
	        String s = "";   
	        int lines = 0;  
	        while (s != null) {  
	            lines++;  
	            s = reader.readLine();  
	            if(s.equals(current.replaceAll("\n", ""))) {  
	             //通过行数查找对应文本
	             System.out.println(s);
	             reader.close();  
	             in.close();  
	             return lines; 
	            }  
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(in !=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}  
       
		return -1;  
    }
    
    // 文件内容的总行数。  
    public static int getTotalLines(File file) {  
        FileReader in = null;
        LineNumberReader reader = null;
		try {
			in = new FileReader(file);
			reader = new LineNumberReader(in);  
	        String s = reader.readLine();  
	        int lines = 0;  
	        while (s != null) {  
	            lines++;  
	            s = reader.readLine();  
	        }  
	        reader.close();  
	        in.close();  
	        return lines;  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(in !=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 0;  
      
        
    }  
      
    
    
    
//    /** 
//     * 读取文件指定行。 
//     */  
//    public static void main(String[] args) throws IOException {  
//        // 指定读取的行号  
//        int lineNumber = 2;  
//        // 读取文件  
//        File sourceFile = new File("D:/java/test.txt");  
//        // 读取指定的行  
//        readAppointedLineNumber(sourceFile, lineNumber);  
//        // 获取文件的内容的总行数  
//        System.out.println(getTotalLines(sourceFile));  
//    }  
}  