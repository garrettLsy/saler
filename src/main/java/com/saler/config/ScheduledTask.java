package com.saler.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.saler.service.BlocService;
import com.saler.service.DirectionService;
import com.saler.service.DistributorsService;
import com.saler.service.HospitalsService;
import com.saler.service.SFCELoginService;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.sobject.Interface_Log__c;
import com.sforce.ws.ConnectionException;

@Component
public class ScheduledTask {


	@Autowired
	HospitalsService hospitalsService;
	@Autowired
	DirectionService directionService;
	@Autowired
	DistributorsService distributorsService;
	@Autowired
	BlocService blocService;
	@Scheduled(cron="0 7 10 15 * ?")//每月8日上午10:15触发   cron表达式详解
	//@Scheduled(fixedRate=10800000)
	public void test() {
		hospitalsService.add(null,null);
		directionService.add(null,null);
		distributorsService.add(null,null);
		blocService.add(null, null);
		
		




	}
}
