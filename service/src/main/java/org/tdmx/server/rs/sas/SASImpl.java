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
package org.tdmx.server.rs.sas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tdmx.core.system.lang.EnumUtils;
import org.tdmx.core.system.lang.StringUtils;
import org.tdmx.lib.common.domain.Job;
import org.tdmx.lib.common.domain.PageSpecifier;
import org.tdmx.lib.control.domain.Account;
import org.tdmx.lib.control.domain.AccountSearchCriteria;
import org.tdmx.lib.control.domain.AccountZone;
import org.tdmx.lib.control.domain.AccountZoneAdministrationCredential;
import org.tdmx.lib.control.domain.AccountZoneAdministrationCredentialSearchCriteria;
import org.tdmx.lib.control.domain.AccountZoneAdministrationCredentialStatus;
import org.tdmx.lib.control.domain.AccountZoneSearchCriteria;
import org.tdmx.lib.control.domain.AccountZoneStatus;
import org.tdmx.lib.control.domain.ControlJob;
import org.tdmx.lib.control.domain.DatabasePartition;
import org.tdmx.lib.control.domain.DatabaseType;
import org.tdmx.lib.control.domain.DnsResolverGroup;
import org.tdmx.lib.control.domain.PartitionControlServer;
import org.tdmx.lib.control.domain.Segment;
import org.tdmx.lib.control.job.JobFactory;
import org.tdmx.lib.control.job.JobScheduler;
import org.tdmx.lib.control.service.AccountService;
import org.tdmx.lib.control.service.AccountZoneAdministrationCredentialService;
import org.tdmx.lib.control.service.AccountZoneService;
import org.tdmx.lib.control.service.DatabasePartitionService;
import org.tdmx.lib.control.service.DnsResolverGroupService;
import org.tdmx.lib.control.service.PartitionControlServerService;
import org.tdmx.lib.control.service.SegmentService;
import org.tdmx.lib.control.service.UniqueIdService;
import org.tdmx.lib.control.service.ZoneDatabasePartitionAllocationService;
import org.tdmx.server.rs.exception.ApplicationValidationError;
import org.tdmx.server.rs.exception.FieldValidationError;
import org.tdmx.server.rs.exception.FieldValidationError.FieldValidationErrorType;
import org.tdmx.server.rs.sas.resource.AccountResource;
import org.tdmx.server.rs.sas.resource.AccountZoneAdministrationCredentialResource;
import org.tdmx.server.rs.sas.resource.AccountZoneResource;
import org.tdmx.server.rs.sas.resource.DatabasePartitionResource;
import org.tdmx.server.rs.sas.resource.DnsResolverGroupResource;
import org.tdmx.server.rs.sas.resource.PartitionControlServerResource;
import org.tdmx.server.rs.sas.resource.SegmentResource;
import org.tdmx.service.control.task.dao.ZACInstallTask;
import org.tdmx.service.control.task.dao.ZoneInstallTask;

public class SASImpl implements SAS {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------
	public static enum PARAM {
		SID("sId"),
		SEGMENT("segment"),
		PARTITION("partition"),
		PARTITIONCONTROLSERVER("pcs"),
		DNSRESOLVERGROUP("dnsResolverGroup"),
		DBTYPE("dbType"),
		PID("pId"),
		AID("aId"),
		ACCOUNT("account"),
		ZID("zId"),
		ZCID("zcId"),
		DRGID("drgId"),
		PCSID("pcsId"),;

		private PARAM(String n) {
			this.n = n;
		}

		private final String n;

		@Override
		public String toString() {
			return this.n;
		}

	}

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------
	private static final Logger log = LoggerFactory.getLogger(SASImpl.class);

	private UniqueIdService accountIdService;
	private SegmentService segmentService;
	private DatabasePartitionService partitionService;
	private DnsResolverGroupService dnsResolverGroupService;
	private PartitionControlServerService partitionControlService;

	private AccountService accountService;
	private AccountZoneService accountZoneService;
	private AccountZoneAdministrationCredentialService accountZoneCredentialService;
	private ZoneDatabasePartitionAllocationService zonePartitionService;
	private JobFactory jobFactory;
	private JobScheduler jobScheduler;

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------

	@Override
	public DnsResolverGroupResource createDnsResolverGroup(DnsResolverGroupResource dnsResolverGroup) {
		validatePresent(PARAM.DNSRESOLVERGROUP, dnsResolverGroup);
		DnsResolverGroup g = DnsResolverGroupResource.mapTo(dnsResolverGroup);
		validateNotPresent(DnsResolverGroupResource.FIELD.ID, g.getId());
		validatePresent(DnsResolverGroupResource.FIELD.GROUPNAME, g.getGroupName());
		validateNotEmpty(DnsResolverGroupResource.FIELD.IPADDRESSLIST, g.getIpAddresses());

		DnsResolverGroup storedGroup = getDnsResolverGroupService().findByName(g.getGroupName());
		validateNotExists(DnsResolverGroupResource.FIELD.GROUPNAME, storedGroup);

		log.info("Creating DnsResolverGroup " + g.getGroupName());
		getDnsResolverGroupService().createOrUpdate(g);

		// the ID is only created on commit of the createOrUpdate above
		storedGroup = getDnsResolverGroupService().findByName(g.getGroupName());
		return DnsResolverGroupResource.mapTo(storedGroup);
	}

	@Override
	public List<DnsResolverGroupResource> searchDnsResolverGroup(Integer pageNo, Integer pageSize, String groupName) {
		// TODO search criteria
		List<DnsResolverGroup> groups = getDnsResolverGroupService().findAll();

		List<DnsResolverGroupResource> result = new ArrayList<>();
		for (DnsResolverGroup g : groups) {
			if (!StringUtils.hasText(groupName) || groupName.equals(g.getGroupName())) {
				result.add(DnsResolverGroupResource.mapTo(g));
			}
		}
		return result;
	}

