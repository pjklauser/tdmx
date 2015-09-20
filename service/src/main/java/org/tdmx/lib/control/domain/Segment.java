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
package org.tdmx.lib.control.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * A descriptor for a Segment of a ServiceProvider.
 * 
 * @author Peter Klauser
 * 
 */
@Entity
@Table(name = "Segment")
public class Segment implements Serializable {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------

	public static final int MAX_HOSTNAME_LEN = 255;

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = -988419614813872556L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SegmentIdGen")
	@TableGenerator(name = "SegmentIdGen", table = "PrimaryKeyGen", pkColumnName = "NAME", pkColumnValue = "segmentObjectId", valueColumnName = "value", allocationSize = 10)
	private Long id;

	@Column(length = AccountZone.MAX_SEGMENT_LEN, nullable = false)
	private String segmentName;

	@Column(length = MAX_HOSTNAME_LEN)
	private String scsHostname;

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Segment [id=");
		builder.append(id);
		builder.append(", segment=");
		builder.append(segmentName);
		builder.append(", scsHostname=");
		builder.append(scsHostname);
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSegmentName() {
		return segmentName;
	}

	public void setSegmentName(String segment) {
		this.segmentName = segment;
	}

	public String getScsHostname() {
		return scsHostname;
	}

	public void setScsHostname(String scsHostname) {
		this.scsHostname = scsHostname;
	}

}