package com.saler.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saler.async.HospitalsAsync;
import com.saler.mapper.TargetMapper;
import com.saler.pojo.Target;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.SARA_Target_Hospital__c;

import tk.mybatis.mapper.entity.Example;

@Service
public class TargetService {

	Logger logger=LoggerFactory.getLogger(getClass());
	@Autowired
	private TargetMapper tm;
	@Autowired
	private HospitalsAsync async;

	@Autowired
	InterfaceLogService interfaceLogService;
	public Map<String,Object> save(String beginTime,String endTime){
		Map<String,Object> map=new HashMap<>();
		//记录失败id
		List<String> errorIdList=new ArrayList<>();
		//失败条数统计
		int errorCount=0;
		SFCELoginService loginService=new SFCELoginService();
		//登录saleforce
		EnterpriseConnection connection=loginService.getconnection();
		Example example=new Example(Target.class);
		if(null!=beginTime&&null!=endTime) {
			example.createCriteria().andGreaterThanOrEqualTo("createon", beginTime)
			.andLessThanOrEqualTo("createon", endTime).orGreaterThanOrEqualTo("modifyon", beginTime)
			.andLessThanOrEqualTo("modifyon", endTime);
		}
		List<Target> list=tm.selectByExample(example).subList(0, 5);
		for(Target tt: list) {
			System.out.println( tt.getId()+"\t"+tt.getPeriod());
		}
		
		try {
			async.addMysqlTarget(list);
		}catch(Exception e) {
			e.printStackTrace();
		}
		logger.debug("从数据库拉去到\t"+list.size()+"\t条");
		List<SARA_Target_Hospital__c> listArray=new ArrayList<>();
		SARA_Target_Hospital__c c=null;
		Calendar calendar=null;
		for(int i=0,p=list.size();i<p;i++) {
			


			try {
				c=new SARA_Target_Hospital__c();
				c.setExternal_Key__c(list.get(i).getId().toString());
				calendar=Calendar.getInstance();
				calendar.setTime(list.get(i).getPeriod());
				calendar.add(Calendar.HOUR_OF_DAY, 8);

				c.setPeriod__c(calendar);

				c.setPharm_Code__c(list.get(i).getHospitalid());
				
				c.setProduct_Code__c(list.get(i).getProductid());
				
				c.setTarget_Unit__c(list.get(i).getTargetunit());
				if(null!=list.get(i).getModifyby()&&!list.get(i).getModifyby().equals("NULL")) {
					c.setModifyBy__c(list.get(i).getModifyby());
				}else {
					c.setModifyBy__c("");

				}
				if(null!=list.get(i).getModifyon()&&!list.get(i).getModifyon().equals("NULL")) {
					calendar.setTime(list.get(i).getModifyon());
					c.setModifyOn__c(calendar);
				}else {
					c.setModifyOn__c(null);
				}
				c.setCreateBy__c(list.get(i).getCreateby());
				calendar=Calendar.getInstance();
				calendar.setTime(list.get(i).getCreateon());
				c.setCreateOn__c(calendar);
				if(list.get(i).getDeleted()==0) {
					c.setDeleted__c(true);
				}else {
					c.setDeleted__c(false);
				}
				listArray.add(c);
			}catch(Exception e) {
				errorIdList.add(list.get(i).getId().toString());
				errorCount++;
				continue;
			}
		}
		logger.debug("遍历到SARA_Target_Hospital__c集合\t"+listArray.size()+"\t条");

		//统计每次198插入多少次
		int count=listArray.size()/198;
		//统计每次198插入过后，余数
		int remainder=listArray.size()%198;
		//统计
		int statistics=0;

		//成功条数
		int successCount=0;
		//用于记录数据
		List<SARA_Target_Hospital__c> recordList=new ArrayList<>();
		//-----------------------------
		List<String> errorPharm_cMessage=new ArrayList<>();//错误数据内容
		try {
			for(SARA_Target_Hospital__c sth :listArray) {
				recordList.add(sth);
				if(recordList.size()>=198) {
					SARA_Target_Hospital__c [] sthArray=recordList.toArray(new SARA_Target_Hospital__c[recordList.size()]);
					UpsertResult[] results=connection.upsert("External_Key__c", sthArray);
					for (int v=0; v< results.length; v++) {
						if (results[v].isSuccess()) {
							logger.debug(v+". Successfully created record - Id: " + results[v].getId());
							successCount++;					
						} else {
							com.sforce.soap.enterprise.Error[] errors = results[v].getErrors();
							for (int j=0; j< errors.length; j++) {
								logger.warn("第"+v+"条"+"ERROR creating record: " + errors[j].getMessage());
								errorIdList.add(sthArray[v].getId());
								errorPharm_cMessage.add(errors[j].getMessage());
								errorCount++;
							}
						}  
					}
					recordList.clear();
					statistics++;
				}else if(recordList.size()==remainder&&count==statistics) {
					SARA_Target_Hospital__c [] sthArray=recordList.toArray(new SARA_Target_Hospital__c[recordList.size()]);
					UpsertResult[] results=connection.upsert("External_Key__c", sthArray);
					for (int v=0; v< results.length; v++) {
						if (results[v].isSuccess()) {
							logger.debug(v+". Successfully created record - Id: " + results[v].getId());
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							System.out.println(sdf.format(sthArray[v].getPeriod__c().getTime()));
							successCount++;					
						} else {
							com.sforce.soap.enterprise.Error[] errors = results[v].getErrors();
							for (int j=0; j< errors.length; j++) {
								logger.warn("第"+v+"条"+"ERROR creating record: " + errors[j].getMessage());
								errorIdList.add(sthArray[v].getId());
								errorPharm_cMessage.add(errors[j].getMessage());
								errorCount++;
							}
						}  
					}
					recordList.clear();
				}

			}
		}/*catch(ConnectionException e) {
			e.printStackTrace();
		}*/catch(Exception ee){
			ee.printStackTrace();
		}finally {
			loginService.closeSFCE(connection);
		}

		if(errorCount>0) {
			interfaceLogService.addInterfaceLog(errorIdList, errorPharm_cMessage, "Target");
		}

		map.put("Object","SARA_Target_Hospital__c");
		map.put("total", listArray.size());
		map.put("success", successCount);
		map.put("error", errorCount);
		return map;
	}
}
