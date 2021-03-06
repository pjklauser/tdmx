/*
 * TDMX - Trusted Domain Messaging eXchange
 * 
 * Enterprise B2B messaging between separate corporations via interoperable cloud service providers.
 * 
 * Copyright (C) 2014 Peter Klauser (http://tdmx.org)
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/.
 */
package org.tdmx.lib.control.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tdmx.lib.control.domain.DatabasePartition;
import org.tdmx.lib.control.service.DatabasePartitionCache;

public class DatabasePartitionConfigurationProvider implements DataSourceConfigurationProvider {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------
	private static final Logger log = LoggerFactory.getLogger(DatabasePartitionConfigurationProvider.class);

	private DatabasePartitionCache cache;

	// default properties
	private String driverClassname;
	private String url;
	private String username;
	private String password;

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------
	@Override
	public DatabaseConnectionInfo getPartitionInfo(String partitionId) {
		if (DynamicDataSource.UNITTEST_PARTITION_ID.equals(partitionId)
				|| DynamicDataSource.VALIDATION_PARTITION_ID.equals(partitionId)) {
			return new DatabaseConnectionInfo(getUsername(), getPassword(), getUrl(), getDriverClassname());
		}
		DatabasePartition partitionInfo = cache.findByPartitionId(partitionId);
		if (partitionInfo == null) {
			log.warn("Unable to find DatabasePartition with partitionId " + partitionId);
			return null;
		}

		DatabaseConnectionInfo result = new DatabaseConnectionInfo(partitionInfo.getUsername(),
				partitionInfo.getPassword(), partitionInfo.getUrl(), getDriverClassname());
		return result;
	}

	// -------------------------------------------------------------------------
	// PROTECTED METHODS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PRIVATE METHODS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PUBLIC ACCESSORS (GETTERS / SETTERS)
	// -------------------------------------------------------------------------

	public DatabasePartitionCache getCache() {
		return cache;
	}

	public void setCache(DatabasePartitionCache cache) {
		this.cache = cache;
	}

	public String getDriverClassname() {
		return driverClassname;
	}

	public void setDriverClassname(String driverClassname) {
		this.driverClassname = driverClassname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
