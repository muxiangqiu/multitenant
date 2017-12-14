package org.malagu.multitenant.service;
/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年11月6日
 */
public interface DatabaseNameService {
	String getDatabaseName(String organizationId);
}
