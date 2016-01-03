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
import java.math.BigInteger;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * An FlowQuota is separated as it's own instance from the Channel so that it can be updated with a higher frequency
 * without incurring the penalty of the data of the Channel not changing fast.
 * 
 * @author Peter Klauser
 * 
 */
@Entity
@Table(name = "FlowQuota")
public class FlowQuota implements Serializable {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = -1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "FlowQuotaIdGen")
	@TableGenerator(name = "FlowQuotaIdGen", table = "PrimaryKeyGen", pkColumnName = "NAME", pkColumnValue = "zoneObjectId", valueColumnName = "value", allocationSize = 10)
	private Long id;

	@OneToOne(optional = false, fetch = FetchType.LAZY, mappedBy = "quota")
	private Channel channel;

	@Enumerated(EnumType.STRING)
	@Column(length = ChannelAuthorizationStatus.MAX_AUTH_STATUS_LEN, nullable = false)
	private ChannelAuthorizationStatus authorizationStatus;

	@Enumerated(EnumType.STRING)
	@Column(length = FlowControlStatus.MAX_FLOWCONTROL_STATUS_LEN, nullable = false)
	private FlowControlStatus relayStatus;

	@Enumerated(EnumType.STRING)
	@Column(length = FlowControlStatus.MAX_FLOWCONTROL_STATUS_LEN, nullable = false)
	private FlowControlStatus flowStatus;

	@Column(nullable = false)
	private BigInteger usedBytes;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "highMarkBytes", column = @Column(name = "limitHighBytes") ),
			@AttributeOverride(name = "lowMarkBytes", column = @Column(name = "limitLowBytes") ) })
	private FlowLimit limit;

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	FlowQuota() {
	}

	public FlowQuota(Channel channel) {
		setChannel(channel);
		setUsedBytes(BigInteger.ZERO);
		setFlowStatus(FlowControlStatus.OPEN);
		setRelayStatus(FlowControlStatus.OPEN);
		updateAuthorizationInfo();
	}

	public FlowQuota(Channel channel, FlowQuota other) {
		setChannel(channel);
		setUsedBytes(other.getUsedBytes());
		setRelayStatus(other.getRelayStatus());
		setFlowStatus(other.getFlowStatus());
		setAuthorizationStatus(other.getAuthorizationStatus());
	}

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------

	/**
	 * Update the FlowQuota's denormalized data regarding the authorization state and flow control limits.
	 */
	public void updateAuthorizationInfo() {
		if (channel != null && channel.getAuthorization() != null) {
			setAuthorizationStatus(
					channel.isOpen() ? ChannelAuthorizationStatus.OPEN : ChannelAuthorizationStatus.CLOSED);
			setLimit(channel.getAuthorization().getLimit());
		}
	}

	public void incrementBufferOnSend(long payloadSizeBytes) {
		setUsedBytes(getUsedBytes().add(BigInteger.valueOf(payloadSizeBytes)));
		if (getUsedBytes().subtract(getLimit().getHighMarkBytes()).compareTo(BigInteger.ZERO) > 0) {
			// quota exceeded, close send
			setFlowStatus(FlowControlStatus.CLOSED);
		}
	}

	public void incrementBufferOnRelay(long payloadSizeBytes) {
		setUsedBytes(getUsedBytes().add(BigInteger.valueOf(payloadSizeBytes)));
		if (getUsedBytes().subtract(getLimit().getHighMarkBytes()).compareTo(BigInteger.ZERO) > 0) {
			// quota exceeded, close relay
			setFlowStatus(FlowControlStatus.CLOSED);
			setRelayStatus(FlowControlStatus.CLOSED);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FlowQuota [id=");
		builder.append(id);
		builder.append(", authorizationStatus=").append(authorizationStatus);
		builder.append(", relayStatus=").append(relayStatus);
		builder.append(", flowStatus=").append(flowStatus);
		builder.append(", usedBytes=").append(usedBytes);
		builder.append(", limit=").append(limit);
		builder.append("]");
		return builder.toString();
	}

	// -------------------------------------------------------------------------
	// PROTECTED METHODS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PRIVATE METHODS
	// -------------------------------------------------------------------------

	private void setChannel(Channel channel) {
		this.channel = channel;
	}

	// -------------------------------------------------------------------------
	// PUBLIC ACCESSORS (GETTERS / SETTERS)
	// -------------------------------------------------------------------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ChannelAuthorizationStatus getAuthorizationStatus() {
		return authorizationStatus;
	}

	public void setAuthorizationStatus(ChannelAuthorizationStatus authorizationStatus) {
		this.authorizationStatus = authorizationStatus;
	}

	public FlowControlStatus getRelayStatus() {
		return relayStatus;
	}

	public void setRelayStatus(FlowControlStatus relayStatus) {
		this.relayStatus = relayStatus;
	}

	public FlowControlStatus getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(FlowControlStatus flowStatus) {
		this.flowStatus = flowStatus;
	}

	public BigInteger getUsedBytes() {
		return usedBytes;
	}

	public void setUsedBytes(BigInteger usedBytes) {
		this.usedBytes = usedBytes;
	}

	public FlowLimit getLimit() {
		return limit;
	}

	public void setLimit(FlowLimit limit) {
		this.limit = limit;
	}

}
