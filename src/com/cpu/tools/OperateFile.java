package com.cpu.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.cpu.constant.GetUserInformation;

public class OperateFile {
	    public static BufferedReader bufread;

        private static ArrayList<String> data = new ArrayList<String>();
	    //ָ���ļ�·��������

	    /**
	     * �����ı��ļ�.
	     * @throws IOException 
	     * 
	     */
	    public static File creatTxtFile(String str) throws IOException{
	    	File filename = new File(GetUserInformation.path+str);
	        if (!filename.exists()) {
	            filename.createNewFile();
	            return filename;
	        }
	       return filename;
	    }
	    
	    /**
	     * ��ȡ�ı��ļ�.
	     * 
	     */
	    public static ArrayList<String> readTxtFile(String str){
	        String read;
	    	File filename = new File(GetUserInformation.path+str);
	        int i = 0;
	        FileReader fileread;
	        try {
	            fileread = new FileReader(filename);
	            bufread = new BufferedReader(fileread);
	            try {
	                while ((read = bufread.readLine()) != null) {
	                    data.add(read);
	                    i++;
	                }
	                fileread.close();
	                bufread.close();
	                if(i == 5)
	                	return data;
	                return null;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    
	    /**
	     * д�ļ�.
	     * 
	     */
	    public static boolean writeTxtFile(ArrayList<String> data
	    		,String filename) throws IOException{
	    	String filein = "";
	    	String pathFile = GetUserInformation.path + filename;
	    	FileWriter resultFile = new FileWriter(pathFile);
	    	 for(int i = 0;i<data.size();i++){
		    		filein = filein + data.get(i) + "\r\n";
		    	}
	    	 resultFile.write(filein);
	    	resultFile.close();   //�ر��ļ�д����
	    	
	    	
	        return false;
	    }
	    public static boolean deleteFile(String str) {
	    	File filename = new File(GetUserInformation.path+str);
	        if (!filename.exists()) {
	            return true;
	        }else{
	        	filename.delete();
	        	return true;
	        }
		}
}