	@Override
	public DnsResolverGroupResource getDnsResolverGroup(Long drgId) {
		validatePresent(PARAM.DRGID, drgId);
		return DnsResolverGroupResource.mapTo(getDnsResolverGroupService().findById(drgId));
	}

	@Override
	public DnsResolverGroupResource updateDnsResolverGroup(Long drgId, DnsResolverGroupResource dnsResolverGroup) {
		validatePresent(PARAM.DRGID, drgId);
		validateEquals(DnsResolverGroupResource.FIELD.ID, drgId, dnsResolverGroup.getId());

		DnsResolverGroup updatedGroup = DnsResolverGroupResource.mapTo(dnsResolverGroup);
		validatePresent(DnsResolverGroupResource.FIELD.ID, updatedGroup.getId());
		validatePresent(DnsResolverGroupResource.FIELD.GROUPNAME, updatedGroup.getGroupName());
		validateNotEmpty(DnsResolverGroupResource.FIELD.IPADDRESSLIST, updatedGroup.getIpAddresses());

		DnsResolverGroup storedGroup = getDnsResolverGroupService().findById(drgId);
		validateExists(DnsResolverGroupResource.FIELD.ID, storedGroup);
		validateEquals(DnsResolverGroupResource.FIELD.GROUPNAME, storedGroup.getGroupName(),
				dnsResolverGroup.getGroupName());

		getDnsResolverGroupService().createOrUpdate(updatedGroup);
		return DnsResolverGroupResource.mapTo(updatedGroup);
	}

	@Override
	public Response deleteDnsResolverGroup(Long drgId) {
		validatePresent(PARAM.DRGID, drgId);

		DnsResolverGroup group = getDnsResolverGroupService().findById(drgId);
		validateExists(DnsResolverGroupResource.FIELD.ID, group);

		getDnsResolverGroupService().delete(group);
		return Response.ok().build();
	}

	@Override
	public SegmentResource createSegment(SegmentResource segment) {
		validatePresent(PARAM.SEGMENT, segment);
		Segment s = SegmentResource.mapTo(segment);
		validateNotPresent(SegmentResource.FIELD.ID, s.getId());
		validatePresent(SegmentResource.FIELD.SEGMENT, s.getSegmentName());
		validatePresent(SegmentResource.FIELD.SCS_URL, s.getScsUrl());

		Segment storedSegment = getSegmentService().findBySegment(s.getSegmentName());
		validateNotExists(SegmentResource.FIELD.SEGMENT, storedSegment);

		log.info("Creating segment " + s.getSegmentName());
		getSegmentService().createOrUpdate(s);

		// the ID is only created on commit of the createOrUpdate above
		storedSegment = getSegmentService().findBySegment(s.getSegmentName());
		return SegmentResource.mapFrom(storedSegment);
	}

	@Override
	public List<SegmentResource> searchSegment(Integer pageNo, Integer pageSize, String segment) {
		// TODO search criteria
		List<Segment> segments = getSegmentService().findAll();

		List<SegmentResource> result = new ArrayList<>();
		for (Segment s : segments) {
			boolean match = true;
			if (StringUtils.hasText(segment) && !segment.equals(s.getSegmentName())) {
				match = false;
			}
			if (match) {
				result.add(SegmentResource.mapFrom(s));
			}
		}
		return result;
	}

	@Override
	public SegmentResource getSegment(Long sId) {
		validatePresent(PARAM.SID, sId);
		return SegmentResource.mapFrom(getSegmentService().findById(sId));
	}

	@Override
	public SegmentResource updateSegment(Long sId, SegmentResource segment) {
		validatePresent(PARAM.SEGMENT, segment);
		validateEquals(PARAM.SID, sId, segment.getId());

		Segment s = SegmentResource.mapTo(segment);
		validatePresent(SegmentResource.FIELD.ID, s.getId());
		validatePresent(SegmentResource.FIELD.SEGMENT, s.getSegmentName());
		validatePresent(SegmentResource.FIELD.SCS_URL, s.getScsUrl());

		Segment storedSegment = getSegmentService().findBySegment(s.getSegmentName());
		validateExists(SegmentResource.FIELD.SEGMENT, storedSegment);

		getSegmentService().createOrUpdate(s);
		return SegmentResource.mapFrom(s);
	}

	@Override
	public Response deleteSegment(Long sId) {
		validatePresent(PARAM.SID, sId);

		Segment segment = getSegmentService().findById(sId);
		validateExists(SegmentResource.FIELD.ID, segment);

		getSegmentService().delete(segment);
		return Response.ok().build();
	}

	@Override
	public PartitionControlServerResource createPartitionControlServer(PartitionControlServerResource pcs) {
		validatePresent(PARAM.PARTITIONCONTROLSERVER, pcs);
		PartitionControlServer p = PartitionControlServerResource.mapTo(pcs);
		validateNotPresent(PartitionControlServerResource.FIELD.ID, p.getId());
		validatePresent(PartitionControlServerResource.FIELD.SEGMENT, p.getSegment());
		validatePresent(PartitionControlServerResource.FIELD.IPADDRESS, p.getIpAddress());
		validatePresent(PartitionControlServerResource.FIELD.PORT, p.getPort());
		validatePresent(PartitionControlServerResource.FIELD.MODULO, p.getServerModulo());

		Segment storedSegment = getSegmentService().findBySegment(p.getSegment());
		validateExists(AccountZoneResource.FIELD.SEGMENT, storedSegment);

		PartitionControlServer storedServer = getPartitionControlService().findByIpEndpoint(p.getIpAddress(),
				p.getPort());
		validateNotExists(PartitionControlServerResource.FIELD.IPADDRESS, storedServer);

		log.info("Creating partition control server " + p.getIpAddress() + ":" + p.getPort());
		getPartitionControlService().createOrUpdate(p);

		// the ID is only created on commit of the createOrUpdate above
		storedServer = getPartitionControlService().findById(p.getId());
		return PartitionControlServerResource.mapFrom(storedServer);
	}

