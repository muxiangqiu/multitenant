package org.malagu.multitenant.service;

import org.malagu.multitenant.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年11月6日
 */
@Service
public class DatabaseNameServiceImpl implements DatabaseNameService {

	@Value("${bdf3.multitenant.databaseNamePrefix:}")
	private String databaseNamePrefix;
	
	@Value("${bdf3.multitenant.databaseNameSuffix:}")
	private String databaseNameSuffix;
	
	@Value("${bdf3.multitenant.masterDatabaseName:}")
	private String masterDatabaseName;
	
	@Override
	public String getDatabaseName(String organizationId) {
		if (Constants.MASTER.equals(organizationId) && !StringUtils.isEmpty(masterDatabaseName)) {
			return masterDatabaseName;
		}
		String databaseName = organizationId;
		if (!StringUtils.isEmpty(databaseNamePrefix)) {
			databaseName = databaseNamePrefix + databaseName;
		}
		if (!StringUtils.isEmpty(databaseNameSuffix)) {
			databaseName += databaseNameSuffix;
		}
		return databaseName;
	}

}
