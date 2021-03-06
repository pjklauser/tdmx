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
package org.tdmx.lib.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.tdmx.core.system.lang.StringUtils;

/**
 * An ProcessingState describes the current status of processing related to the enclosing entity.
 * 
 * @author Peter Klauser
 * 
 */
@Embeddable
public class ProcessingState implements Serializable {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------
	public static final int MAX_ERRORMESSAGE_LEN = 2048;

	public static final int FAILURE_RELAY_INITIATION = 501;
	public static final int FAILURE_RELAY_RETRY = 502;

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = -128859602084626282L;

	@Enumerated(EnumType.STRING)
	@Column
	private ProcessingStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date timestamp; // the time since we've been in this status

	@Column
	private Integer errorCode;

	@Column
	private String errorMessage;

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	private ProcessingState() {
		this(ProcessingStatus.NONE);
	}

	private ProcessingState(ProcessingStatus currentStatus) {
		status = currentStatus;
		timestamp = new Date();
	}

	public static ProcessingState error(int errorCode, String errorMsg) {
		ProcessingState e = new ProcessingState(ProcessingStatus.FAILURE);
		e.setErrorCode(errorCode);
		e.setErrorMessage(errorMsg);
		return e;
	}

	public static ProcessingState newProcessingState(ProcessingStatus status, Date timestamp, int errorCode,
			String errorMsg) {
		ProcessingState e = new ProcessingState();
		e.setStatus(status);
		e.setTimestamp(timestamp);
		e.setErrorCode(errorCode);
		e.setErrorMessage(errorMsg);
		return e;
	}

	public static ProcessingState none() {
		return new ProcessingState(ProcessingStatus.NONE);
	}

	public static ProcessingState pending() {
		return new ProcessingState(ProcessingStatus.PENDING);
	}

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProcessingState [");
		builder.append(" status=").append(status);
		builder.append(" timestamp=").append(timestamp);
		builder.append(" errorCode=").append(errorCode);
		builder.append(" errorMessage=").append(errorMessage);
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

	public ProcessingStatus getStatus() {
		return status;
	}

	public void setStatus(ProcessingStatus status) {
		this.status = status;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = StringUtils.truncateToMaxLen(errorMessage, MAX_ERRORMESSAGE_LEN);
	}

}
