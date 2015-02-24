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
package org.tdmx.lib.control.job;

import org.tdmx.lib.control.datasource.ThreadLocalPartitionIdProvider;
import org.tdmx.lib.control.service.AccountZoneService;
import org.tdmx.lib.zone.service.ZoneService;
import org.tdmx.service.control.task.dao.ZoneInstallTask;

public class ZoneInstallJobExecutorImpl implements JobExecutor<ZoneInstallTask> {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------
	private ThreadLocalPartitionIdProvider zonePartition;
	private AccountZoneService accountZoneService;
	private ZoneService zoneService;

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	public ZoneInstallJobExecutorImpl() {
	}

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------

	@Override
	public String getType() {
		return ZoneInstallTask.class.getName();
	}

	@Override
	public void execute(Long id, ZoneInstallTask task) {
		// TODO

		// tx1: (r/o) lookup AccountZone in ControlDB to figure out the partition to provision the Zone to.
		// check that the AccountZone has our jobId so we have effectively the "lock" to update it later.

		// set the partition

		// tx2: (w) create the ZoneDB's zone in the partition

		// tx3: (w) remove our jobId from the AccountZone
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

	public ThreadLocalPartitionIdProvider getZonePartition() {
		return zonePartition;
	}

	public void setZonePartition(ThreadLocalPartitionIdProvider zonePartition) {
		this.zonePartition = zonePartition;
	}

	public AccountZoneService getAccountZoneService() {
		return accountZoneService;
	}

	public void setAccountZoneService(AccountZoneService accountZoneService) {
		this.accountZoneService = accountZoneService;
	}

	public ZoneService getZoneService() {
		return zoneService;
	}

	public void setZoneService(ZoneService zoneService) {
		this.zoneService = zoneService;
	}

}