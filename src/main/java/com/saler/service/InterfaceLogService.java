package com.saler.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.saler.mapper.BlocMapper;
import com.saler.mapper.DirectionMapper;
import com.saler.mapper.DistributorsMapper;
import com.saler.mapper.HospitalsMapper;
import com.saler.mapper.TargetMapper;
import com.saler.pojo.Bloc;
import com.saler.pojo.Direction;
import com.saler.pojo.Distributors;
import com.saler.pojo.Hospitals;
import com.saler.pojo.Target;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.sobject.Interface_Log__c;
import com.sforce.ws.ConnectionException;

import tk.mybatis.mapper.entity.Example;
@Service
public class InterfaceLogService {
	@Autowired
	private HospitalsMapper hospitalsService;
	@Autowired
	private DirectionMapper  directionService;
	@Autowired
	private DistributorsMapper  distributorsService;
	@Autowired
	private BlocMapper  blocService;
	@Autowired
	private TargetMapper targetMapper;
	public void addInterfaceLog(List<String> idList,List<?> errorMsgList,String objectName) {
		SFCELoginService login=new SFCELoginService();
		EnterpriseConnection connection=login.getconnection();

		Interface_Log__c [] cArray=new Interface_Log__c[1];
		Interface_Log__c c=new Interface_Log__c();
		c.setExternal_Key__c(JSON.toJSONString(idList));
		c.setError_Message__c(JSON.toJSONString(errorMsgList));
		c.setObject_Name__c(objectName);
		if(objectName.equals("Pharm")) {
			List<String> record=new ArrayList<>();
			List<Hospitals> countList=new ArrayList<>();
			if(idList.size()>2000) {
				for(int i=0;i<idList.size();i++) {
					record.add(idList.get(i));
					if(record.size()>=1000) {
						Example example=new Example(Hospitals.class);
						example.createCriteria().andIn("hospitalid", record);
						List<Hospitals> list=hospitalsService.selectByExample(example);
						countList.addAll(list);
						record.clear();
					}
				}
				Example example=new Example(Hospitals.class);
				example.createCriteria().andIn("hospitalid", record);
				List<Hospitals> list=hospitalsService.selectByExample(example);
				countList.addAll(list);
				c.setRecordDetail__c(JSON.toJSONString(list));
			}else {
				Example example=new Example(Hospitals.class);
				example.createCriteria().andIn("hospitalid", idList);
				List<Hospitals> list=hospitalsService.selectByExample(example);
				c.setRecordDetail__c(JSON.toJSONString(list));
			}
		}
		else if(objectName.equals("Distributor")){
			Example example=new Example(Distributors.class);
			example.createCriteria().andIn("distributorid", idList);
			List<Distributors> list=distributorsService.selectByExample(example);
			c.setRecordDetail__c(JSON.toJSONString(list));
		}else if(objectName.equals("SalesData")) {
			Example example=new Example(Direction.class);
			example.createCriteria().andIn("amapId", idList);
			List<Direction> list=directionService.selectByExample(example);
			c.setRecordDetail__c(JSON.toJSONString(list));

		}else if(objectName.equals("Group")) {
			Example example=new Example(Bloc.class);
			example.createCriteria().andIn("id", idList);
			List<Bloc> list=blocService.selectByExample(example);
			c.setRecordDetail__c(JSON.toJSONString(list));
		}else if(objectName.equals("Target")){
			Example example=new Example(Target.class);
			example.createCriteria().andIn("id", idList);
			List<Target> list=targetMapper.selectByExample(example);
			c.setRecordDetail__c(JSON.toJSONString(list));
		}
		cArray[0]=c;
		try {
			connection.create(cArray);
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			login.closeSFCE(connection);
		}

	}
}
