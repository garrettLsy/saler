package com.saler.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.saler.async.SaleforceAddAsync;
import com.saler.mapper.InstitutionAgnmappingMapper;
import com.saler.pojo.InstitutionAgnmapping;
import tk.mybatis.mapper.entity.Example;
/***
 * 医院code临时关联service
 * @author lenovo
 *
 */
@Service
public class InstitutionAgnmappingService {

	@Autowired
	private InstitutionAgnmappingMapper agnmappingMapper;
	
	@Autowired
	private SaleforceAddAsync async;
	
	@Autowired
	private InterfaceLogService interfaceLogService;
	
	@Retryable(value= {Exception.class},maxAttempts=5,	backoff = @Backoff(delay = 5000,multiplier = 2))
	public Map<String,Object> add(String beginTime,String endTime){
		Map<String,Object> map=new HashMap<>();
		Example example=new Example(InstitutionAgnmapping.class);
		if(null!=beginTime&&null!=endTime&&!beginTime.equals("")&&!endTime.equals("")) {
			example.createCriteria().andGreaterThanOrEqualTo("modifyon",beginTime)
			.andLessThanOrEqualTo("modifyon", endTime).orGreaterThanOrEqualTo("createon", beginTime)
			.andLessThanOrEqualTo("createon", endTime);
		}
		List<InstitutionAgnmapping> list=agnmappingMapper.selectByExample(example);
		async.addInstitutionAgnmapping(list, interfaceLogService);
		map.put("flag", 0);
		map.put("errorMsg","");
		return map;
	}
}
