package com.saler.async;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.saler.pojo.Bloc;
import com.saler.pojo.Direction;
import com.saler.pojo.Distributors;
import com.saler.pojo.Hospitals;
import com.saler.pojo.Target;
import com.saler.util.LoggerUtil;
@Component
public class HospitalsAsync {
	@Autowired
	private RestTemplate restTemplates;
	@Async
	@Transactional(propagation=Propagation.REQUIRED)
	public void addMysqlHospitals(List<Hospitals> list) {
		restTemplates.postForObject(LoggerUtil.HERO_HTTP+"/sale/addhospitals",JSONArray.parseArray(JSON.toJSONString(list)),Boolean.class);
	}
	
	@Async
	@Transactional(propagation=Propagation.REQUIRED)
	public void addMysqlDistributors(RestTemplate restTemplate,List<Distributors> list) {
		restTemplates.postForObject(LoggerUtil.HERO_HTTP+"/sale/adddistributors", JSONArray.parseArray(JSON.toJSONString(list)), Boolean.class);
	}
	@Async
	@Transactional(propagation=Propagation.REQUIRED)
	public void addMysqlDirction(RestTemplate restTemplate,List<Direction> list,String version) {
		Map<String,Object> map=new HashMap<>();
		map.put("list",JSON.toJSONString(list));
		map.put("version", version);
		System.out.println("sdaf");
		restTemplate.postForObject(LoggerUtil.HERO_HTTP+"/sale/adddirection", map, Boolean.class);
	}
	@Async
	@Transactional(propagation=Propagation.REQUIRED)
	public void addMysqlBloc(List<Bloc> list) {
		restTemplates.postForObject(LoggerUtil.HERO_HTTP+"/sale/addbloc", JSONArray.parseArray(JSON.toJSONString(list)), Boolean.class);
	}
	

	@Async
	@Transactional(propagation=Propagation.REQUIRED)
	public void addMysqlTarget(List<Target> list,RestTemplate restTemplate) {
		restTemplate.postForObject(LoggerUtil.HERO_HTTP+"/sale/addtarget", JSONArray.parseArray(JSON.toJSONString(list)), Boolean.class);
		
	}
	
}
