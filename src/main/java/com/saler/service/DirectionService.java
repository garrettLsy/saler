package com.saler.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.saler.async.HospitalsAsync;
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

		List<AMAP_Data__c> clist=new ArrayList<>();
		AMAP_Data__c c=null;
		Calendar calendar=null;
		calendar=Calendar.getInstance();
		for(int i=0;i<list.size();i++) {
			c=new AMAP_Data__c();
			c.setVersion__c(timeVersion);
			c.setType__c("Sales Data");
			c.setStyle__c(list.get(i).getStyle());
			c.setSales_Month__c(list.get(i).getMon());
			calendar.setTime(list.get(i).getSalesDate());
			c.setSales_Date__c(calendar);
			c.setQuantity__c((double)list.get(i).getQty());
			if(null!=list.get(i).getProvinceNameCn()&&!list.get(i).getProvinceNameCn().equals("NULL")) {
				c.setProvinceName__c(list.get(i).getProvinceNameCn());
			}else {
				c.setProvinceName__c("");

			}
			if(null!=list.get(i).getProductNameEn()&&!list.get(i).getProductNameEn().equals("NULL")) {
				c.setProduct_Name_EN__c(list.get(i).getProductNameEn());
			}else {
				c.setProduct_Name_EN__c("");
			}
			if(null!=list.get(i).getProductName()&&!list.get(i).getProductName().equals("NULL")) {
				c.setProduct_Name__c(list.get(i).getProductName());
			}else {
				c.setProduct_Name__c("");
			}
			c.setProduct_Code__c(list.get(i).getProductId());
			if(null!=list.get(i).getPharmNameCe()&&!list.get(i).getPharmNameCe().equals("NULL")) {
				c.setPharm_Name__c(list.get(i).getPharmNameCe());
			}else {
				c.setPharm_Name__c("");
			}
			c.setPharm_Code__c(list.get(i).getPharmId());
			c.setHospital_Name__c(list.get(i).getHospital());
			c.setHospital_Code__c(list.get(i).getHospitalId());
			c.setDistributor_Name__c(list.get(i).getDistName());
			c.setDistributor_Code__c(list.get(i).getDistId());
			c.setBrand__c(list.get(i).getBrand());
			if(null!=list.get(i).getAmapId()&&!list.get(i).getAmapId().equals("NULL")) {
				c.setPrimaryKey__c(list.get(i).getAmapId());
			}else {
				c.setPrimaryKey__c("");
			}
			if(null!=list.get(i).getGroupName()&&!list.get(i).getGroupName().equals("NULL")) {
			c.setGroup_Name__c(list.get(i).getGroupName());
			}else {
				c.setGroup_Name__c("");
			}
			if(null!=list.get(i).getProductId()&&!list.get(i).getProductId().equals("NULL")) {
				c.setProduct_Code__c(list.get(i).getProductId());
			}else {
				c.setProduct_Code__c("");
			}
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
		
		//记录失败原Id
		List<String> errorListId=new ArrayList<>();
		//记录失败原因 
		List<String> errorListMsg=new ArrayList<>();
		try {
			for(int i=0;i<clist.size();i++) {
				adclist.add(clist.get(i));
				if(adclist.size()>=198) {
					adcArray=new AMAP_Data__c[adclist.size()];
					for(int v=0;v<adclist.size();v++) {
						adcArray[v]=adclist.get(v);
					}
					//UpsertResult[] results=connection.upsert("PriadcArray);
					SaveResult[] results=connection.create(adcArray);
					for (int v=0; v< results.length; v++) {
						if (results[v].isSuccess()) {
							logger.debug(v+". Successfully created record - Id: " + results[v].getId());
							successCount++;
						} else {
							com.sforce.soap.enterprise.Error[] errors = results[v].getErrors();
							for (int j=0; j< errors.length; j++) {
								logger.warn("第"+v+"条"+"ERROR creating record: " + errors[j].getMessage());
								errorListId.add(adcArray[v].getPrimaryKey__c());
								errorListMsg.add(errors[j].getMessage());
								errorCount++;
							}
						}    
					}

					adcArray.clone();
					adclist.clear();
					statistics++;
				}else if(remainder==adclist.size()&&count==statistics) {
					adcArray=new AMAP_Data__c[adclist.size()];
					for(int v=0;v<adclist.size();v++) {
						adcArray[v]=adclist.get(v);
					}
					//UpsertResult[] results=connection.upsert("PrimaryKey__c", adcArray);
					SaveResult[] results=connection.create(adcArray);
					for (int v=0; v< results.length; v++) {
						if (results[v].isSuccess()) {
							logger.debug(v+". Successfully created record - Id: " + results[v].getId());
							successCount++;
						} else {
							com.sforce.soap.enterprise.Error[] errors = results[v].getErrors();
							for (int j=0; j< errors.length; j++) {
								logger.warn("第"+v+"条"+"ERROR creating record: " + errors[j].getClass());
								errorListId.add(adcArray[v].getPrimaryKey__c());
								errorListMsg.add(errors[j].getMessage());
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
			interfaceLogService.addInterfaceLog(errorListId, errorListMsg, "SalesData");
		}
		logger.debug("此次流向数据表<AMAP_Data__c>导入成功条数为\t"+successCount+"\t条"+
				"\n失败条数为"+errorCount+"\t条"+"\n失败信息为:"+errorListMsg+"\n失败数据为:\n"+errorListId+"\n版本号为:"+timeVersion);
		map.put("Object","SalesData");
		map.put("total", clist.size());
		map.put("success", successCount);
		map.put("error", errorCount);
		map.put("version", timeVersion);
		return map;
	}



}
