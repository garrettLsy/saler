package com.saler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.saler.config.ReadTxtConfig;

@Service
public class TXTInputService {
	
	public List<String> logTXTList(){
		List<String> list=new ArrayList<>();
		list=ReadTxtConfig.outTxt();
		List<String> logList=new ArrayList<>();
		if(list.size()>200) {
			logList=list.subList(list.size()-200,list.size());
		}else {
			logList=list;
		}
		Collections.reverse(logList);
		return logList;
	}
}
