package com.saler.service;

import java.sql.SQLDataException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.saler.config.ReadTxtConfig;
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

	
	public Map<String,Object> add(String beginTime,String endTime) throws ConnectionException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String toLeadDate=sdf.format(new Date());
		Map<String,Object> map=new HashMap<>();
		Example example=new Example(Distributors.class);
		if(null!=beginTime&&null!=endTime&&!"".equals(beginTime)&&!"".equals(endTime)) {
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
		map.put("flag", 200);
		map.put("massage","数据正在导入请稍后刷新页面");
		ReadTxtConfig.inputTxt("经销商数据正在导入中。。。。。。"+"\t\t导入时间为:"+toLeadDate);
		return map;
	}

}
