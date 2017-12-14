package org.malagu.multitenant.resource;

import org.malagu.multitenant.domain.Organization;
import org.malagu.multitenant.service.DataSourceService;
import org.malagu.multitenant.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
@Component
@Order(1500)
public class InitializedDataResourceAllocator implements ResourceAllocator {
	
	@Autowired
	private DataSourceService dataSourceService;
	
	@Value("${resourceScript:}")
	private String resourceScript;
	
	@Autowired
	private ScriptService scriptService;
	
	

	@Override
	public void allocate(Organization organization) {
		scriptService.runScripts(organization.getId(), dataSourceService.getDataSource(organization), resourceScript, "multitenant");
	}
	
	
	
}