	@Override
	public List<PartitionControlServerResource> searchPartitionControlServer(Integer pageNo, Integer pageSize,
			String segment, Integer modulo, String ipaddress, Integer port) {
		List<PartitionControlServer> pcss = getPartitionControlService().findAll();

		List<PartitionControlServerResource> result = new ArrayList<>();
		for (PartitionControlServer p : pcss) {
			boolean match = true;
			if (StringUtils.hasText(segment) && !segment.equals(p.getSegment())) {
				match = false;
			}
			if (modulo != null && !modulo.equals(p.getServerModulo())) {
				match = false;
			}
			if (StringUtils.hasText(ipaddress) && !ipaddress.equals(p.getIpAddress())) {
				match = false;
			}
			if (port != null && !port.equals(p.getPort())) {
				match = false;
			}

			if (match) {
				result.add(PartitionControlServerResource.mapFrom(p));
			}
		}
		return result;
	}

	@Override
	public PartitionControlServerResource getPartitionControlServer(Long pcsId) {
		validatePresent(PARAM.PCSID, pcsId);

		PartitionControlServer storedServer = getPartitionControlService().findById(pcsId);
		return PartitionControlServerResource.mapFrom(storedServer);
	}

	@Override
	public PartitionControlServerResource updatePartitionControlServer(Long pcsId, PartitionControlServerResource pcs) {
		validatePresent(PARAM.PCSID, pcsId);
		validatePresent(PARAM.PARTITIONCONTROLSERVER, pcs);
		validateEquals(PARAM.PCSID, pcsId, pcs.getId());

		PartitionControlServer storedServer = getPartitionControlService().findById(pcsId);
		validateExists(PartitionControlServerResource.FIELD.ID, storedServer);

		PartitionControlServer p = PartitionControlServerResource.mapTo(pcs);
		validatePresent(PartitionControlServerResource.FIELD.ID, p.getId());
		validatePresent(PartitionControlServerResource.FIELD.SEGMENT, p.getSegment());
		validatePresent(PartitionControlServerResource.FIELD.IPADDRESS, p.getIpAddress());
		validatePresent(PartitionControlServerResource.FIELD.PORT, p.getPort());
		validatePresent(PartitionControlServerResource.FIELD.MODULO, p.getServerModulo());

		Segment storedSegment = getSegmentService().findBySegment(p.getSegment());
		validateExists(AccountZoneResource.FIELD.SEGMENT, storedSegment);
		if (!storedServer.getIpAddress().equals(p.getIpAddress()) || storedServer.getPort() != p.getPort()) {
			// change of IP endpoint still needs to be unique
			PartitionControlServer other = getPartitionControlService().findByIpEndpoint(p.getIpAddress(), p.getPort());
			validateNotExists(PartitionControlServerResource.FIELD.IPADDRESS, other);
		}
		log.info("Updating partition control server " + p.getIpAddress() + ":" + p.getPort());
		getPartitionControlService().createOrUpdate(p);

		return PartitionControlServerResource.mapFrom(p);
	}

	@Override
	public Response deletePartitionControlServer(Long pcsId) {
		validatePresent(PARAM.PCSID, pcsId);

		PartitionControlServer storedServer = getPartitionControlService().findById(pcsId);
		validateExists(PartitionControlServerResource.FIELD.ID, storedServer);

		getPartitionControlService().delete(storedServer);
		return Response.ok().build();
	}

	@Override
	public DatabasePartitionResource createDatabasePartition(DatabasePartitionResource partition) {
		validatePresent(PARAM.PARTITION, partition);
		DatabasePartition p = DatabasePartitionResource.mapTo(partition);
		validateNotPresent(DatabasePartitionResource.FIELD.ID, p.getId());
		validatePresent(DatabasePartitionResource.FIELD.PARTITION_ID, p.getPartitionId());
		validatePresent(DatabasePartitionResource.FIELD.SEGMENT, p.getSegment());
		validatePresent(DatabasePartitionResource.FIELD.DB_TYPE, p.getDbType());

		Segment storedSegment = getSegmentService().findBySegment(p.getSegment());
		validateExists(AccountZoneResource.FIELD.SEGMENT, storedSegment);

		DatabasePartition storedPartition = getPartitionService().findByPartitionId(p.getPartitionId());
		validateNotExists(DatabasePartitionResource.FIELD.PARTITION_ID, storedPartition);

		log.info("Creating database partition " + p.getPartitionId());
		getPartitionService().createOrUpdate(p);

		// the ID is only created on commit of the createOrUpdate above
		storedPartition = getPartitionService().findByPartitionId(p.getPartitionId());
		return DatabasePartitionResource.mapFrom(storedPartition);
	}

	@Override
	public List<DatabasePartitionResource> searchDatabasePartition(Integer pageNo, Integer pageSize, String partitionId,
			String dbType, String segment) {
		// TODO search criteria
		validateEnum(PARAM.DBTYPE, DatabaseType.class, dbType);
		DatabaseType databaseType = EnumUtils.mapTo(DatabaseType.class, dbType);
		if (!StringUtils.hasText(partitionId)) {
			validatePresent(PARAM.DBTYPE, databaseType);
		}

		List<DatabasePartition> partitions = null;
		if (StringUtils.hasText(partitionId)) {
			partitions = new ArrayList<>();
			DatabasePartition p = getPartitionService().findByPartitionId(partitionId);
			partitions.add(p);
		} else if (StringUtils.hasText(segment)) {
			partitions = getPartitionService().findByTypeAndSegment(databaseType, segment);
		} else {
			partitions = getPartitionService().findByType(databaseType);
		}

		List<DatabasePartitionResource> result = new ArrayList<>();
		for (DatabasePartition p : partitions) {
			result.add(DatabasePartitionResource.mapFrom(p));
		}
		return result;
	}

