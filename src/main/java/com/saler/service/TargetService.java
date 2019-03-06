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
import com.saler.mapper.TargetMapper;
import com.saler.pojo.Target;

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


	public Map<String,Object> save(String beginTime,String endTime){
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
		map.put("flag", 0);
		map.put("errorMsg","");
		return map;
	}
}
