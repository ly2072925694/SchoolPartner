package com.cpu.entity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.util.Log;

/**
 * 文件夹实体类
 * @author ZJC
 *
 */
public class ImageFolder {

	/**
	 * 图片的文件夹路径
	 */
	private String dir;
	
	/**
	 * 文件夹中第一张图片的路劲
	 */
	private String firstImagePath;
	
	/**
	 * 文件夹的名称
	 */
	private String name;
	
	/**
	 * 图片数量
	 */
	private int count;
	
	/**
	 * 最新的一张图片
	 */
	private String latestImagePath;

	/**
	 * 文件夹路劲
	 * @return
	 */
	public String getDir(){
		return dir;
	}
	
	/**
	 * 文件夹绝对路劲
	 */
	private String abSolutePath;
	


	/**
	 * 设置文件夹路劲时自动获取文件夹名称、最新1张图片
	 * @param dir
	 */
	public void setDir(String dir){
		this.dir = dir;
		int lastIndexOf = this.dir.lastIndexOf("/");
		this.name = this.dir.substring(lastIndexOf);
		
		//文件夹绝对路劲
		setAbSolutePath(findAbsolutePath());
		//设置路径是自动获取最新1张图片
		setLatestImagePath(findLatestImagePath());
	}
	
	public String getAbSolutePath() {
		return abSolutePath;
	}

	public void setAbSolutePath(String abSolutePath) {
		this.abSolutePath = abSolutePath;
	}
	
	public String getFirstImagePath(){
		return firstImagePath;
	}
	
	public void setFirstImagePath(String firstImagePath){
		this.firstImagePath = firstImagePath;
	}
	
	public String getName(){
		return name;
	}
	
	public int getCount(){
		return count;
	}
	
	public void setCount(int count){
		this.count = count;
	}
	
	public String getLatestImagePath() {
		return latestImagePath;
	}

	public void setLatestImagePath(String latestImagePath) {
		this.latestImagePath = latestImagePath;
	}
	
	/**
	 * 根据文件夹路径，返回最新1张图片
	 * @return
	 */
	public String findLatestImagePath(){
		List<String> curImgs;//当前文件加下所有的图片路径
		File file = new File(getDir());
		curImgs = Arrays.asList(file.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {

				if (filename.endsWith(".jpg") || filename.endsWith(".png")  
	                    || filename.endsWith(".jpeg")){  
					Log.i("File", filename);
	                return true;  
				}
				return false;
			}
		}));
		//按时间最新排序
		Collections.reverse(curImgs);
		//返回绝对路径
		return file.getAbsolutePath() + "/" +curImgs.get(0);
	}
	
	public String findAbsolutePath(){
		File file = new File(getDir());
		return file.getAbsolutePath();
	}
}
