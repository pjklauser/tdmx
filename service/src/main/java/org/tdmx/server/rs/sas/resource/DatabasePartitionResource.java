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
package org.tdmx.server.rs.sas.resource;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.tdmx.core.cli.display.annotation.CliAttribute;
import org.tdmx.core.cli.display.annotation.CliRepresentation;
import org.tdmx.core.system.lang.EnumUtils;
import org.tdmx.lib.control.domain.DatabasePartition;
import org.tdmx.lib.control.domain.DatabaseType;

@CliRepresentation(name = "DatabasePartition")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "partition")
@XmlType(name = "DatabasePartition")
public class DatabasePartitionResource {

	public enum FIELD {
		ID("id"),
		PARTITION_ID("partitionId"),
		DB_TYPE("dbType"),
		SEGMENT("segment"),
		URL("url"),
		USERNAME("username"),
		PASSWORD("password"),
		SIZEFACTOR("sizeFactor"),
		ACTIVATION_TS("activationTS"),
		DEACTIVATION_TS("deactivationTS"),;

		private final String n;

		private FIELD(String n) {
			this.n = n;
		}

		@Override
		public String toString() {
			return this.n;
		}
	}

	@CliAttribute(order = 0, verbose = true)
	private Long id;
	@CliAttribute(order = 1)
	private String partitionId;
	@CliAttribute(order = 2)
	private String dbType;
	@CliAttribute(order = 3)
	private String segment;
	@CliAttribute(order = 4)
	private String url;
	@CliAttribute(order = 5)
	private String username;
	@CliAttribute(order = 6)
	private String password;
	@CliAttribute(order = 7)
	private int sizeFactor;
	@CliAttribute(order = 8)
	private Date activationTimestamp;
	@CliAttribute(order = 9)
	private Date deactivationTimestamp;

	public static DatabasePartition mapTo(DatabasePartitionResource partition) {
		if (partition == null) {
			return null;
		}
		DatabasePartition p = new DatabasePartition();
		p.setId(partition.getId());
		p.setPartitionId(partition.getPartitionId());
		p.setDbType(EnumUtils.mapTo(DatabaseType.class, partition.getDbType()));
		p.setSegment(partition.getSegment());
		p.setUrl(partition.getUrl());
		p.setUsername(partition.getUsername());
		p.setPassword(partition.getPassword());
		p.setSizeFactor(partition.getSizeFactor());
		p.setActivationTimestamp(partition.getActivationTimestamp());
		p.setDeactivationTimestamp(partition.getDeactivationTimestamp());

		return p;
	}

	public static DatabasePartitionResource mapFrom(DatabasePartition partition) {
		if (partition == null) {
			return null;
		}
		DatabasePartitionResource p = new DatabasePartitionResource();
		p.setId(partition.getId());
		p.setPartitionId(partition.getPartitionId());
		p.setDbType(EnumUtils.mapToString(partition.getDbType()));
		p.setSegment(partition.getSegment());
		p.setUrl(partition.getUrl());
		p.setUsername(partition.getUsername());
		p.setPassword(partition.getPassword());
		p.setSizeFactor(partition.getSizeFactor());
		p.setActivationTimestamp(partition.getActivationTimestamp());
		p.setDeactivationTimestamp(partition.getDeactivationTimestamp());

		return p;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(String partitionId) {
		this.partitionId = partitionId;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
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

	public int getSizeFactor() {
		return sizeFactor;
	}

	public void setSizeFactor(int sizeFactor) {
		this.sizeFactor = sizeFactor;
	}

	public Date getActivationTimestamp() {
		return activationTimestamp;
	}

	public void setActivationTimestamp(Date activationTimestamp) {
		this.activationTimestamp = activationTimestamp;
	}

	public Date getDeactivationTimestamp() {
		return deactivationTimestamp;
	}

	public void setDeactivationTimestamp(Date deactivationTimestamp) {
		this.deactivationTimestamp = deactivationTimestamp;
	}

}