	@Override
	public DatabasePartitionResource getDatabasePartition(Long pId) {
		validatePresent(PARAM.PID, pId);

		DatabasePartition storedPartition = getPartitionService().findById(pId);
		return DatabasePartitionResource.mapFrom(storedPartition);
	}

	@Override
	public DatabasePartitionResource updateDatabasePartition(Long pId, DatabasePartitionResource partition) {
		validatePresent(PARAM.PARTITION, partition);
		validateEquals(PARAM.PID, pId, partition.getId());

		DatabasePartition p = DatabasePartitionResource.mapTo(partition);
		validatePresent(DatabasePartitionResource.FIELD.ID, p.getId());
		validatePresent(DatabasePartitionResource.FIELD.PARTITION_ID, p.getPartitionId());
		validatePresent(DatabasePartitionResource.FIELD.SEGMENT, p.getSegment());
		validatePresent(DatabasePartitionResource.FIELD.DB_TYPE, p.getDbType());

		DatabasePartition storedPartition = getPartitionService().findByPartitionId(p.getPartitionId());
		validateExists(DatabasePartitionResource.FIELD.PARTITION_ID, storedPartition);

		// immutable SEGMENT, DB_TYPE
		validateEquals(DatabasePartitionResource.FIELD.SEGMENT, storedPartition.getSegment(), p.getSegment());
		validateEquals(DatabasePartitionResource.FIELD.DB_TYPE, storedPartition.getDbType(), p.getDbType());

		// immutable after active + ActiveTS
		if (storedPartition.getActivationTimestamp() != null) {
			validateEquals(DatabasePartitionResource.FIELD.ACTIVATION_TS, storedPartition.getActivationTimestamp(),
					p.getActivationTimestamp());
			validatePresent(DatabasePartitionResource.FIELD.SIZEFACTOR, storedPartition.getDbType());
			validateEquals(DatabasePartitionResource.FIELD.SIZEFACTOR, storedPartition.getSizeFactor(),
					p.getSizeFactor());
			validatePresent(DatabasePartitionResource.FIELD.URL, storedPartition.getUrl());
			validatePresent(DatabasePartitionResource.FIELD.USERNAME, storedPartition.getUsername());
			validatePresent(DatabasePartitionResource.FIELD.PASSWORD, storedPartition.getPassword());
		}

		getPartitionService().createOrUpdate(p);

		// TODO #88 cache invalidation partitions

		return DatabasePartitionResource.mapFrom(p);
	}

	@Override
	public Response deleteDatabasePartition(Long pId) {
		validatePresent(PARAM.PID, pId);

		DatabasePartition storedPartition = getPartitionService().findById(pId);
		validateExists(DatabasePartitionResource.FIELD.ID, storedPartition);

		getPartitionService().delete(storedPartition);
		return Response.ok().build();
	}

	@Override
	public AccountResource createAccount(AccountResource account) {
		validatePresent(PARAM.ACCOUNT, account);
		Account a = AccountResource.mapTo(account);
		validateNotPresent(AccountResource.FIELD.ID, a.getId());
		validateNotPresent(AccountResource.FIELD.ACCOUNTID, a.getAccountId());

		a.setAccountId(getAccountIdService().getNextId());

		log.info("Creating accountId " + a.getAccountId() + " for " + a.getEmail());
		getAccountService().createOrUpdate(a);

		// the ID is only created on commit of the createOrUpdate above
		Account storedAccount = getAccountService().findByAccountId(a.getAccountId());
		return AccountResource.mapFrom(storedAccount);
	}

	@Override
	public List<AccountResource> searchAccount(Integer pageNo, Integer pageSize, String email, String accountId) {
		AccountSearchCriteria sc = new AccountSearchCriteria(getPageSpecifier(pageNo, pageSize));
		sc.setEmail(email);
		sc.setAccountId(accountId);
		List<Account> accounts = getAccountService().search(sc);

		List<AccountResource> result = new ArrayList<>();
		for (Account a : accounts) {
			result.add(AccountResource.mapFrom(a));
		}
		return result;
	}

	@Override
	public AccountResource getAccount(Long aId) {
		validatePresent(PARAM.AID, aId);
		return AccountResource.mapFrom(getAccountService().findById(aId));
	}

	@Override
	public AccountResource updateAccount(Long aId, AccountResource account) {
		validatePresent(PARAM.ACCOUNT, account);
		validateEquals(PARAM.AID, aId, account.getId());

		Account a = AccountResource.mapTo(account);
		validatePresent(AccountResource.FIELD.ID, a.getId());
		validatePresent(AccountResource.FIELD.ACCOUNTID, a.getAccountId());
		getAccountService().createOrUpdate(a);
		return AccountResource.mapFrom(a);
	}

	@Override
	public Response deleteAccount(Long aId) {
		validatePresent(PARAM.AID, aId);
		Account a = getAccountService().findById(aId);
		getAccountService().delete(a);
		return Response.ok().build();
	}

