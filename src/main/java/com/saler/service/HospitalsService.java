package com.saler.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saler.mapper.HospitalsMapper;
import com.saler.pojo.Hospitals;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.Interface_Log__c;
import com.sforce.soap.enterprise.sobject.Pharm__c;
import com.sforce.ws.ConnectionException;

import tk.mybatis.mapper.entity.Example;

@Service
public class HospitalsService {

	Logger logger=LoggerFactory.getLogger(getClass());
	@Autowired
	private HospitalsMapper hm;
	@Autowired
	InterfaceLogService interfaceLogService;
	/*@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private HospitalsAsync async;*/

	public List<Hospitals> query(){
		return hm.selectAll();
	}

	@SuppressWarnings("unlikely-arg-type")
	public Map<String,Object> add(String beginTime,String endTime) {
		Map<String,Object> map=new HashMap<>();
		//记录失败数据  id:value
		List<Map<String,Object>> errorKeyValue=new ArrayList<>();
		//记录失败原因
		List<String> errorString=new ArrayList<>();
		SFCELoginService login=new SFCELoginService();
		EnterpriseConnection connection=login.getconnection();
		Example example=new Example(Hospitals.class);
		if(null!=beginTime&&null!=endTime) {
			example.createCriteria().andGreaterThanOrEqualTo("modifyon",beginTime).andLessThanOrEqualTo("modifyon", endTime);
		}
		List<Hospitals> list=hm.selectByExample(example).subList(0, 100);

		/*try {
			async.addMysqlHospitals(restTemplate, list);
		}catch(Exception e) {
			System.out.println("没有灵魂的代码");
		}*/
		/*List<Hospitals> list=hm.selectAll();*/
		logger.debug("从数据库读取到\t"+list.size()+"\t条");
		List<Pharm__c> csList=new ArrayList<>();
		Pharm__c c = null;
		Calendar calendar=null;
		for(int i=0;i<list.size();i++) {
			c=new Pharm__c();
			try {
				if(null!=list.get(i).getComments()&&!list.get(i).getComments().equals("NULL")) {
					c.setComments__c(list.get(i).getComments());
				}else {
					c.setComments__c("");
				}
				c.setType__c(list.get(i).getType());
				c.setTier__c(list.get(i).getTier());
				if(list.get(i).equals("NULL")) {
					c.setTelephone_Number__c("");
				}else {
					c.setTelephone_Number__c(list.get(i).getTelno());
				}
				c.setStyle__c(list.get(i).getStyle());
				c.setStatus__c(list.get(i).getStatus());
				c.setProvinceID__c(list.get(i).getProvinceid());
				if(null!=list.get(i).getProvincename()&&!list.get(i).getProvincename().equals("NULL")) {
					c.setProvince__c(list.get(i).getProvincename().trim());
				}else {
					c.setProvince__c("");
				}
				if(list.get(i).getPostalcode().equals(0)) {
					c.setPostCode__c("");
				}else {
					c.setPostCode__c(list.get(i).getPostalcode());
				}
				if(null!=list.get(i).getNameen()&&!list.get(i).getNameen().equals("NULL")) {
					c.setPharmNameEN__c(list.get(i).getNameen().trim());
				}else {
					c.setPharmNameEN__c("");
				}
				if(null !=list.get(i).getNamecn()&&!list.get(i).getNamecn().equals("NULL")) {
					c.setPharm_Name_CN__c(list.get(i).getNamecn().trim());
				}else {
					c.setPharm_Name_CN__c("");

				}
				if(null!=list.get(i).getHospitalid()&&!list.get(i).getHospitalid().equals("NULL")) {
					c.setPharm_Code__c(list.get(i).getHospitalid().trim());			
				}else {
					c.setPharm_Code__c("");
				}
				if(null==list.get(i).getNoofbeds()) {
					c.setNumberOfBeds__c(0.0);
				}else {
					c.setNumberOfBeds__c((double)list.get(i).getNoofbeds());
				}
				//时间转日历
				calendar=Calendar.getInstance();
				if(null==list.get(i).getModifyon()) {
					c.setModifyOn__c(null);

				}else {
					calendar.setTime(list.get(i).getModifyon());
					c.setModifyOn__c(calendar);
				}
				c.setModifyBy__c(list.get(i).getModifyby());
				if(null==list.get(i).getDeleted()) {
					c.setIs_Deleted__c(null);
				}
				else if(list.get(i).getDeleted().equals(0)) {
					c.setIs_Deleted__c(true);
				}else {
					c.setIs_Deleted__c(false);
				}
				//c.setGroup_Name__c(list.get(i).getg);
				c.setGrade__c(list.get(i).getGrade());
				//c.setDistributor__c(list.get(i).get);
				//时间转日历
				calendar=Calendar.getInstance();
				if(null==list.get(i).getCreateon()) {
					c.setCreateOn__c(null);
				}else {
					calendar.setTime(list.get(i).getCreateon());
					c.setCreateOn__c(calendar);
				}
				c.setCreateBy__c(list.get(i).getCreateby());
				if(null!=list.get(i).getCompletename()&&!list.get(i).getCompletename().equals("NULL")) {
					c.setComplete_Pharm_Name_CN__c(list.get(i).getCompletename().trim());
				}else {
					c.setComplete_Pharm_Name_CN__c("");
				}
				if(null!=list.get(i).getComments()&&!list.get(i).getComments().equals("NULL")) {

					c.setComments__c(list.get(i).getComments());
				}else {
					c.setComments__c("");
				}
				if(null!=list.get(i).getCityid()&&!list.get(i).getCityid().equals("NULL")) {
					c.setCityID__c(list.get(i).getCityid());
				}else {
					c.setCityID__c("");
				}
				if(null!=list.get(i).getCityname()&&!list.get(i).getCityname().equals("NULL")) {
					c.setCity_Name__c(list.get(i).getCityname().trim());
				}else {
					c.setCity_Name__c("");
				}
				if(null!=list.get(i).getAddress()&&!list.get(i).getAddress().equals("NULL")) {
					c.setAddress__c(list.get(i).getAddress());
				}else {
					c.setAddress__c("");
				}
				if(null!=list.get(i).getNamecn()&&!list.get(i).getNamecn().equals("NULL")) {
					c.setName(list.get(i).getNamecn().trim());

				}else {
					c.setName("");
				}
			}catch(Exception e) {
				Map<String,Object> maps=new HashMap<>();
				maps.put(list.get(i).getHospitalid(), "数据异常");
				errorKeyValue.add(maps);
				continue;
			}
			csList.add(c);
		}
		logger.debug("放入到到List<Distributors__c>\t"+csList.size()+"\t条");
		//统计每次198插入多少次
		int count=csList.size()/198;
		//统计每次198插入过后，余数
		int remainder=csList.size()%198;
		//统计
		int statistics=0;
		//失败条数统计
		int errorCount=0;
		//成功条数
		int successCount=0;
		//用于记录数据
		List<Pharm__c> cs2=new ArrayList<>();
		//-----------------------------
		List<Pharm__c> errorPharm__cList=new ArrayList<>();//收集错误对象
		List<String> errorPharm_cList_ID=new ArrayList<>(); //错误数据id
		List<String> errorPharm_cMessage=new ArrayList<>();//错误数据内容
		//List<Hospitals> HospitalsList=new ArrayList<>();
		Pharm__c [] cs=null;
		try {
			for(int i=0;i<csList.size();i++) {
				cs2.add(csList.get(i));
				if(cs2.size()>=198) {
					cs=new Pharm__c[cs2.size()];
					for(int v=0;v<cs2.size();v++) {
						cs[v]=cs2.get(v);
					}
					UpsertResult[] results=connection.upsert("Pharm_Code__c", cs);
					for (int v=0; v< results.length; v++) {
						if (results[v].isSuccess()) {
							logger.debug(v+". Successfully created record - Id: " + results[v].getId());
							successCount++;					
						} else {
							com.sforce.soap.enterprise.Error[] errors = results[v].getErrors();
							for (int j=0; j< errors.length; j++) {
								logger.warn("第"+v+"条"+"ERROR creating record: " + errors[j].getMessage());
								errorPharm_cList_ID.add(list.get(i).getHospitalid());
								errorPharm_cMessage.add(errors[j].getMessage());
								errorCount++;
							}
						}  
					}
					cs2.clear();
					cs.clone();
					statistics++;
				}else if (remainder==cs2.size()&&count==statistics) {
					cs=new Pharm__c[cs2.size()];
					for(int v=0;v<cs2.size();v++) {
						cs[v]=cs2.get(v);
					}
					UpsertResult[] results=connection.upsert("Pharm_Code__c", cs);
					for (int v=0; v< results.length; v++) {
						if (results[v].isSuccess()) {
							logger.debug(v+". Successfully created record - Id: " + results[v].getId());
							successCount++;					
						} else {
							com.sforce.soap.enterprise.Error[] errors = results[v].getErrors();
							for (int j=0; j< errors.length; j++) {
								logger.warn("第"+v+"条"+"ERROR creating record: " + errors[j].getMessage());
								errorPharm_cList_ID.add(cs[v].getPharm_Code__c());
								errorPharm_cMessage.add(errors[j].getMessage());
								errorCount++;
							}
						}    
					}
					cs2.clear();
					cs.clone();
				}

			}
		}catch(ConnectionException ce) {
			ce.printStackTrace();
		}finally {
			login.closeSFCE(connection);
		}
		//将错误信息插入 错误信息收集表
		if(0<errorCount) {
			interfaceLogService.addInterfaceLog(errorPharm_cList_ID, errorPharm_cMessage, "Pharm");
		}


		logger.debug("此次医院数据表<Pharm__c>导入成功条数为\t"+successCount+"\t条"+
				"\n失败条数为"+errorCount+"\t条"+"\n失败信息为:"+errorString+"\n失败数据为:\n"+errorKeyValue+"\n版本号为:"+null);
		map.put("Object","Pharm");
		map.put("total", csList.size());
		map.put("success", successCount);
		map.put("error", errorCount);


		return map;
	}


	public void  querySFDC() {
		SFCELoginService loginService=new SFCELoginService();
		EnterpriseConnection connection=loginService.getconnection();
		QueryResult queryResults;
		try {
			queryResults = connection.query("SELECT Id, Name, CreateOn__c, CreateBy__c, CityID__c, Address__c, Complete_Pharm_Name_CN__c, Pharm_Code__c, Grade__c FROM Pharm__c");
			if (queryResults.getSize() > 0) {
				for (int i=0;i<queryResults.getRecords().length;i++) {
					//System.out.println(queryResults.getRecords().toString());
					Pharm__c c = (Pharm__c)queryResults.getRecords()[i];
					logger.debug("Id: " + c.getId() + " - Name: "+c.getPharm_Name_CN__c()+" "+
							c.getComments__c()+" - Account: "+c.getCreateBy__c()+"\tit__C"+c.getCreateOn__c());

				}
			}else {
				System.out.println("adsaf");
			}
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//退出
			loginService.closeSFCE(connection);
		}
	}

}
