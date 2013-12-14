package org.tdmx.console.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tdmx.console.application.domain.DnsResolverListDO;
import org.tdmx.console.application.domain.DomainObjectChangesHolder;
import org.tdmx.console.application.domain.DomainObjectFieldChanges;
import org.tdmx.console.application.domain.validation.FieldError;
import org.tdmx.console.application.search.SearchService;
import org.xbill.DNS.ResolverConfig;


public class DnsResolverServiceImpl implements DnsResolverService {

	//-------------------------------------------------------------------------
	//PUBLIC CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	//-------------------------------------------------------------------------
	public static final String SYSTEM_DNS_RESOLVER_LIST_ID = "system-dns-resolver-list";
	public static final String SYSTEM_DNS_RESOLVER_LIST_NAME = "system";
	
	private ObjectRegistry objectRegistry;
	private SearchService searchService;
	
	//-------------------------------------------------------------------------
	//CONSTRUCTORS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public void updateSystemResolverList() {
		// TODO Auto-generated method stub
		DnsResolverListDO systemList = objectRegistry.getDnsResolverList(SYSTEM_DNS_RESOLVER_LIST_ID);
		if ( systemList == null ) {
			DomainObjectChangesHolder h = new DomainObjectChangesHolder();
			systemList = new DnsResolverListDO();
			systemList.setId(SYSTEM_DNS_RESOLVER_LIST_ID);
			systemList.setActive(Boolean.TRUE);
			systemList.setName(SYSTEM_DNS_RESOLVER_LIST_NAME);
			systemList.setHostnames(getSystemDnsHostnames());
			objectRegistry.notifyAdd(systemList, h);
			searchService.update(h);
		} else {
			DnsResolverListDO systemListCopy = new DnsResolverListDO(systemList);
			systemListCopy.setHostnames(getSystemDnsHostnames());
			createOrUpdate(systemListCopy);
		}
	}

	@Override
	public List<FieldError> createOrUpdate(DnsResolverListDO resolverList) {
		DomainObjectChangesHolder holder = new DomainObjectChangesHolder();
		List<FieldError> validation = resolverList.validate();
		if ( !validation.isEmpty() ) {
			return validation;
		}
		DnsResolverListDO existing = objectRegistry.getDnsResolverList(resolverList.getId());
		if ( existing == null ) {
			objectRegistry.notifyAdd(resolverList, holder);
			searchService.update(holder);
		} else {
			DomainObjectFieldChanges changes = existing.merge(resolverList);
			if ( !changes.isEmpty() ) {
				objectRegistry.notifyModify(changes, holder);
				searchService.update(holder);
				
				//TODO if system's hostnames change then issue audit warning
			}
		}
		return validation;
	}

	@Override
	public void delete(DnsResolverListDO existing) {
		DomainObjectChangesHolder holder = new DomainObjectChangesHolder();
		objectRegistry.notifyRemove(existing, holder);
		searchService.update(holder);
	}

    //-------------------------------------------------------------------------
	//PROTECTED METHODS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private List<String> getSystemDnsHostnames() {
		List<String> hosts= new ArrayList<>();
		
		String[] list = ResolverConfig.getCurrentConfig().servers();
		if ( list != null ) {
			for( String h : list ) {
				hosts.add(h);
			}
		}
		return Collections.unmodifiableList(hosts);
	}
	
	//-------------------------------------------------------------------------
	//PUBLIC ACCESSORS (GETTERS / SETTERS)
	//-------------------------------------------------------------------------

	public ObjectRegistry getObjectRegistry() {
		return objectRegistry;
	}

	public void setObjectRegistry(ObjectRegistry objectRegistry) {
		this.objectRegistry = objectRegistry;
	}

	public SearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

}
