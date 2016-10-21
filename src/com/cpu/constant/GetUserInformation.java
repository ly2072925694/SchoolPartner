package com.cpu.constant;

import java.io.IOException;

import com.cpu.tools.OperateFile;

import android.os.Environment;


public class GetUserInformation {
	public static String path = Environment
			.getExternalStorageDirectory().getAbsolutePath()+"/" ;
	
	public static String ReadId(String filename) throws IOException {
    	if(OperateFile.readTxtFile(filename)!=null)
		return OperateFile.readTxtFile(filename).get(1);
		return null;
	}
	public static String ReadTele(String filename) throws IOException {
    	if(OperateFile.readTxtFile(filename)!=null)
		return OperateFile.readTxtFile(filename).get(3);
		return null;
	}
	
	public static String ReadToken(String filename) throws IOException {
    	if(OperateFile.readTxtFile(filename)!=null)
		return OperateFile.readTxtFile(filename).get(0);
		return null;
	}
	
	public static final String[] m = {"全部","按时间排序","按热度排序","仅看女生","仅看男生"};
	public static final String[] n = {"申请加入","等待审核"};
	public static final String[] type = {"study","travel","exercise","food","other","movie"};
}
