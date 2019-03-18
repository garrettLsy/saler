package com.saler.service;

import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.saler.async.HospitalsAsync;
import com.saler.async.SaleforceAddAsync;
import com.saler.mapper.HospitalsMapper;
import com.saler.pojo.Hospitals;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
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
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private HospitalsAsync async;
	@Autowired
	private SaleforceAddAsync addAsync;
	
	@Retryable(value= {Exception.class},maxAttempts=5,	backoff = @Backoff(delay = 5000,multiplier = 2))
	public Map<String,Object> add(String beginTime,String endTime) {
		Map<String,Object> map=new HashMap<>();
		Example example=new Example(Hospitals.class);
		if(null!=beginTime&&null!=endTime&&!"".equals(beginTime)&&!"".equals(endTime)) {
			example.createCriteria().andGreaterThanOrEqualTo("modifyon",beginTime)
			.andLessThanOrEqualTo("modifyon", endTime).orGreaterThanOrEqualTo("createon", beginTime)
			.andLessThanOrEqualTo("createon", endTime);
		}
		List<Hospitals> list=hm.selectByExample(example);
		/*try {
			async.addMysqlHospitals(list);
		}catch(Exception e) {
			System.out.println("没有灵魂的代码");
		}*/
		addAsync.addHospitals(list, interfaceLogService);
		map.put("flag", 0);
		map.put("errorMsg","");
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
