package org.malagu.multitenant;

import org.malagu.linq.JpaUtil;
import org.malagu.linq.initiator.JpaUtilAble;
import org.malagu.multitenant.domain.DataSourceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
@Component
public class MasterDataSourceInitiator implements JpaUtilAble {

	@Autowired
	private DataSourceProperties properties;
	
	@Override
	@Transactional
	public void afterPropertiesSet(ApplicationContext applicationContext) {
		boolean isExist = true;
		DataSourceInfo dataSourceInfo = JpaUtil.getOne(DataSourceInfo.class, Constants.MASTER);
		if (dataSourceInfo == null) {
			dataSourceInfo = new DataSourceInfo();
			isExist = false;
		}
		dataSourceInfo.setId(Constants.MASTER);
		dataSourceInfo.setDriverClassName(properties.determineDriverClassName());
		dataSourceInfo.setEnabled(true);
		dataSourceInfo.setJndiName(properties.getJndiName());
		dataSourceInfo.setName("主公司数据源");
		dataSourceInfo.setUrl(properties.determineUrl());
		dataSourceInfo.setUsername(properties.determineUsername());
		dataSourceInfo.setPassword(properties.determinePassword());
		dataSourceInfo.setShared(true);
		dataSourceInfo.setDepletionIndex(1);
		dataSourceInfo.setType(properties.getType() != null ? properties.getType().getName() : null);
		if (isExist) {
			JpaUtil.merge(dataSourceInfo);
		} else {
			JpaUtil.persist(dataSourceInfo);
		}
		
	}

}