	@Override
	public AccountZoneResource createAccountZone(Long aId, AccountZoneResource accountZone) {
		validatePresent(PARAM.AID, aId);
		validateEnum(AccountZoneResource.FIELD.ACCESSSTATUS, AccountZoneStatus.class, accountZone.getAccessStatus());

		AccountZone az = AccountZoneResource.mapTo(accountZone);

		// check that the account exists and accountId same
		Account a = getAccountService().findById(aId);
		validatePresent(PARAM.ACCOUNT, a);
		validateEquals(AccountZoneResource.FIELD.ACCOUNTID, a.getAccountId(), az.getAccountId());

		validatePresent(AccountZoneResource.FIELD.ZONEAPEX, az.getZoneApex());
		validatePresent(AccountZoneResource.FIELD.ACCESSSTATUS, az.getStatus());
		validateNotPresent(AccountZoneResource.FIELD.ID, az.getId());

		validatePresent(AccountZoneResource.FIELD.SEGMENT, az.getSegment());
		Segment storedSegment = getSegmentService().findBySegment(az.getSegment());
		validateExists(AccountZoneResource.FIELD.SEGMENT, storedSegment);

		validateNotPresent(AccountZoneResource.FIELD.ZONEPARTITIONID, az.getZonePartitionId());

		String partitionId = getZonePartitionService().getZonePartitionId(a.getAccountId(), az.getZoneApex(),
				az.getSegment());
		az.setZonePartitionId(partitionId);
		validatePresent(AccountZoneResource.FIELD.ZONEPARTITIONID, az.getZonePartitionId());

		log.info("Creating AccountZone " + az.getZoneApex() + " in ZoneDB partition " + az.getZonePartitionId());
		getAccountZoneService().createOrUpdate(az);

		// schedule the job to install the Zone in the ZoneDB partition.
		ZoneInstallTask installTask = new ZoneInstallTask();
		installTask.setAccountId(az.getAccountId());
		installTask.setZoneApex(az.getZoneApex());
		Job installJob = getJobFactory().createJob(installTask);
		ControlJob j = getJobScheduler().scheduleImmediate(installJob);

		az.setJobId(j.getId());

		getAccountZoneService().createOrUpdate(az);
		return AccountZoneResource.mapFrom(az);
	}

	@Override
	public List<AccountZoneResource> searchAccountZone(Long aId, Integer pageNo, Integer pageSize, String zoneApex) {
		validatePresent(PARAM.AID, aId);
		AccountZoneSearchCriteria sc = new AccountZoneSearchCriteria(getPageSpecifier(pageNo, pageSize));
		sc.setZoneApex(zoneApex);
		List<AccountZone> accountzones = getAccountZoneService().search(sc);

		List<AccountZoneResource> result = new ArrayList<>();
		for (AccountZone az : accountzones) {
			result.add(AccountZoneResource.mapFrom(az));
		}
		return result;
	}

	@Override
	public List<AccountZoneResource> searchAccountZone(Integer pageNo, Integer pageSize, String accountId,
			String zoneApex, String segment, String zonePartitionId, String status) {
		AccountZoneSearchCriteria sc = new AccountZoneSearchCriteria(getPageSpecifier(pageNo, pageSize));
		sc.setAccountId(accountId);
		sc.setZoneApex(zoneApex);
		sc.setSegment(segment);
		sc.setZonePartitionId(zonePartitionId);
		if (StringUtils.hasText(status)) {
			sc.setStatus(AccountZoneStatus.valueOf(status));
		}

		List<AccountZone> accountzones = getAccountZoneService().search(sc);

		List<AccountZoneResource> result = new ArrayList<>();
		for (AccountZone az : accountzones) {
			result.add(AccountZoneResource.mapFrom(az));
		}
		return result;
	}

	@Override
	public AccountZoneResource getAccountZone(Long aId, Long zId) {
		validatePresent(PARAM.AID, aId);
		validatePresent(PARAM.ZID, zId);

		Account a = getAccountService().findById(aId);
		if (a != null) {
			AccountZone az = getAccountZoneService().findById(zId);
			if (az != null && a.getAccountId().equals(az.getAccountId())) {
				return AccountZoneResource.mapFrom(az);
			}
		}
		return null;
	}

	@Override
	public AccountZoneResource updateAccountZone(Long aId, Long zId, AccountZoneResource accountZone) {
		validatePresent(PARAM.AID, aId);
		validatePresent(PARAM.ZID, zId);
		validateEquals(PARAM.ZID, zId, accountZone.getId());
		validateEnum(AccountZoneResource.FIELD.ACCESSSTATUS, AccountZoneStatus.class, accountZone.getAccessStatus());

		Account a = getAccountService().findById(aId);
		validateExists(PARAM.AID, a);
		AccountZone az = getAccountZoneService().findById(zId);
		validateExists(PARAM.ZID, az);
		// the accountzone must belong to the account
		validateEquals(AccountZoneResource.FIELD.ACCOUNTID, a.getAccountId(), az.getAccountId());

		AccountZone updatedAz = AccountZoneResource.mapTo(accountZone);

		// some things cannot change
		validateEquals(AccountZoneResource.FIELD.ID, az.getId(), updatedAz.getId());
		validateEquals(AccountZoneResource.FIELD.ACCOUNTID, az.getAccountId(), updatedAz.getAccountId());
		validateEquals(AccountZoneResource.FIELD.ZONEAPEX, az.getZoneApex(), updatedAz.getZoneApex());
		validateEquals(AccountZoneResource.FIELD.JOBID, az.getJobId(), updatedAz.getJobId());
		validateNotPresent(AccountZoneResource.FIELD.JOBID, updatedAz.getJobId());

		// TODO change segment - job
		// TODO change partitionId - store jobId

		return null;
	}

	@Override
	public Response deleteAccountZone(Long aId, Long zId) {
		validatePresent(PARAM.AID, aId);
		validatePresent(PARAM.ZID, zId);
		Account a = getAccountService().findById(aId);
		validateExists(PARAM.AID, a);
		AccountZone az = getAccountZoneService().findById(zId);
		validateExists(PARAM.ZID, az);

		// the accountZone must actually relate to the account!
		validateEquals(AccountZoneResource.FIELD.ACCOUNTID, a.getAccountId(), az.getAccountId());

		getAccountZoneService().delete(az);

		return Response.ok().build();
	}

