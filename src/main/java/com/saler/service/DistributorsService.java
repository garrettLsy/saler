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
import org.springframework.web.client.RestTemplate;

import com.saler.async.HospitalsAsync;
import com.saler.mapper.DistributorsMapper;
import com.saler.pojo.Distributors;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.Distributor__c;
import com.sforce.ws.ConnectionException;
import com.sforce.soap.enterprise.Error;
import tk.mybatis.mapper.entity.Example;
@Service
public class DistributorsService {
	@Autowired
	private DistributorsMapper distributorsMapper;

	@Autowired
	InterfaceLogService interfaceLogService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private HospitalsAsync async;
	Logger logger=LoggerFactory.getLogger(getClass());

	/*private Distributor__c c;*/

	//插入
	public Map<String,Object> add(String beginTime,String endTime) {
		Map<String,Object> map=new HashMap<>();
		SFCELoginService login=new SFCELoginService();
		EnterpriseConnection connection=login.getconnection();
		Example example=new Example(Distributors.class);
		if(null!=beginTime&&null!=endTime) {
			example.createCriteria().andGreaterThanOrEqualTo("modifyon",beginTime)
			.andLessThanOrEqualTo("modifyon", endTime).orGreaterThanOrEqualTo("createon", beginTime)
			.andLessThanOrEqualTo("createon", endTime);
		}
		List<Distributors> list=distributorsMapper.selectByExample(example);
		/*try {
			async.addMysqlDistributors(restTemplate,list);
		}catch(Exception e) {
			System.out.println("没有灵魂的代码");
		}*/
		logger.debug("从数据库读取到\t"+list.size()+"\t条");
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
				c.setIs_Deleted__c(true);
			}else {
				c.setIs_Deleted__c(false);
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
			int statistics=0;
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
		map.put("Object","Distributor");
		map.put("total", csList.size());
		map.put("success", successCount);
		map.put("error", errorCount);


		return map;
	}




	public void  query() {
		SFCELoginService loginService=new SFCELoginService();
		EnterpriseConnection connection=loginService.getconnection();
		QueryResult queryResults;
		try {
			queryResults = connection.query("SELECT Id, LastActivityDate, CityID__c, "
					+ "NameEN__c, Comments__c, ProvinceID__c, City__c, Is_Deleted__c, Province__c, "
					+ "ModifyOn__c, ModifyBy__c, CreateBy__c,"
					+ " CreateOn__c, Distributor_Name_CN__c FROM Distributor__c");

			if (queryResults.getSize() > 0) {

				for (int i=0;i<queryResults.getRecords().length;i++) {
					Distributor__c c = (Distributor__c)queryResults.getRecords()[i];
					logger.debug("Id: " + c.getNameEN__c() + " - Name: "+c.getCreateOn__c()+" "+
							c.getComments__c()+" - Account: "+c.getName()+"\tit__C"+c.getDistributor_Name_CN__c());

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
