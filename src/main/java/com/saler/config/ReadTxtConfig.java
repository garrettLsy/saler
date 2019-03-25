package com.saler.config;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;  

public class ReadTxtConfig { 

	/**
	 *  读入TXT文件 
	 */
	public static List<String> outTxt() {  
		BufferedReader br=null;
		List<String> list=new ArrayList<>();
		try { 
			String pathname = "E:\\input.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
			File filename = new File(pathname); // 要读取以上路径的input。txt文件  
			InputStreamReader reader = new InputStreamReader( new FileInputStream(filename),"UTF-8"); // 建立一个输入流对象reader  
			br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
			String line = ""; 
			
			while (line != null) {  
				line = br.readLine(); // 一次读入一行数据 
				list.add(line);
			}   

		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		return list;
	} 

	/**
	 * 写入Txt文件
	 * @param log
	 */
	public static void inputTxt(String log) {

		File writename = new File("E:\\input.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件 
		BufferedWriter out =null;
		try {
			if (!writename.exists()) {
				writename.createNewFile();
			}
			out = new BufferedWriter(new FileWriter(writename,true));  
			
			out.write(log+"\n"); // \r\n即为换行  
			out.flush(); // 把缓存区内容压入文件  

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();//关闭文件
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} 

	}
}  