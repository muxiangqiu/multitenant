package org.malagu.multitenant.service;

import java.util.List;

import org.malagu.linq.JpaUtil;
import org.malagu.multitenant.domain.Organization;
import org.malagu.multitenant.resource.ResourceAllocator;
import org.malagu.multitenant.resource.ResourceReleaser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
@Service
@Transactional(readOnly = true)
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private List<ResourceAllocator> allocators;
	
	@Autowired
	private List<ResourceReleaser> releasers;
	
	@Override
	public Organization get(String id) {
		return JpaUtil.getOne(Organization.class, id);
	}

	@Override
	@Transactional
	public void register(Organization organization) {
		for (ResourceAllocator allocator : allocators) {
			allocator.allocate(organization);
		}
		JpaUtil.persist(organization);
	}
	
	@Override
	@Transactional
	public void allocteResource(Organization organization) {
		for (ResourceAllocator allocator : allocators) {
			allocator.allocate(organization);
		}
	}

	@Override
	@Transactional
	public void releaseResource(Organization organization) {
		for (ResourceReleaser releaser : releasers) {
			releaser.release(organization);
		}
	}

}
