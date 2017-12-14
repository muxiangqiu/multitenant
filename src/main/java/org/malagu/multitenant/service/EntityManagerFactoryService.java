package org.malagu.multitenant.service;

import org.malagu.multitenant.domain.Organization;

import javax.persistence.EntityManagerFactory;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
public interface EntityManagerFactoryService {

	EntityManagerFactory getEntityManagerFactory(Organization organization);
	
	EntityManagerFactory createEntityManagerFactory(Organization organization);

	EntityManagerFactory getOrCreateEntityManagerFactory(Organization organization);
	
	void removeEntityManagerFactory(Organization organization);

	void generateTables(Organization organization);

	EntityManagerFactory createTempEntityManagerFactory(Organization organization);

}
