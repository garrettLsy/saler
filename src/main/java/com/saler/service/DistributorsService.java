package com.saler.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.saler.async.HospitalsAsync;
import com.saler.async.SaleforceAddAsync;
import com.saler.mapper.DistributorsMapper;
import com.saler.pojo.Distributors;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Distributor__c;
import com.sforce.ws.ConnectionException;

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
	@Autowired
	private SaleforceAddAsync addAsync;
	Logger logger=LoggerFactory.getLogger(getClass());

	



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

	public Map<String,Object> add(String beginTime,String endTime) {
		Map<String,Object> map=new HashMap<>();

		Example example=new Example(Distributors.class);
		if(null!=beginTime&&null!=endTime) {
			example.createCriteria().andEqualTo("deleted",0).andGreaterThanOrEqualTo("modifyon",beginTime)
			.andLessThanOrEqualTo("modifyon", endTime).orGreaterThanOrEqualTo("createon", beginTime)
			.andLessThanOrEqualTo("createon", endTime);
		}else {
			example.createCriteria().andEqualTo("deleted",0);
		}
		List<Distributors> list=distributorsMapper.selectByExample(example);
		/*try {
			async.addMysqlDistributors(restTemplate,list);
		}catch(Exception e) {
			System.out.println("没有灵魂的代码");
		}*/
		addAsync.adddistributors(list, interfaceLogService);
		map.put("flag", 0);
		map.put("errorMsg","");
		return map;
	}

}
