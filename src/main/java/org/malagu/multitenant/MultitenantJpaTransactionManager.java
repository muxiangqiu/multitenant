package org.malagu.multitenant;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.malagu.multitenant.domain.Organization;
import org.malagu.multitenant.service.DataSourceService;
import org.malagu.multitenant.service.EntityManagerFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2016年8月16日
 */
@Component
public class MultitenantJpaTransactionManager extends JpaTransactionManager {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private EntityManagerFactoryService entityManagerFactoryService;
	
	@Autowired
	private DataSourceService dataSourceService;
	
	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		Organization organization = MultitenantUtils.peekOrganization();
		if (organization != null) {
			return entityManagerFactoryService.getOrCreateEntityManagerFactory(organization);
		}
		return super.getEntityManagerFactory();
		
	}

	@Override
	public DataSource getDataSource() {
		Organization organization = MultitenantUtils.peekOrganization();
		if (organization != null) {
			return dataSourceService.getOrCreateDataSource(organization);
		}
		return super.getDataSource();
	}	

	

}
