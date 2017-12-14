package org.malagu.multitenant.listener;

import org.malagu.multitenant.Constants;
import org.malagu.multitenant.domain.DataSourceInfo;
import org.malagu.multitenant.domain.Organization;
import org.malagu.multitenant.service.DatabaseNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
@Component
@Order(1000)
public class MysqlDataSourceCreateListener implements DataSourceCreateListener {

	@Autowired
	private DatabaseNameService databaseNameService;
	
	@Override
	public void onCreate(Organization organization, DataSourceInfo dataSourceInfo,
						 DataSourceBuilder dataSourceBuilder) {
		if ("com.mysql.jdbc.Driver".equals(dataSourceInfo.getDriverClassName())) {
			String url = dataSourceInfo.getUrl();
			if (!url.contains(databaseNameService.getDatabaseName(Constants.MASTER))) {
				String databaseName = databaseNameService.getDatabaseName(Constants.MASTER);
				url += "/" + databaseName;
				dataSourceBuilder.url(url);
			}
		}
		
	}

	

	

}
