package com.saler.saler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saler.config.AESConfig;
import com.saler.service.BlocService;
import com.saler.service.DirectionService;
import com.saler.service.DistributorsService;
import com.saler.service.HospitalsService;
import com.saler.service.TargetService;



@RunWith(SpringRunner.class)
@SpringBootTest
public class SalerApplicationTests {

	@Autowired
	DistributorsService ds;
	@Autowired
	HospitalsService hs;
	@Autowired
	DirectionService dn;
	@Autowired
	BlocService gs;
	@Autowired
	TargetService ts;

	@Test
	public void contextLoads() {
		/*SFCELoginService login=new SFCELoginService();
		login.getconnection();*/
		 byte[] encr=AESConfig.encrypt("hello world", "123456");
		 System.out.println(new String(encr));
		 
		 byte[] decry= AESConfig.decrypt(encr, "123456");
		 System.out.println(new String(decry));
	}

	@Test
	public void query() {
	/*	gs.add(null, null);
		ds.add(null,null);
		hs.add(null, null);
		dn.add(null,null);*/
		ts.save(null,null);
	}


	
}

