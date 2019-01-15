package com.saler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saler.config.PropertiesConfig;
import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SFCELoginService {

	Logger logger=LoggerFactory.getLogger(getClass());

	public EnterpriseConnection connection;

	//读取链接
	public EnterpriseConnection getconnection() {
		if(this.connection!=null) {
			return connection;
		}else {
			SFCDnewConnection();
			return connection;
		}
	}


	//SFCE登录
	public void SFCDnewConnection() {
	/*	PropertiesConfig pc=new PropertiesConfig("./application.properties");
	
		String userName=(String)pc.getProperties().getProperty("salesforce.userName");
		String userPassWord=pc.getProperties().getProperty("salesforce.password");*/
		String userName="wilson.chen@allianture.com";
		String userPassWord="demo12345PkYlAUnHjkfMV80KTCynK7Fpx";
		ConnectorConfig SFDCConfig = new ConnectorConfig();
		try {
			logger.debug(userName);
			logger.debug(userPassWord);
			SFDCConfig.setUsername(userName);
			SFDCConfig.setPassword(userPassWord);
			this.connection=Connector.newConnection(SFDCConfig);

		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {

		}
	}

	//断开SFCE链接
	public void closeSFCE(EnterpriseConnection connection) {
		try {
			if(null!=connection) {
				connection.logout();
			}else {
				return ;
			}
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


}
