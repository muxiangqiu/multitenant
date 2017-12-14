package org.malagu.multitenant.service;


import org.malagu.multitenant.domain.DataSourceInfo;
import org.malagu.multitenant.domain.Organization;

/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
public interface DataSourceInfoService {

	DataSourceInfo get(Organization organization);
	
	DataSourceInfo allocate(Organization organization);

}
