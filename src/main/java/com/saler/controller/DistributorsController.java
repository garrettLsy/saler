package com.saler.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.saler.service.BlocService;
import com.saler.service.DirectionService;
import com.saler.service.DistributorsService;
import com.saler.service.HospitalsService;
import com.saler.service.InstitutionAgnmappingService;
import com.saler.service.TXTInputService;
import com.saler.service.TargetService;
import com.saler.util.MD5Util;
import com.sforce.ws.ConnectionException;

@Controller
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
	@Autowired
	private InstitutionAgnmappingService mappingService;
	@Autowired
	private TXTInputService txt;
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getMaps(String objectType,String beginTime,String endTime) throws ConnectionException{
		logger.debug("开始时间:\t"+beginTime+"\t结束时间:\t"+endTime+"\t对象:\t"+objectType);
		Map<String,Object> map=new HashMap<>();
		//开始时间
		/*	String beginTime=(String) param.get("beginTime");
		//结束时间
		String endTime=(String) param.get("endTime");
		//对象
		String objectType=(String) param.get("objectType");*/
		//md5编码
		String header=request.getHeader("Authorization");
		header="12220d1e4548f9ce775c023cbf4a82a8";
		if(header.equals(MD5Util.returnMD5())) {
			if(objectType.equals("Pharm")) {
				return hospitalsService.add(beginTime, endTime);
			}else if(objectType.equals("Distributor")) {
				return distributorsService.add(beginTime, endTime);
			}else if(objectType.equals("SalesData")) {
				return directionService.add(beginTime, endTime);
			}else if(objectType.equals("Group")) {
				return blocService.add(beginTime, endTime);
			}else if(objectType.equals("Target")) {
				return targetService.save(beginTime, endTime);
			}else if(objectType.equals("InstitutionAgnmapping")) {
				return mappingService.add(beginTime, endTime);
			}
		}else {
			//md5匹配不上
			map.put("flag", 1);
			map.put("errorMsg", "请求不合法");
		}
		return map;
	} 
	
	@RequestMapping(value="/hello",method=RequestMethod.GET)
	public String demo(Model model) {
		model.addAttribute("list", txt.logTXTList());
		return "demo";
	}

}
