package com.saler.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saler.async.SaleforceAddAsync;
import com.saler.config.ReadTxtConfig;
import com.saler.mapper.ProductHierarchyMapper;
import com.saler.pojo.ProductHierarchy;
import com.sforce.ws.ConnectionException;

@Service
public class ProductHierarchyService {
	@Autowired
	private ProductHierarchyMapper hierarchyMapper;
	@Autowired
	private SaleforceAddAsync async;
	@Autowired
	private InterfaceLogService interfaceLogService;
	
	 public Map<String,Object>  add() throws ConnectionException {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String toLeadDate=sdf.format(new Date());
			Map<String,Object> map=new HashMap<>();
			//Example example=new Example(ProductHierarchy.class);
			List<ProductHierarchy> list=hierarchyMapper.selectAll();
			async.addProductHierarchy(list, interfaceLogService);
			map.put("flag", 200);
			map.put("massage","数据正在导入请稍后刷新页面");
			ReadTxtConfig.inputTxt("医院表数据正在导入中。。。。。。"+"\t\t导入时间为:"+toLeadDate);
			return map;
	 }
}
