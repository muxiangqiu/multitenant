package org.malagu.multitenant.strategy;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.malagu.linq.strategy.GetEntityManagerFactoryStrategy;
import org.malagu.multitenant.MultitenantUtils;
import org.malagu.multitenant.domain.Organization;
import org.malagu.multitenant.service.EntityManagerFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
@Component("multitenant.getEntityManagerFactoryStrategyImpl")
@Primary
public class GetEntityManagerFactoryStrategyImpl implements
		GetEntityManagerFactoryStrategy {
	
	@Autowired
	private List<EntityManagerFactory> entityManagerFactories;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private EntityManagerFactoryService entityManagerFactoryService;

	@Override
	public EntityManagerFactory getEntityManagerFactory(Class<?> domainClass) {
		RuntimeException exception = new RuntimeException("entityManagerFactories can not be empty!");
		Organization organization = MultitenantUtils.peekOrganization();
		if (organization != null) {
			EntityManagerFactory entityManagerFactory = entityManagerFactoryService.getOrCreateEntityManagerFactory(organization);
			try {
				if (domainClass == null) {
					return entityManagerFactory;
				} else {
					entityManagerFactory.getMetamodel().entity(domainClass);
					return entityManagerFactory;
				}
			} catch (IllegalArgumentException e) {
				exception = e;
			}
		}
		
		if (domainClass == null) {
			return entityManagerFactory;
		}
		
		for (EntityManagerFactory emf : entityManagerFactories) {
			try {
				emf.getMetamodel().entity(domainClass);
				return emf;
			} catch (IllegalArgumentException e) {
				exception = e;
			}
		}
		throw exception;
	}

}
