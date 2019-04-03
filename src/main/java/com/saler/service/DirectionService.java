package com.saler.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.saler.async.HospitalsAsync;
import com.saler.async.SaleforceAddAsync;
import com.saler.config.ReadTxtConfig;
import com.saler.mapper.DirectionMapper;
import com.saler.pojo.Direction;
import com.saler.util.LoggerUtil;
import com.sforce.ws.ConnectionException;

import tk.mybatis.mapper.entity.Example;

@Service
public class DirectionService extends LoggerUtil{
	@Autowired
	private DirectionMapper dm;
	@Autowired
	InterfaceLogService interfaceLogService;
	@Autowired
	private HospitalsAsync async;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private SaleforceAddAsync addAsync;
	public Map<String,Object> add(String beginTime,String endTime) throws ConnectionException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String toLeadDate=sdf.format(new Date());
		Map<String,Object> map=new HashMap<>();
	
		//查询本地数据库
		Example example=new Example(Direction.class);
		example.setOrderByClause("amap_id ASC");
		if(null!=beginTime&&null!=endTime&&!"".equals(beginTime)&&!"".equals(endTime)) {
			
			example.createCriteria().andGreaterThanOrEqualTo("salesDate",beginTime).andLessThanOrEqualTo("salesDate",endTime);
		}
		List<Direction> list=dm.selectByExample(example);
		//List<Direction> list=dcm.selectByExample();
		
		/*try {
			async.addMysqlDirction(restTemplate, list, timeVersion);
		} catch (Exception e) {
			System.out.println("木有灵魂的代码");	
			e.printStackTrace();
		}*/
		map.put("flag", 200);
		map.put("massage","数据正在导入请稍后刷新页面");
		addAsync.addDirection(list, interfaceLogService);
		ReadTxtConfig.inputTxt("流向数据数据正在导入中。。。。。。"+"\t\t导入时间为:"+toLeadDate);
		return map;
	}

}
