package org.malagu.multitenant.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.malagu.multitenant.Constants;
import org.malagu.multitenant.script.DynamicResourceDatabasePopulator;
import org.malagu.multitenant.script.ScriptVarRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
@Service
public class ScriptServiceImpl implements ScriptService {
	
	@Autowired
	private ConfigurableApplicationContext applicationContext;
	
	@Autowired
	private DataSourceProperties properties;
	
	@Autowired(required = false)
	private List<ScriptVarRegister> scriptVarRegisters;
	
	@Autowired
	private DatabaseNameService databaseNameService;
	
	@Override
	public void runScripts(String organizationId, DataSource dataSource, String locations, String fallback) {
		Map<String, Object> vars = new HashMap<>();
		String databaseName = databaseNameService.getDatabaseName(organizationId);
		vars.put("organizationId", organizationId);
		vars.put("databaseName", databaseName);
		if (scriptVarRegisters != null) {
			for (ScriptVarRegister scriptVarRegister : scriptVarRegisters) {
				scriptVarRegister.register(vars);
			}
		}
		List<Resource> scripts = getScripts(locations, fallback);
		runScripts(scripts, dataSource, vars);
	}

	private List<Resource> getScripts(String locations, String fallback) {
		if (StringUtils.isEmpty(locations)) {
			String platform = this.properties.getPlatform();
			locations = "classpath*:" + fallback + "-" + platform + ".sql,";
			locations += "classpath*:" + fallback + ".sql";
		}
		return getResources(locations);
	}

	private List<Resource> getResources(String locations) {
		List<Resource> resources = new ArrayList<Resource>();
		for (String location : StringUtils.commaDelimitedListToStringArray(locations)) {
			try {
				for (Resource resource : this.applicationContext.getResources(location)) {
					if (resource.exists()) {
						resources.add(resource);
					}
				}
			}
			catch (IOException ex) {
				throw new IllegalStateException(
						"Unable to load resource from " + location, ex);
			}
		}
		return resources;
	}

	private void runScripts(List<Resource> resources, DataSource dataSource, Map<String, Object> vars) {
		if (resources.isEmpty()) {
			return;
		}
		DynamicResourceDatabasePopulator populator = new DynamicResourceDatabasePopulator();
		populator.setContinueOnError(this.properties.isContinueOnError());
		populator.setSeparator(this.properties.getSeparator());
		if (this.properties.getSqlScriptEncoding() != null) {
			populator.setSqlScriptEncoding(this.properties.getSqlScriptEncoding().name());
		}
		for (Resource resource : resources) {
			populator.addScript(resource);
		}
		populator.setVars(vars);
		DatabasePopulatorUtils.execute(populator, dataSource);
	}

	@Override
	public void runScripts(DataSource dataSource, String locations, String fallback) {
		runScripts(Constants.MASTER, dataSource, locations, fallback);
		
	}



}
