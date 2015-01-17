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
package org.tdmx.lib.zone.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * An Service is only unique within a domain.
 * 
 * @author Peter Klauser
 * 
 */
@Embeddable
public class ServiceID implements Serializable {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------
	public static final int MAX_NAME_LEN = 255;

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = -128859602084626282L;

	@Column(length = Zone.MAX_NAME_LEN, nullable = false)
	private String zoneApex;

	@Column(length = DomainID.MAX_NAME_LEN, nullable = false)
	/**
	 * The fully qualified domain name ( includes the zoneApex ).
	 */
	private String domainName;

	@Column(length = MAX_NAME_LEN, nullable = false)
	/**
	 * The serviceName part.
	 */
	private String serviceName;

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	public ServiceID() {
	}

	public ServiceID(String serviceName, String domainName, String zoneApex) {
		this.serviceName = serviceName;
		this.zoneApex = zoneApex;
		this.domainName = domainName;
	}

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------

	@Override
	public int hashCode() {
		return Objects.hash(zoneApex, domainName, serviceName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ServiceID) {
			ServiceID other = (ServiceID) obj;
			return Objects.equals(zoneApex, other.getZoneApex()) && Objects.equals(domainName, other.getDomainName())
					&& Objects.equals(serviceName, other.getServiceName());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServiceID [zoneApex=");
		builder.append(zoneApex);
		builder.append(", domainName=");
		builder.append(domainName);
		builder.append(", serviceName=");
		builder.append(serviceName);
		builder.append("]");
		return builder.toString();
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
	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getZoneApex() {
		return zoneApex;
	}

	public void setZoneApex(String zoneApex) {
		this.zoneApex = zoneApex;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}