package org.malagu.multitenant.resource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.malagu.multitenant.domain.Organization;
import org.malagu.multitenant.service.EntityManagerFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Component;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
@Component
@Order(1000)
public class DatabaseResourceReleaser implements ResourceReleaser {
	
	@Autowired
	private DataSourceProperties properties;
	
	@Autowired
	private EntityManagerFactory emf;
	
	@Autowired
	private EntityManagerFactoryService entityManagerFactoryService;

	@Override
	public void release(Organization organization) {
		entityManagerFactoryService.removeEntityManagerFactory(organization);
		EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(emf);
		if (!EmbeddedDatabaseConnection.isEmbedded(properties.determineDriverClassName())) {
			em.createNativeQuery("drop database " + organization.getId()).executeUpdate();
		}
		
	}
}
