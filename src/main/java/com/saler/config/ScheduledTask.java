package com.saler.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.saler.service.BlocService;
import com.saler.service.DirectionService;
import com.saler.service.DistributorsService;
import com.saler.service.HospitalsService;
import com.saler.service.InstitutionAgnmappingService;
import com.saler.service.ProductHierarchyService;
import com.saler.service.TargetService;
import com.sforce.ws.ConnectionException;

@Component
public class ScheduledTask {


	@Autowired
	private HospitalsService hospitalsService;
	@Autowired
	private DirectionService directionService;
	@Autowired
	private DistributorsService distributorsService;
	@Autowired
	private BlocService blocService;
	@Autowired
	private InstitutionAgnmappingService  agnmappingService;
	@Autowired
	private ProductHierarchyService hierarchyService;
	@Autowired
	private TargetService targetService;
	//@Scheduled(cron="0 7 10 15 * ?")//每月8日上午10:15触发   cron表达式详解0 0 12 * * ? 
	//@Scheduled(fixedRate=10800000)
	/*@Scheduled(cron="0 0 15 * * ?")*/
	public void test() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
		try {
			hospitalsService.add(sdf.format(new Date())+"-01-01",sdf2.format(new Date()));
			directionService.add(sdf.format(new Date())+"-01-01",sdf2.format(new Date()));
			distributorsService.add(sdf.format(new Date())+"-01-01",sdf2.format(new Date()));
			blocService.add(sdf.format(new Date())+"-01-01",sdf2.format(new Date()));
			agnmappingService.add(sdf.format(new Date())+"-01-01",sdf2.format(new Date()));
			targetService.save(sdf.format(new Date())+"-01-01",sdf2.format(new Date()));
			hierarchyService.add();
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
