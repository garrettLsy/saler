package com.saler.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.saler.async.HospitalsAsync;
import com.saler.async.SaleforceAddAsync;
import com.saler.mapper.DirectionMapper;
import com.saler.pojo.Direction;
import com.saler.util.LoggerUtil;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.AMAP_Data__c;

import tk.mybatis.mapper.entity.Example;

@Service
public class DirectionService extends LoggerUtil{
	@Autowired
	private DirectionMapper dm;
	@Autowired
	InterfaceLogService interfaceLogService;
	@Autowired
	private HospitalsAsync async;
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SaleforceAddAsync addAsync;

	public Map<String,Object> add(String beginTime,String endTime) {
		Map<String,Object> map=new HashMap<>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		//以当前时间   获取版本号
		String  timeVersion=formatter.format(new Date());
		
		SFCELoginService loginService=new SFCELoginService();
		//链接salesforce
		EnterpriseConnection connection=loginService.getconnection();
		
		//查询本地数据库
		Example example=new Example(Direction.class);
		if(null!=beginTime&&null!=endTime) {
			example.createCriteria().andGreaterThanOrEqualTo("salesDate",beginTime).andLessThanOrEqualTo("salesDate",endTime);
		}
		List<Direction> list=dm.selectByExample(example);
		
		/*try {
			async.addMysqlDirction(restTemplate, list, timeVersion);
		} catch (Exception e) {
			System.out.println("木有灵魂的代码");	
			e.printStackTrace();
		}*/
		map.put("flag", 0);
		map.put("errorMsg","");
		addAsync.addDirection(list, interfaceLogService, timeVersion);
		return map;
	}

}
