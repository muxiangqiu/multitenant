package org.malagu.multitenant.resource;
/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */

import org.malagu.multitenant.domain.DataSourceInfo;
import org.malagu.multitenant.domain.Organization;
import org.malagu.multitenant.service.DataSourceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(500)
public class DataSourceResourceAllocator implements ResourceAllocator {
	
	@Autowired
	private DataSourceInfoService dataSourceInfoService;

	@Override
	public void allocate(Organization organization) {
		DataSourceInfo dataSourceInfo = dataSourceInfoService.allocate(organization);
		organization.setDataSourceInfoId(dataSourceInfo.getId());
		dataSourceInfo.setDepletionIndex(dataSourceInfo.getDepletionIndex() + 1);
	}

}
