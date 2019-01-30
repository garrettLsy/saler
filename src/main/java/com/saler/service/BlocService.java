package com.saler.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.saler.async.HospitalsAsync;
import com.saler.mapper.BlocMapper;
import com.saler.pojo.Bloc;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.AMAP_Data__c;

import tk.mybatis.mapper.entity.Example;


@Service
public class BlocService {

	@Autowired
	private BlocMapper bm;
	@Autowired
	InterfaceLogService interfaceLogService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private HospitalsAsync async;
	Logger logger=LoggerFactory.getLogger(getClass());


	public Map<String,Object> add(String beginTime,String endTime) {
		Map<String,Object> map=new HashMap<>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		//以当前时间   获取版本号
		//		String  timeVersion=formatter.format(new Date());
		SFCELoginService loginService=new SFCELoginService();
		//链接salesforce

		EnterpriseConnection connection=loginService.getconnection();
		//查询本地数据库
		Example example=new Example(Bloc.class);
		if(null!=beginTime&&null!=endTime) {
			example.createCriteria().andGreaterThanOrEqualTo("modifyon",beginTime)
			.andLessThanOrEqualTo("modifyon",endTime).orGreaterThanOrEqualTo("createon", beginTime)
			.andLessThanOrEqualTo("createon", endTime);
		}
		List<Bloc> list=bm.selectByExample(example);
		/*try {
			async.addMysqlBloc( list);
		} catch (Exception e) {
			System.out.println("木有灵魂的代码");	
			e.printStackTrace();
		}*/
		List<AMAP_Data__c> clist=new ArrayList<>();
		AMAP_Data__c c=null;
		Calendar calendar=null;
		
		for(int i=0;i<list.size();i++) {
			c=new AMAP_Data__c();
			c.setPrimaryKey4Group__c(list.get(i).getId().toString());
			//c.setPrimaryKey__c(list.get(i).getId());
			if(null !=list.get(i).getPeriod()) {
				calendar=Calendar.getInstance();
				calendar.setTime(list.get(i).getPeriod());
				c.setPeriod__c(calendar);
			}else {
				c.setPeriod__c(null);
			}
			c.setPharm_Code__c(list.get(i).getHospitalId());
			if(null!=list.get(i).getSegmentation()&&!list.get(i).getSegmentation().equals("NULL")) {
				c.setSegmentation__c(list.get(i).getSegmentation());
			}else {
				c.setSegmentation__c("");

			}
			if(null!=list.get(i).getChain()&&!list.get(i).getChain().equals("NULL")) {
				c.setGroup_Name__c(list.get(i).getChain());
			}else {
				c.setGroup_Name__c("");
			}
			if(null!=list.get(i).getChainEn()&&!list.get(i).getChainEn().equals("NULL")) {
				c.setGroup_Name_EN__c(list.get(i).getChainEn());
			}else {
				c.setGroup_Name_EN__c("");
			}
			if(null!=list.get(i).getModifyon()) {
				calendar=Calendar.getInstance();
				calendar.setTime(list.get(i).getModifyon());
				c.setModifyOn__c(calendar);
			}else {
				c.setModifyOn__c(null);

			}
			if(null!=list.get(i).getModifyby()&&!list.get(i).getModifyby().equals("NULL")){
				c.setModifyBy__c(list.get(i).getModifyby());
			}else {
				c.setModifyBy__c("");
			}
			if(null!=list.get(i).getCreateby()&&!list.get(i).getCreateby().equals("NULL")) {
				c.setCreateBy__c(list.get(i).getCreateby());

			}else {
				c.setCreateBy__c("");
			}
			if(null!=list.get(i).getModifyon()) {
				calendar=Calendar.getInstance();
				calendar.setTime(list.get(i).getModifyon());
				c.setCreateOn__c(calendar);
			}else {
				c.setCreateOn__c(null);
			}
			if(null!=list.get(i).getDeleted()&&list.get(i).getDeleted().equals("1")) {
				c.setIsDeleted__c(true);
			}else {
				c.setIsDeleted__c(false);
			}
			c.setProvinceName__c(list.get(i).getTaid().trim());
			c.setType__c("Group Info");

			clist.add(c);
		}
		logger.debug("流向数据表--------------->\t从数据库中取到\t"+list.size()+"\t条");
		AMAP_Data__c [] adcArray=null;
		List<AMAP_Data__c> adclist=new ArrayList<>();
		//统计每次198插入多少次
		int count=clist.size()/198;
		//统计每次198插入过后，余数
		int remainder=clist.size()%198;
		//统计
		int statistics=0;
		//失败条数统计
		int errorCount=0;
		//成功条数
		int successCount=0;
		//记录失败数据id
		List<String> errorListID=new ArrayList<>();
		List<String> errorListMsg=new ArrayList<>();
		try {
			for(int i=0;i<clist.size();i++) {
				adclist.add(clist.get(i));
				if(adclist.size()>=198) {
					adcArray=adclist.toArray(new AMAP_Data__c[adclist.size()]);
					
					UpsertResult[] results=connection.upsert("PrimaryKey4Group__c", adcArray);
					//SaveResult[] results=connection.create(adcArray);
					for (int v=0; v< results.length; v++) {
						if (results[v].isSuccess()) {
							logger.debug(v+". Successfully created record - Id: " + results[v].getId());
							successCount++;
						} else {
							com.sforce.soap.enterprise.Error[] errors = results[v].getErrors();
							for (int j=0; j< errors.length; j++) {
								logger.warn("第"+v+"条"+"ERROR creating record: " + errors[j].getMessage());
								errorListID.add(adcArray[v].getPrimaryKey4Group__c());
								errorListMsg.add( errors[j].getMessage());
								errorCount++;
							}
						}    
					}

					adcArray.clone();
					adclist.clear();
					statistics++;
				}else if(remainder==adclist.size()&&count==statistics) {
					adcArray=adclist.toArray(new AMAP_Data__c[adclist.size()]);
					UpsertResult[] results=connection.upsert("PrimaryKey4Group__c", adcArray);
					for (int v=0; v< results.length; v++) {
						if (results[v].isSuccess()) {
							logger.debug(v+". Successfully created record - Id: " + results[v].getId());
							successCount++;
						} else {
							com.sforce.soap.enterprise.Error[] errors = results[v].getErrors();
							for (int j=0; j< errors.length; j++) {
								logger.warn("第"+v+"条"+"ERROR creating record: " + errors[j].getClass());
								errorListID.add(adcArray[v].getPrimaryKey4Group__c());
								errorListMsg.add( errors[j].getMessage());
								errorCount++;
							}
						}    
					}

					adcArray.clone();
					adclist.clear();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			loginService.closeSFCE(connection);
		}
		//将错误信息插入 错误信息收集表
		if(0<errorCount) {
			interfaceLogService.addInterfaceLog(errorListID, errorListMsg, "Group");
		}
		logger.debug("此次流向数据表<AMAP_Data__c>导入成功条数为\t"+successCount+"\t条"+
				"\n失败条数为"+errorCount+"\t条"+"\n失败信息为:"+errorListMsg+"\n失败数据为:\n"+errorListID);

		map.put("Object","Group");
		map.put("total", clist.size());
		map.put("success", successCount);
		map.put("error", errorCount);

		return map;
	}


}
