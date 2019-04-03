package com.saler.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.saler.async.HospitalsAsync;
import com.saler.async.SaleforceAddAsync;
import com.saler.config.ReadTxtConfig;
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

	
	public Map<String,Object> add(String beginTime,String endTime) throws ConnectionException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String toLeadDate=sdf.format(new Date());
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
		//
		
			addAsync.addHospitals(list, interfaceLogService);
		
		map.put("flag", 200);
		map.put("massage","数据正在导入请稍后刷新页面");
		ReadTxtConfig.inputTxt("医院表数据正在导入中。。。。。。"+"\t\t导入时间为:"+toLeadDate);
		return map;
	}


	@Recover
	public Map<String,Object> recover(ConnectionException ne) {
		ReadTxtConfig.inputTxt("网络异常 重试五次均失败  -_-");
		return null;
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
