package com.cpu.entity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.util.Log;

/**
 * �ļ���ʵ����
 * @author ZJC
 *
 */
public class ImageFolder {

	/**
	 * ͼƬ���ļ���·��
	 */
	private String dir;
	
	/**
	 * �ļ����е�һ��ͼƬ��·��
	 */
	private String firstImagePath;
	
	/**
	 * �ļ��е�����
	 */
	private String name;
	
	/**
	 * ͼƬ����
	 */
	private int count;
	
	/**
	 * ���µ�һ��ͼƬ
	 */
	private String latestImagePath;

	/**
	 * �ļ���·��
	 * @return
	 */
	public String getDir(){
		return dir;
	}
	
	/**
	 * �ļ��о���·��
	 */
	private String abSolutePath;
	


	/**
	 * �����ļ���·��ʱ�Զ���ȡ�ļ������ơ�����1��ͼƬ
	 * @param dir
	 */
	public void setDir(String dir){
		this.dir = dir;
		int lastIndexOf = this.dir.lastIndexOf("/");
		this.name = this.dir.substring(lastIndexOf);
		
		//�ļ��о���·��
		setAbSolutePath(findAbsolutePath());
		//����·�����Զ���ȡ����1��ͼƬ
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
	 * �����ļ���·������������1��ͼƬ
	 * @return
	 */
	public String findLatestImagePath(){
		List<String> curImgs;//��ǰ�ļ��������е�ͼƬ·��
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
		//��ʱ����������
		Collections.reverse(curImgs);
		//���ؾ���·��
		return file.getAbsolutePath() + "/" +curImgs.get(0);
	}
	
	public String findAbsolutePath(){
		File file = new File(getDir());
		return file.getAbsolutePath();
	}
}
