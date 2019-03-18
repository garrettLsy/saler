package com.saler.async;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.saler.pojo.Bloc;
import com.saler.pojo.Direction;
import com.saler.pojo.Distributors;
import com.saler.pojo.Hospitals;
import com.saler.pojo.Target;
import com.saler.service.InterfaceLogService;
import com.saler.service.SFCELoginService;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.Error;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.AMAP_Data__c;
import com.sforce.soap.enterprise.sobject.Distributor__c;
import com.sforce.soap.enterprise.sobject.Pharm__c;
import com.sforce.soap.enterprise.sobject.SARA_Target_Hospital__c;
import com.sforce.ws.ConnectionException;
@Component
public class SaleforceAddAsync {

	Logger logger=LoggerFactory.getLogger(getClass());

	/***
	 * 
	 * @param list 集团医院关系数据集合
	 * @param interfaceLogService  
	 */
	@Async
	@Transactional(propagation=Propagation.REQUIRES_NEW)  // 重启开启事物，如果存在事物，就停止当前事物
	public void addBloc(List<Bloc> list,InterfaceLogService interfaceLogService) {
		SFCELoginService loginService=new SFCELoginService();
		//链接salesforc
		EnterpriseConnection connection=loginService.getconnection();
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

	}
	@Async
	@Transactional(propagation=Propagation.REQUIRES_NEW)  // 重启开启事物，如果存在事物，就停止当前事物
	public void addDirection(List<Direction> list,InterfaceLogService interfaceLogService,	String  timeVersion) {
		SFCELoginService loginService=new SFCELoginService();
		//链接salesforce
		EnterpriseConnection connection=loginService.getconnection();
		List<AMAP_Data__c> clist=new ArrayList<>();
		AMAP_Data__c c=null;
		Calendar calendar=null;
		for(int i=0;i<list.size();i++) {
		
			c=new AMAP_Data__c();
			c.setVersion__c(timeVersion);
			c.setType__c("Sales Data");
			c.setStyle__c(list.get(i).getStyle());
			c.setSales_Month__c(list.get(i).getMon());
			if(null!=list.get(i).getSalesDate()) {
			calendar=Calendar.getInstance();  //卡琳大
			calendar.setTime(list.get(i).getSalesDate());
			calendar.add(Calendar.HOUR_OF_DAY, 12);
			c.setSales_Date__c(calendar);
			}else {
				c.setSales_Date__c(null);
			}
			if(null!=list.get(i).getQty()&&list.get(i).getQty()!=0) {
				c.setQuantity__c((double)list.get(i).getQty());
			}else {
				c.setQuantity__c(0.0);
			}

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
			if(null!=list.get(i).getPharmNameCn()&&!list.get(i).getPharmNameCn().equals("NULL")) {
				c.setPharm_Name__c(list.get(i).getPharmNameCn());
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
		//成功条数
		int successCount=0;
		//失败条数统计
		int errorCount=0;
		//记录失败原Id
		List<String> errorListId=new ArrayList<>();
		//记录失败原因 
		List<String> errorListMsg=new ArrayList<>();
		try {
			for(int i=0;i<clist.size();i++) {
				adclist.add(clist.get(i));
				if(adclist.size()>=198) {
					//转换为数组格式
					adcArray=adclist.toArray(new AMAP_Data__c[adclist.size()]);
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
					//转换为数组格式
					adcArray=adclist.toArray(new AMAP_Data__c[adclist.size()]);
					//插入
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
	}

	@Async
	@Transactional(propagation=Propagation.REQUIRES_NEW)  // 重启开启事物，如果存在事物，就停止当前事物
	public void adddistributors(List<Distributors> list,InterfaceLogService interfaceLogService) {
		SFCELoginService login=new SFCELoginService();
		EnterpriseConnection connection=login.getconnection();
		List<Distributor__c> csList=new ArrayList<>();
		Distributor__c c = null;
		Calendar calendar=null;
		for(int i=0;i<list.size();i++) {
			c=new Distributor__c();
			c.setProvinceID__c(list.get(i).getProvinceid());
			if(null!=list.get(i).getProvinceName()&&!list.get(i).getProvinceName().equals("NULL")) {
				c.setProvince__c(list.get(i).getProvinceName().trim());
			}else {
				c.setProvince__c("");
			}
			if(null!=list.get(i).getNameen()&&!list.get(i).getNameen().equals("NULL")) {
				c.setNameEN__c(list.get(i).getNameen().trim());
			}else {
				c.setNameEN__c("");
			}

			if(null==list.get(i).getModifyon()) {
				c.setModifyOn__c(null);
			}else {
				calendar=Calendar.getInstance();
				calendar.setTime(list.get(i).getModifyon());
				c.setModifyOn__c(calendar);
			}
			c.setModifyBy__c(list.get(i).getModifyby());
			if(list.get(i).getDeleted().equals(0)) {
				c.setIs_Deleted__c(false);
			}else {
				c.setIs_Deleted__c(true);
			}
			if(null!=list.get(i).getNamecn()&&!list.get(i).getNamecn().equals("NULL")) {
				c.setDistributor_Name_CN__c(list.get(i).getNamecn().trim());
			}else {
				c.setDistributor_Name_CN__c("");
			}
			c.setDistributor_Code__c(list.get(i).getDistributorid());

			if(null==list.get(i).getCreateon()) {
				c.setCreateOn__c(null);
			}else {
				calendar=Calendar.getInstance();
				calendar.setTime(list.get(i).getCreateon());
				c.setCreateOn__c(calendar);
			}
			c.setCreateBy__c(list.get(i).getCreateby());
			if(null!=list.get(i).getComments()&&!list.get(i).getComments().equals("NULL")) {
				c.setComments__c(list.get(i).getComments());
			}else {
				c.setComments__c("");
			}
			if(null!=list.get(i).getCityId()&&!list.get(i).getCityId().equals("NULL")) {
				c.setCityID__c(list.get(i).getCityId());
			}else {
				c.setCityID__c("");
			}
			if(null!=list.get(i).getCityName()&&!list.get(i).getCityName().equals("NULL")) {
				c.setCity__c(list.get(i).getCityName());

			}else {
				c.setCity__c("");
			}
			if(null!=list.get(i).getNamecn()&&!list.get(i).getNamecn().equals("NULL")) {
				c.setName(list.get(i).getNamecn());
			}else {
				c.setName("");
			}
			csList.add(c);
		}
		logger.debug("放入到到List<Distributors__c>\t"+csList.size()+"\t条");
		//统计每次198插入多少次
		int count=csList.size()/198;
		//统计每次198插入过后，余数
		int remainder=csList.size()%198;
		//统计
		//int statistics=0;
		//计数
		int sum=0;
		//失败条数统计
		int errorCount=0;
		//成功条数
		int successCount=0;
		//记录失败数据 Id
		List<String> errorListId=new ArrayList<>();
		List<String> errorListMsg=new ArrayList<>();

		List<Distributor__c> cs2=new ArrayList<>();
		Distributor__c [] cs=null;
		try {
			for(int i=0;i<csList.size();i++) {
				cs2.add(csList.get(i));
				if(cs2.size()>=198) {
					cs=cs2.toArray(new Distributor__c[cs2.size()]);
					UpsertResult[] results=connection.upsert("Distributor_Code__c", cs);
					for (int v=0; v< results.length; v++) {
						if (results[v].isSuccess()) {
							logger.debug(v+". Successfully created record - Id: " + results[v].getId());
							successCount++;
						} else {
							Error[] errors = results[v].getErrors();
							for (int j=0; j< errors.length; j++) {
								logger.warn("第"+v+"条"+"ERROR creating record: " + errors[j].getMessage());
								errorListId.add(cs[v].getDistributor_Code__c());
								errorListMsg.add(errors[j].getMessage());
								errorCount++;
							}
						}    
					}
					cs2.clear();
					cs.clone();
					sum++;
				}else if (remainder==cs2.size()&&count==sum) {
					//转换为数据格式
					cs=cs2.toArray(new Distributor__c[cs2.size()]);

					UpsertResult[] results=connection.upsert("Distributor_Code__c", cs);
					for (int v=0; v< results.length; v++) {
						if (results[v].isSuccess()) {
							logger.debug(v+". Successfully created record - Id: " + results[v].getId());
							successCount++;					
						} else {
							Error[] errors = results[v].getErrors();
							for (int j=0; j< errors.length; j++) {
								logger.warn("第"+v+"条"+"ERROR creating record: " + errors[j].getMessage());
								errorListId.add(cs[v].getDistributor_Code__c());
								errorListMsg.add(errors[j].getMessage());
								errorCount++;					
							}
						}    
					}
					cs2.clear();
					cs.clone();
				}

			}
		}catch(Exception ce) {
			ce.printStackTrace();
		}finally {
			login.closeSFCE(connection);
		}
		//将错误信息插入 错误信息收集表
		if(0<errorCount) {
			interfaceLogService.addInterfaceLog(errorListId, errorListMsg, "Distributor");
		}

		logger.debug("此次銷售商表<Distributor__c>导入成功条数为\t"+successCount+"\t条"+
				"\n失败条数为"+errorCount+"\t条"+"\n失败信息为:"+errorListMsg+"\n失败数据为:\n"+errorListId+"\n版本号为:"+null);

	}

	@Async
	@Transactional(propagation=Propagation.REQUIRES_NEW)  // 重启开启事物，如果存在事物，就停止当前事物
	public void addHospitals(List<Hospitals> list,InterfaceLogService interfaceLogService) {
		SFCELoginService login=new SFCELoginService();
		EnterpriseConnection connection=login.getconnection();

		//记录失败数据  id:value
		List<Map<String,Object>> errorKeyValue=new ArrayList<>();
		//记录失败原因
		List<String> errorString=new ArrayList<>();
		//失败条数统计
		int errorCount=0;
		//错误数据id
		List<String> errorPharm_cList_ID=new ArrayList<>();


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
				if(null!=list.get(i).getTelno()&&!list.get(i).getTelno().equals("NULL")) {
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
				if(null!=list.get(i).getPostalcode()&&!list.get(i).getPostalcode().equals(0)) {
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
				if(null!=list.get(i).getNoofbeds()) {
				
					c.setNumberOfBeds__c((double)list.get(i).getNoofbeds());
				}else {
					c.setNumberOfBeds__c(0.0);
				}
				//时间转日历

				if(null==list.get(i).getModifyon()) {
					c.setModifyOn__c(null);

				}else {
					calendar=Calendar.getInstance();
					calendar.setTime(list.get(i).getModifyon());
					c.setModifyOn__c(calendar);
				}
				c.setModifyBy__c(list.get(i).getModifyby());
				if(null==list.get(i).getDeleted()) {
					c.setIs_Deleted__c(null);
				}
				else if(list.get(i).getDeleted().equals(0)) {
					c.setIs_Deleted__c(false);
				}else {
					c.setIs_Deleted__c(true);
				}
				//c.setGroup_Name__c(list.get(i).getg);
				c.setGrade__c(list.get(i).getGrade());
				//c.setDistributor__c(list.get(i).get);
				//时间转日历

				if(null==list.get(i).getCreateon()) {
					c.setCreateOn__c(null);
				}else {
					calendar=Calendar.getInstance();
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
				errorPharm_cList_ID.add(list.get(i).getHospitalid());
				errorCount++;
				e.printStackTrace();
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
		//成功条数
		int successCount=0;
		//用于记录数据
		List<Pharm__c> cs2=new ArrayList<>();
		//-----------------------------

		List<String> errorPharm_cMessage=new ArrayList<>();//错误数据内容
		//List<Hospitals> HospitalsList=new ArrayList<>();
		Pharm__c [] cs=null;
		try {
			for(int i=0;i<csList.size();i++) {
				cs2.add(csList.get(i));
				if(cs2.size()>=198) {
					cs=cs2.toArray(new Pharm__c[cs2.size()] );

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
					cs=cs2.toArray(new Pharm__c[cs2.size()] );
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
	}
	@Async
	@Transactional(propagation=Propagation.REQUIRES_NEW)  // 重启开启事物，如果存在事物，就停止当前事物
	public void addtTarGet(List<Target> list,InterfaceLogService interfaceLogService) {
		SFCELoginService loginService=new SFCELoginService();
		//登录saleforce
		EnterpriseConnection connection=loginService.getconnection();
		//记录失败id
		List<String> errorIdList=new ArrayList<>();
		//失败条数统计
		int errorCount=0;
		
		List<SARA_Target_Hospital__c> listArray=new ArrayList<>();
		SARA_Target_Hospital__c c=null;
		Calendar calendar=null;
		for(int i=0,p=list.size();i<p;i++) {
			try {
				c=new SARA_Target_Hospital__c();
				c.setExternal_Key__c(list.get(i).getId().toString());
				//TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
				calendar=Calendar.getInstance();
				calendar.setTime(list.get(i).getPeriod());
				calendar.add(Calendar.HOUR_OF_DAY, 12);
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
					c.setDeleted__c(false);
				}else {
					c.setDeleted__c(true);
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
		}catch(ConnectionException e) {
			e.printStackTrace();
		}catch(Exception ee){
			ee.printStackTrace();	
		}finally {
			loginService.closeSFCE(connection);
		}

		if(errorCount>0) {
			interfaceLogService.addInterfaceLog(errorIdList, errorPharm_cMessage, "Target");
		}

	} 
}
