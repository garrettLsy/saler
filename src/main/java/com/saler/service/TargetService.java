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
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.saler.async.HospitalsAsync;
import com.saler.async.SaleforceAddAsync;
import com.saler.config.ReadTxtConfig;
import com.saler.mapper.TargetMapper;
import com.saler.pojo.Target;
import com.sforce.ws.ConnectionException;

import tk.mybatis.mapper.entity.Example;

@Service
public class TargetService {

	Logger logger=LoggerFactory.getLogger(getClass());
	@Autowired
	private TargetMapper tm;
	@Autowired
	private HospitalsAsync async;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private InterfaceLogService interfaceLogService;
	@Autowired
	private SaleforceAddAsync addAsync;

	public Map<String,Object> save(String beginTime,String endTime) throws ConnectionException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String toLeadDate=sdf.format(new Date());
		Map<String,Object> map=new HashMap<>();
		Example example=new Example(Target.class);
		if(null!=beginTime&&null!=endTime&&!"".equals(beginTime)&&!"".equals(endTime)) {
			example.createCriteria().andGreaterThanOrEqualTo("createon", beginTime)
			.andLessThanOrEqualTo("createon", endTime).orGreaterThanOrEqualTo("modifyon", beginTime)
			.andLessThanOrEqualTo("modifyon", endTime);
		}
		List<Target> list=tm.selectByExample(example);
	/*	try {
			async.addMysqlTarget(list,restTemplate);
		}catch(Exception e) {
			e.printStackTrace();
		}*/
		addAsync.addtTarGet(list, interfaceLogService);
		map.put("flag", 200);
		map.put("massage","数据正在导入请稍后刷新页面");
		ReadTxtConfig.inputTxt("TARGET表数据正在导入中。。。。。。"+"\t\t导入时间为:"+toLeadDate);
		return map;
	}
}