	@Override
	public AccountZoneAdministrationCredentialResource createAccountZoneAdministrationCredential(Long aId, Long zId,
			AccountZoneAdministrationCredentialResource zac) {
		validatePresent(PARAM.AID, aId);
		validatePresent(PARAM.ZID, zId);

		Account a = getAccountService().findById(aId);
		validateExists(PARAM.AID, a);
		AccountZone az = getAccountZoneService().findById(zId);
		validateExists(PARAM.ZID, az);
		validateEquals(AccountZoneResource.FIELD.ACCOUNTID, a.getAccountId(), az.getAccountId());

		validateEquals(AccountZoneAdministrationCredentialResource.FIELD.ACCOUNTID, a.getAccountId(),
				zac.getAccountId());
		validateEquals(AccountZoneAdministrationCredentialResource.FIELD.ZONEAPEX, az.getZoneApex(), zac.getZoneApex());

		// field validation
		validateNotPresent(AccountZoneAdministrationCredentialResource.FIELD.ID, zac.getId());
		validatePresent(AccountZoneAdministrationCredentialResource.FIELD.CERTIFICATEPEM, zac.getZoneApex());
		validateNotPresent(AccountZoneAdministrationCredentialResource.FIELD.FINGERPRINT, zac.getFingerprint());
		validateNotPresent(AccountZoneAdministrationCredentialResource.FIELD.STATUS, zac.getStatus());

		// we cannot trust what is sent is correct, so we double check the fingerprint
		AccountZoneAdministrationCredential controlCredential = new AccountZoneAdministrationCredential(
				a.getAccountId(), zac.getCertificatePem());
		AccountZoneAdministrationCredential azc = AccountZoneAdministrationCredentialResource.mapTo(zac);
		validateEquals(AccountZoneAdministrationCredentialResource.FIELD.ZONEAPEX, controlCredential.getZoneApex(),
				azc.getZoneApex());
		azc.setFingerprint(controlCredential.getFingerprint());
		azc.setCredentialStatus(controlCredential.getCredentialStatus());

		log.info("Creating AccountZoneAdministrationCredential " + zac);
		getAccountZoneCredentialService().createOrUpdate(azc);

		if (AccountZoneAdministrationCredentialStatus.PENDING_INSTALLATION == azc.getCredentialStatus()) {

			// schedule the job to check DNS and install the ZoneAdministrationCredential in the ZoneDB partition.
			ZACInstallTask installTask = new ZACInstallTask();
			installTask.setAccountId(az.getAccountId());
			installTask.setZoneApex(az.getZoneApex());
			installTask.setFingerprint(azc.getFingerprint());
			Job installJob = getJobFactory().createJob(installTask);
			ControlJob j = getJobScheduler().scheduleImmediate(installJob);

			azc.setJobId(j.getId());
			getAccountZoneCredentialService().createOrUpdate(azc);

		}
		return AccountZoneAdministrationCredentialResource.mapFrom(azc);
	}

	@Override
	public List<AccountZoneAdministrationCredentialResource> searchAccountZoneAdministrationCredential(Integer pageNo,
			Integer pageSize, Long aId, Long zId) {
		validatePresent(PARAM.AID, aId);
		validatePresent(PARAM.ZID, zId);

		Account a = getAccountService().findById(aId);
		validateExists(PARAM.AID, a);
		AccountZone az = getAccountZoneService().findById(zId);
		validateExists(PARAM.ZID, az);
		validateEquals(AccountZoneResource.FIELD.ACCOUNTID, a.getAccountId(), az.getAccountId());

		// we list all ZACs of the account zone.
		AccountZoneAdministrationCredentialSearchCriteria sc = new AccountZoneAdministrationCredentialSearchCriteria(
				getPageSpecifier(pageNo, pageSize));
		sc.setAccountId(az.getAccountId());
		sc.setZoneApex(az.getZoneApex());
		List<AccountZoneAdministrationCredential> accountzones = getAccountZoneCredentialService().search(sc);

		List<AccountZoneAdministrationCredentialResource> result = new ArrayList<>();
		for (AccountZoneAdministrationCredential azc : accountzones) {
			result.add(AccountZoneAdministrationCredentialResource.mapFrom(azc));
		}
		return result;
	}

	@Override
	public List<AccountZoneAdministrationCredentialResource> searchAccountZoneAdministrationCredential(Integer pageNo,
			Integer pageSize, String zoneApex, String accountId, String fingerprint, String status) {
		// we list all ZACs, or restrict to those of an account if the accountId is set, or zone if the zone parameter
		// is provided
		AccountZoneAdministrationCredentialSearchCriteria sc = new AccountZoneAdministrationCredentialSearchCriteria(
				getPageSpecifier(pageNo, pageSize));
		sc.setAccountId(accountId);
		sc.setZoneApex(zoneApex);
		sc.setFingerprint(fingerprint);
		if (StringUtils.hasText(status)) {
			sc.setStatus(AccountZoneAdministrationCredentialStatus.valueOf(status));
		}
		List<AccountZoneAdministrationCredential> accountzones = getAccountZoneCredentialService().search(sc);

		List<AccountZoneAdministrationCredentialResource> result = new ArrayList<>();
		for (AccountZoneAdministrationCredential azc : accountzones) {
			result.add(AccountZoneAdministrationCredentialResource.mapFrom(azc));
		}
		return result;
	}

	@Override
	public AccountZoneAdministrationCredentialResource getAccountZoneAdministrationCredential(Long aId, Long zId,
			Long zcId) {
		validatePresent(PARAM.AID, aId);
		validatePresent(PARAM.ZID, zId);
		validatePresent(PARAM.ZCID, zcId);

		Account a = getAccountService().findById(aId);
		validateExists(PARAM.AID, a);
		AccountZone az = getAccountZoneService().findById(zId);
		validateExists(PARAM.ZID, az);
		validateEquals(AccountZoneResource.FIELD.ACCOUNTID, a.getAccountId(), az.getAccountId());
		AccountZoneAdministrationCredential azc = getAccountZoneCredentialService().findById(zcId);
		validateExists(PARAM.ZCID, azc);
		validateEquals(AccountZoneAdministrationCredentialResource.FIELD.ACCOUNTID, a.getAccountId(),
				azc.getAccountId());

		return AccountZoneAdministrationCredentialResource.mapFrom(azc);
	}

