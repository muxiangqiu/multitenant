package org.malagu.multitenant.resource;

import org.malagu.multitenant.domain.Organization;

/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
public interface ResourceReleaser {

	void release(Organization organization);
}
