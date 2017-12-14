package org.malagu.multitenant.service;

import org.malagu.multitenant.domain.Organization;

/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
public interface OrganizationService {

	Organization get(String id);

	void register(Organization organization);
	
	void releaseResource(Organization organization);

	void allocteResource(Organization organization);
}