	@Override
	public AccountZoneAdministrationCredentialResource updateAccountZoneAdministrationCredential(Long aId, Long zId,
			Long zcId, AccountZoneAdministrationCredentialResource zac) {
		validatePresent(PARAM.AID, aId);
		validatePresent(PARAM.ZID, zId);
		validatePresent(PARAM.ZCID, zcId);
		validateEnum(AccountZoneAdministrationCredentialResource.FIELD.STATUS,
				AccountZoneAdministrationCredentialStatus.class, zac.getStatus());

		Account a = getAccountService().findById(aId);
		validateExists(PARAM.AID, a);
		AccountZone az = getAccountZoneService().findById(zId);
		validateExists(PARAM.ZID, az);
		validateEquals(AccountZoneResource.FIELD.ACCOUNTID, a.getAccountId(), az.getAccountId());
		AccountZoneAdministrationCredential azc = getAccountZoneCredentialService().findById(zcId);
		validateExists(PARAM.ZCID, azc);
		validateEquals(AccountZoneAdministrationCredentialResource.FIELD.ACCOUNTID, a.getAccountId(),
				azc.getAccountId());

		// we don't support changing the certificate, but we support deinstallation or re-installation (maybe DNS fixed)
		AccountZoneAdministrationCredential updatedAzc = AccountZoneAdministrationCredentialResource.mapTo(zac);
		validateEquals(AccountZoneAdministrationCredentialResource.FIELD.ZONEAPEX, azc.getZoneApex(),
				updatedAzc.getZoneApex());
		validateEquals(AccountZoneAdministrationCredentialResource.FIELD.CERTIFICATEPEM, azc.getCertificateChainPem(),
				updatedAzc.getCertificateChainPem());
		validateEquals(AccountZoneAdministrationCredentialResource.FIELD.FINGERPRINT, azc.getFingerprint(),
				updatedAzc.getFingerprint());
		validateEquals(AccountZoneAdministrationCredentialResource.FIELD.JOBID, azc.getJobId(), updatedAzc.getJobId());
		validateNotPresent(AccountZoneAdministrationCredentialResource.FIELD.JOBID, updatedAzc.getJobId());

		if (AccountZoneAdministrationCredentialStatus.DEINSTALLED == updatedAzc.getCredentialStatus()) {
			if (AccountZoneAdministrationCredentialStatus.INSTALLED == azc.getCredentialStatus()) {
				// TODO #89 ZACRemoveTask and schedule job.
				updatedAzc.setCredentialStatus(AccountZoneAdministrationCredentialStatus.PENDING_DEINSTALLATION);

			} else {
				// if not already installed, then we can immediately remove.
				updatedAzc.setCredentialStatus(AccountZoneAdministrationCredentialStatus.DEINSTALLED);
			}
			getAccountZoneCredentialService().createOrUpdate(updatedAzc);
		} else if (AccountZoneAdministrationCredentialStatus.PENDING_INSTALLATION == updatedAzc.getCredentialStatus()) {
			validateEquals(AccountZoneAdministrationCredentialResource.FIELD.STATUS,
					AccountZoneAdministrationCredentialStatus.NO_DNS_TRUST, updatedAzc.getCredentialStatus());
			// schedule the job to re-check DNS and install the ZoneAdministrationCredential in the ZoneDB partition.
			ZACInstallTask installTask = new ZACInstallTask();
			installTask.setAccountId(az.getAccountId());
			installTask.setZoneApex(az.getZoneApex());
			installTask.setFingerprint(azc.getFingerprint());
			Job installJob = getJobFactory().createJob(installTask);
			ControlJob j = getJobScheduler().scheduleImmediate(installJob);

			updatedAzc.setJobId(j.getId());
			getAccountZoneCredentialService().createOrUpdate(updatedAzc);

		}
		return AccountZoneAdministrationCredentialResource.mapFrom(updatedAzc);
	}

	@Override
	public Response deleteAccountZoneAdministrationCredential(Long aId, Long zId, Long zcId) {
		validatePresent(PARAM.AID, aId);
		validatePresent(PARAM.ZID, zId);
		validatePresent(PARAM.ZCID, zcId);

		Account a = getAccountService().findById(aId);
		validateExists(PARAM.AID, a);
		AccountZone az = getAccountZoneService().findById(zId);
		validateExists(PARAM.ZID, az);
		validateEquals(AccountZoneResource.FIELD.ACCOUNTID, a.getAccountId(), az.getAccountId());
		AccountZoneAdministrationCredential azc = getAccountZoneCredentialService().findById(zcId);
		validateExists(PARAM.ZCID, azc);
		validateEquals(AccountZoneAdministrationCredentialResource.FIELD.ACCOUNTID, a.getAccountId(),
				azc.getAccountId());
		validateEquals(AccountZoneAdministrationCredentialResource.FIELD.STATUS,
				AccountZoneAdministrationCredentialStatus.DEINSTALLED, azc.getCredentialStatus());

		getAccountZoneCredentialService().delete(azc);
		return Response.ok().build();
	}

	// -------------------------------------------------------------------------
	// PROTECTED METHODS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PRIVATE METHODS
	// -------------------------------------------------------------------------

	private PageSpecifier getPageSpecifier(Integer pageNo, Integer pageSize) {
		int pageNumber = pageNo != null ? pageNo : 0;
		int pageSz = pageSize != null ? pageSize : 10;
		return new PageSpecifier(pageNumber, pageSz);

	}

