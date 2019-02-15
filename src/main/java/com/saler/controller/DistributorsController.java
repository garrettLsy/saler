package com.saler.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.saler.service.BlocService;
import com.saler.service.DirectionService;
import com.saler.service.DistributorsService;
import com.saler.service.HospitalsService;
import com.saler.service.TargetService;
import com.saler.util.MD5Util;

@RestController
@RequestMapping("/sale")
public class DistributorsController {

	Logger logger=LoggerFactory.getLogger(getClass());

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private DirectionService directionService;
	@Autowired
	private DistributorsService distributorsService;
	@Autowired
	private BlocService blocService;
	@Autowired
	private HospitalsService hospitalsService;
	@Autowired
	private TargetService targetService;
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Map<String,Object> getMaps(@RequestBody JSONObject param){
		logger.debug(param.toJSONString());
		Map<String,Object> map=new HashMap<>();
		String beginTime=param.getString("beginTime");
		String endTime=param.getString("endTime");
		String objectType=param.getString("objectType");
		String header=request.getHeader("Authorization");
		if(header.equals(MD5Util.returnMD5())) {

			if(objectType.equals("Pharm")) {
				return hospitalsService.add(beginTime, endTime);
			}else if(objectType.equals("Distributor")) {
				return	distributorsService.add(beginTime, endTime);
			}else if(objectType.equals("SalesData")) {
				return directionService.add(beginTime, endTime);
			}else if(objectType.equals("Group")) {
				return	blocService.add(beginTime, endTime);
			}else if(objectType.equals("Target")) {
				return targetService.save(beginTime, endTime);
			}
		}else {
			map.put("error", "请求不合法");
		}
		return map;
	} 

}
