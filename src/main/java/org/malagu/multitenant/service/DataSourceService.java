package org.malagu.multitenant.service;

import javax.sql.DataSource;

import org.malagu.multitenant.domain.Organization;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
public interface DataSourceService {

	DataSource getDataSource(Organization organization);
	
	DataSource createDataSource(Organization organization);
	
	DataSource getOrCreateDataSource(Organization organization);
	
	void removeDataSource(Organization organization);

	DataSource getOrCreateDataSource(String organizationId);

	void clearDataSource();

	SingleConnectionDataSource createSingleConnectionDataSource(Organization organization);
}