	private ValidationException createVE(FieldValidationErrorType type, String fieldName) {
		List<ApplicationValidationError> errors = new ArrayList<>();
		errors.add(new FieldValidationError(type, fieldName));
		ValidationException ve = new javax.ws.rs.ValidationException(Status.BAD_REQUEST, errors);
		return ve;
	}

	private void validateEquals(Enum<?> fieldId, String expectedValue, String fieldValue) {
		if (StringUtils.hasText(expectedValue) && StringUtils.hasText(fieldValue)) {
			if (!expectedValue.equals(fieldValue)) {
				throw createVE(FieldValidationErrorType.IMMUTABLE, fieldId.toString());
			}
		} else if (StringUtils.hasText(expectedValue)) {
			throw createVE(FieldValidationErrorType.MISSING, fieldId.toString());
		}
	}

	private void validateInSet(Enum<?> fieldId, Object[] expectedValues, Object fieldValue) {
		if (fieldValue == null) {
			throw createVE(FieldValidationErrorType.MISSING, fieldId.toString());
		}
		for (Object expectedValue : expectedValues) {
			if (expectedValue.equals(fieldValue)) {
				return;
			}
		}
		throw createVE(FieldValidationErrorType.INVALID, fieldId.toString());
	}

	private void validateEquals(Enum<?> fieldId, Object expectedValue, Object fieldValue) {
		if (expectedValue != null && fieldValue != null) {
			if (!expectedValue.equals(fieldValue)) {
				throw createVE(FieldValidationErrorType.IMMUTABLE, fieldId.toString());
			}
		} else if (expectedValue != null) {
			throw createVE(FieldValidationErrorType.MISSING, fieldId.toString());
		}
	}

	private void validateNotPresent(Enum<?> fieldId, String fieldValue) {
		if (StringUtils.hasText(fieldValue)) {
			throw createVE(FieldValidationErrorType.PRESENT, fieldId.toString());
		}
	}

	private void validateNotPresent(Enum<?> fieldId, Object fieldValue) {
		if (fieldValue != null) {
			throw createVE(FieldValidationErrorType.PRESENT, fieldId.toString());
		}
	}

	private void validateNotExists(Enum<?> fieldId, Object object) {
		if (object != null) {
			throw createVE(FieldValidationErrorType.EXISTS, fieldId.toString());
		}
	}

	private void validateExists(Enum<?> fieldId, Object object) {
		if (object == null) {
			throw createVE(FieldValidationErrorType.NOT_EXISTS, fieldId.toString());
		}
	}

	private void validateNotEmpty(Enum<?> fieldId, Collection<?> fieldValue) {
		if (fieldValue == null || fieldValue.isEmpty()) {
			throw createVE(FieldValidationErrorType.MISSING, fieldId.toString());
		}
	}

	private void validatePresent(Enum<?> fieldId, Object fieldValue) {
		if (fieldValue == null) {
			throw createVE(FieldValidationErrorType.MISSING, fieldId.toString());
		}
	}

	private void validatePresent(Enum<?> fieldId, String fieldValue) {
		if (!StringUtils.hasText(fieldValue)) {
			throw createVE(FieldValidationErrorType.MISSING, fieldId.toString());
		}
	}

	private <E extends Enum<E>> void validateEnum(Enum<?> fieldId, Class<E> enumClass, String fieldValue) {
		if (StringUtils.hasText(fieldValue)) {
			if (EnumUtils.mapTo(enumClass, fieldValue) == null) {
				throw createVE(FieldValidationErrorType.INVALID, fieldId.toString());
			}
		}
	}

	// -------------------------------------------------------------------------
	// PUBLIC ACCESSORS (GETTERS / SETTERS)
	// -------------------------------------------------------------------------

	public UniqueIdService getAccountIdService() {
		return accountIdService;
	}

	public void setAccountIdService(UniqueIdService accountIdService) {
		this.accountIdService = accountIdService;
	}

	public DnsResolverGroupService getDnsResolverGroupService() {
		return dnsResolverGroupService;
	}

	public void setDnsResolverGroupService(DnsResolverGroupService dnsResolverGroupService) {
		this.dnsResolverGroupService = dnsResolverGroupService;
	}

	public SegmentService getSegmentService() {
		return segmentService;
	}

	public void setSegmentService(SegmentService segmentService) {
		this.segmentService = segmentService;
	}

	public PartitionControlServerService getPartitionControlService() {
		return partitionControlService;
	}

	public void setPartitionControlService(PartitionControlServerService partitionControlService) {
		this.partitionControlService = partitionControlService;
	}

	public DatabasePartitionService getPartitionService() {
		return partitionService;
	}

	public void setPartitionService(DatabasePartitionService partitionService) {
		this.partitionService = partitionService;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public ZoneDatabasePartitionAllocationService getZonePartitionService() {
		return zonePartitionService;
	}

	public void setZonePartitionService(ZoneDatabasePartitionAllocationService zonePartitionService) {
		this.zonePartitionService = zonePartitionService;
	}

	public AccountZoneService getAccountZoneService() {
		return accountZoneService;
	}

	public void setAccountZoneService(AccountZoneService accountZoneService) {
		this.accountZoneService = accountZoneService;
	}

	public AccountZoneAdministrationCredentialService getAccountZoneCredentialService() {
		return accountZoneCredentialService;
	}

	public void setAccountZoneCredentialService(
			AccountZoneAdministrationCredentialService accountZoneCredentialService) {
		this.accountZoneCredentialService = accountZoneCredentialService;
	}

	public JobFactory getJobFactory() {
		return jobFactory;
	}

	public void setJobFactory(JobFactory jobFactory) {
		this.jobFactory = jobFactory;
	}

	public JobScheduler getJobScheduler() {
		return jobScheduler;
	}

	public void setJobScheduler(JobScheduler jobScheduler) {
		this.jobScheduler = jobScheduler;
	}

}
