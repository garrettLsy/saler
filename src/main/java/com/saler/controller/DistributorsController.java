package com.saler.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saler.service.BlocService;
import com.saler.service.DirectionService;
import com.saler.service.DistributorsService;
import com.saler.service.HospitalsService;

@RestController
@RequestMapping("/sale")
public class DistributorsController {

	Logger logger=LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	private DirectionService directionService;
	@Autowired
	private DistributorsService distributorsService;
	@Autowired
	private BlocService blocService;
	@Autowired
	private HospitalsService hospitalsService;
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public Map<String,Object> getMaps(@RequestParam String beginTime,@RequestParam String endTime,
			@RequestParam String objectType){
		Map<String,Object> map=new HashMap<>();
		if(objectType.equals("Pharm")) {
			return hospitalsService.add(beginTime, endTime);
		}else if(objectType.equals("Distributor")) {
			return	distributorsService.add(beginTime, endTime);
		}else if(objectType.equals("SalesData")) {
			return directionService.add(beginTime, endTime);
		}else if(objectType.equals("Group")) {
			return	blocService.add(beginTime, endTime);
		}
		return map;
	} 
	
}
