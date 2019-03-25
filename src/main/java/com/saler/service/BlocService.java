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
import com.saler.mapper.BlocMapper;
import com.saler.pojo.Bloc;
import com.sforce.ws.ConnectionException;

import tk.mybatis.mapper.entity.Example;


@Service
public class BlocService {

	@Autowired
	private BlocMapper bm;
	@Autowired
	InterfaceLogService interfaceLogService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private HospitalsAsync async;
	@Autowired
	private SaleforceAddAsync saleasync;
	Logger logger=LoggerFactory.getLogger(getClass());

	
	public Map<String,Object> add(String beginTime,String endTime) throws ConnectionException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String toLeadDate=sdf.format(new Date());
		Map<String,Object> map=new HashMap<>();
		//查询本地数据库
		Example example=new Example(Bloc.class);
		if(null!=beginTime&&null!=endTime&&!"".equals(beginTime)&&!"".equals(endTime)) {
			
			example.createCriteria().andIsNotNull("chain").andGreaterThanOrEqualTo("modifyon",beginTime)
			.andLessThanOrEqualTo("modifyon",endTime).orGreaterThanOrEqualTo("createon", beginTime)
			.andLessThanOrEqualTo("createon", endTime);
		}else {
			example.createCriteria().andIsNotNull("chain");
		}
		//List<Bloc> list=bm.selectByExample(example).subList(268400,362499);
		List<Bloc> list=bm.selectByExample(example);
		/*try {
			async.addMysqlBloc( list);
		} catch (Exception e) {
			System.out.println("木有灵魂的代码");	
			e.printStackTrace();
		}*/
		saleasync.addBloc(list,interfaceLogService);
		map.put("flag", 0);
		map.put("errorMsg","");
		ReadTxtConfig.inputTxt("医院集团关系表数据正在导入中。。。。。。"+"\t\t导入时间为:"+toLeadDate);
		return map;
	}
}
