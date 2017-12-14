package org.malagu.multitenant.listener;

import org.malagu.multitenant.domain.DataSourceInfo;
import org.malagu.multitenant.domain.Organization;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
public interface DataSourceCreateListener {

	void onCreate(Organization organization, DataSourceInfo dataSourceInfo, DataSourceBuilder dataSourceBuilder);
}
