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
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.tdmx.core.api.v01.mos.ws.MOS;
import org.tdmx.core.api.v01.mrs.ws.MRS;

/**
 * An ChannelMessage is a message in a Flow.
 * 
 * ChannelMessages are created at by the {@link MOS#submit(org.tdmx.core.api.v01.mos.Submit)} when the Channel's
 * FlowControl permits sending on the originating side, and when relayed in with
 * {@link MRS#relay(org.tdmx.core.api.v01.mrs.Relay)} at the destination side .
 * 
 * @author Peter Klauser
 * 
 */
@Entity
@Table(name = "ChannelMessage")
public class ChannelMessage implements Serializable {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------
	public static final int MAX_MSGID_LEN = 64;
	public static final int MAX_SHA256_MAC_LEN = 80;
	public static final int MAX_EXTREF_LEN = 256;
	public static final int MAX_SIGNATURE_LEN = 128;
	public static final int MAX_CRCMANIFEST_LEN = 8000;

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = -128859602084626282L;

	// TODO "Relay" Processingstatus of msg relay

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ChannelMessageIdGen")
	@TableGenerator(name = "ChannelMessageIdGen", table = "PrimaryKeyGen", pkColumnName = "NAME", pkColumnValue = "channelmessageObjectId", valueColumnName = "value", allocationSize = 10)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Channel channel;

	// -------------------------------------------------------------------------
	// HEADER FIELDS
	// -------------------------------------------------------------------------
	@Column(length = MAX_MSGID_LEN, nullable = false)
	private String msgId;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date ttlTimestamp;

	/**
	 * The public certificate of the receiving Agent.
	 * 
	 * NOTE: Maximum length is defined by {@link AgentCredential#MAX_CERTIFICATECHAIN_LEN}
	 */
	@Column(name = "receiverPem", length = AgentCredential.MAX_CERTIFICATECHAIN_LEN, nullable = false)
	private String receiverCertificateChainPem;

	@Column(length = DestinationSession.MAX_IDENTIFIER_LEN, nullable = false)
	private String encryptionContextId;

	@Column(length = MAX_SIGNATURE_LEN, nullable = false)
	private String payloadSignature;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "signatureDate", column = @Column(name = "senderSignatureDate", nullable = false)),
			@AttributeOverride(name = "certificateChainPem", column = @Column(name = "senderPem", length = AgentCredential.MAX_CERTIFICATECHAIN_LEN, nullable = false)),
			@AttributeOverride(name = "value", column = @Column(name = "senderSignature", length = AgentSignature.MAX_SIGNATURE_LEN, nullable = false)),
			@AttributeOverride(name = "algorithm", column = @Column(name = "senderSignatureAlgorithm", length = AgentSignature.MAX_SIG_ALG_LEN, nullable = false)) })
	private AgentSignature signature;

	@Column(length = MAX_EXTREF_LEN)
	private String externalReference;

	// -------------------------------------------------------------------------
	// PAYLOAD FIELDS
	// -------------------------------------------------------------------------

	@Column(nullable = false)
	private long chunkSize; // chunkSize in Bytes

	@Column(nullable = false)
	private long payloadLength; // total encrypted length = SUM length chunks

	@Basic(fetch = FetchType.EAGER)
	@Column(nullable = false)
	@Lob
	private byte[] encryptionContext; // sender input to encryption scheme //TODO stipulate max len

	@Column(nullable = false)
	private long plaintextLength; // total length of plaintext ( unencrypted, unzipped )

	@Column(length = MAX_SHA256_MAC_LEN, nullable = false)
	private String macOfMacs;

	// -------------------------------------------------------------------------
	// CONTROL FIELDS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	public ChannelMessage() {
	}

	// public ChannelMessage(Channel channel, ChannelMessage other) {
	// setChannel(channel);
	// // header fields
	// setMsgId(other.getMsgId());
	// setTtlTimestamp(other.getTtlTimestamp());
	// setEncryptionContextId(other.getEncryptionContextId());
	// setExternalReference(other.getExternalReference());
	// setPayloadSignature(other.getPayloadSignature());
	// // payload fields
	// setChunkSize(other.getChunkSize());
	// setPayloadLength(other.getPayloadLength());
	// setEncryptionContext(other.getEncryptionContext());
	// setPlaintextLength(other.getPlaintextLength());
	// setMacOfMacs(other.getMacOfMacs());
	// }

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChannelMessage [id=");
		builder.append(id);
		builder.append(" msgId=").append(msgId);
		builder.append(" ttlTimestamp=").append(ttlTimestamp);
		builder.append(" payloadLength=").append(payloadLength);
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

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public Date getTtlTimestamp() {
		return ttlTimestamp;
	}

	public void setTtlTimestamp(Date ttlTimestamp) {
		this.ttlTimestamp = ttlTimestamp;
	}

	public String getPayloadSignature() {
		return payloadSignature;
	}

	public void setPayloadSignature(String payloadSignature) {
		this.payloadSignature = payloadSignature;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public long getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(long chunkSize) {
		this.chunkSize = chunkSize;
	}

	public long getPayloadLength() {
		return payloadLength;
	}

	public void setPayloadLength(long payloadLength) {
		this.payloadLength = payloadLength;
	}

	public byte[] getEncryptionContext() {
		return encryptionContext;
	}

	public void setEncryptionContext(byte[] encryptionContext) {
		this.encryptionContext = encryptionContext;
	}

	public long getPlaintextLength() {
		return plaintextLength;
	}

	public void setPlaintextLength(long plaintextLength) {
		this.plaintextLength = plaintextLength;
	}

	public String getEncryptionContextId() {
		return encryptionContextId;
	}

	public void setEncryptionContextId(String encryptionContextId) {
		this.encryptionContextId = encryptionContextId;
	}

	public String getReceiverCertificateChainPem() {
		return receiverCertificateChainPem;
	}

	public void setReceiverCertificateChainPem(String receiverCertificateChainPem) {
		this.receiverCertificateChainPem = receiverCertificateChainPem;
	}

	public AgentSignature getSignature() {
		return signature;
	}

	public void setSignature(AgentSignature signature) {
		this.signature = signature;
	}

	public String getMacOfMacs() {
		return macOfMacs;
	}

	public void setMacOfMacs(String macOfMacs) {
		this.macOfMacs = macOfMacs;
	}

}