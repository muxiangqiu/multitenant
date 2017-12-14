package org.malagu.multitenant.service;

import javax.sql.DataSource;

/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
public interface ScriptService {

	void runScripts(DataSource dataSource, String locations, String fallback);

	void runScripts(String organizationId, DataSource dataSource, String locations, String fallback);

}
