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

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import org.tdmx.client.crypto.algorithm.SignatureAlgorithm;
import org.tdmx.client.crypto.certificate.PKIXCredential;
import org.tdmx.core.api.SignatureUtils;
import org.tdmx.core.api.v01.msg.Flowtarget;
import org.tdmx.lib.common.domain.ProcessingState;
import org.tdmx.lib.common.domain.ProcessingStatus;
import org.tdmx.server.ws.ApiToDomainMapper;
import org.tdmx.server.ws.DomainToApiMapper;

public class ZoneFacade {

	public static final BigInteger ONE_KB = BigInteger.valueOf(1024);
	public static final BigInteger ONE_MB = BigInteger.valueOf(1024 * 1024);
	public static final BigInteger ONE_GB = BigInteger.valueOf(1024 * 1024 * 1024);

	public static final String DUMMY_SP_URL = "https://localhost:9000/api/mrs/v1.0";

	private static final DomainToApiMapper d2a = new DomainToApiMapper();
	private static final ApiToDomainMapper a2d = new ApiToDomainMapper();

	public static Zone createZone(Long accountZoneId, String zoneApex) throws Exception {
		Zone z = new Zone(accountZoneId, zoneApex);
		return z;
	}

	public static Domain createDomain(Zone zone, String domainName) throws Exception {
		Domain d = new Domain(zone, domainName);
		return d;
	}

	public static Address createAddress(Domain domain, String localName) throws Exception {
		Address a = new Address(domain, localName);
		return a;
	}

	public static Service createService(Domain domain, String serviceName, int concurrencyLimit) throws Exception {
		Service s = new Service(domain, serviceName);
		s.setConcurrencyLimit(concurrencyLimit);
		return s;
	}

	public static ChannelOrigin createChannelOrigin(String localName, String domainName, String serviceProvider) {
		ChannelOrigin co = new ChannelOrigin();
		co.setLocalName(localName);
		co.setDomainName(domainName);
		co.setServiceProvider(serviceProvider);
		return co;
	}

	public static ChannelDestination createChannelDestination(String localName, String domainName, String serviceName,
			String serviceProvider) {
		ChannelDestination cd = new ChannelDestination();
		cd.setLocalName(localName);
		cd.setDomainName(domainName);
		cd.setServiceName(serviceName);
		cd.setServiceProvider(serviceProvider);
		return cd;
	}

	public static FlowTarget createFlowTarget(PKIXCredential userCred, AgentCredential userAgent, Service service) {
		FlowTarget ft = new FlowTarget(userAgent, service);

		FlowSession ps = new FlowSession();
		ps.setScheme("encryptionscheme");
		ps.setSessionKey(new byte[] { 1, 2, 3 });
		ps.setValidFrom(new Date());
		ft.setPrimary(ps);

		Flowtarget aft = d2a.mapFlowTarget(ft);
		SignatureUtils.createFlowTargetSignature(userCred, SignatureAlgorithm.SHA_256_RSA, new Date(), aft);

		return a2d.mapFlowTarget(userAgent, service, aft.getFlowtargetsession());
	}

	// TODO give DAC to be able to construct correct signatures
	public static ChannelAuthorization createSendRecvChannelAuthorization(Domain domain, PKIXCredential userCred,
			AgentCredential userAgent, ChannelOrigin origin, ChannelDestination dest) {

		Channel channel = new Channel(domain);
		channel.setOrigin(origin);
		channel.setDestination(dest);

		ChannelAuthorization c = new ChannelAuthorization(channel);
		channel.setAuthorization(c);

		EndpointPermission sendPermission = new EndpointPermission();
		sendPermission.setGrant(EndpointPermissionGrant.ALLOW);
		sendPermission.setMaxPlaintextSizeBytes(ONE_MB);
		sendPermission.setValidUntil(getDateYearsFromNow(1));
		AgentSignature sendPermSignature = new AgentSignature();
		sendPermSignature.setAlgorithm(SignatureAlgorithm.SHA_256_RSA);
		sendPermSignature.setCertificateChainPem(userAgent.getCertificateChainPem());
		sendPermSignature.setSignatureDate(new Date());
		sendPermSignature.setValue("hexvalueofsignature");
		sendPermission.setSignature(sendPermSignature);
		c.setSendAuthorization(sendPermission);

		EndpointPermission recvPermission = new EndpointPermission();
		recvPermission.setGrant(EndpointPermissionGrant.ALLOW);
		recvPermission.setMaxPlaintextSizeBytes(ONE_MB);
		recvPermission.setValidUntil(getDateYearsFromNow(1));
		AgentSignature recvPermSignature = new AgentSignature();
		recvPermSignature.setAlgorithm(SignatureAlgorithm.SHA_256_RSA);
		recvPermSignature.setCertificateChainPem(userAgent.getCertificateChainPem());
		recvPermSignature.setSignatureDate(new Date());
		recvPermSignature.setValue("hexvalueofsignature");
		recvPermission.setSignature(recvPermSignature);
		c.setRecvAuthorization(recvPermission);

		FlowLimit unsentBuffer = new FlowLimit();
		unsentBuffer.setHighMarkBytes(ONE_GB);
		unsentBuffer.setLowMarkBytes(ONE_MB);
		c.setUnsentBuffer(unsentBuffer);

		FlowLimit undeliveredBuffer = new FlowLimit();
		undeliveredBuffer.setHighMarkBytes(ONE_GB);
		undeliveredBuffer.setLowMarkBytes(ONE_MB);
		c.setUndeliveredBuffer(undeliveredBuffer);

		// TODO make a valid signature
		AgentSignature signature = new AgentSignature();
		signature.setAlgorithm(SignatureAlgorithm.SHA_256_RSA);
		signature.setCertificateChainPem(userAgent.getCertificateChainPem());
		signature.setSignatureDate(new Date());
		signature.setValue("hexvalueofsignature");
		c.setSignature(signature);

		c.setProcessingState(new ProcessingState(ProcessingStatus.SUCCESS));
		return c;
	}

	public static ChannelAuthorization createSendChannelAuthorization(Domain domain, PKIXCredential userCred,
			AgentCredential userAgent, ChannelOrigin origin, ChannelDestination dest) {

		Channel channel = new Channel(domain);
		channel.setOrigin(origin);
		channel.setDestination(dest);

		ChannelAuthorization c = new ChannelAuthorization(channel);
		channel.setAuthorization(c);

		EndpointPermission sendPermission = new EndpointPermission();
		sendPermission.setGrant(EndpointPermissionGrant.ALLOW);
		sendPermission.setMaxPlaintextSizeBytes(ONE_MB);
		sendPermission.setValidUntil(getDateYearsFromNow(1));
		AgentSignature sendPermSignature = new AgentSignature();
		sendPermSignature.setAlgorithm(SignatureAlgorithm.SHA_256_RSA);
		sendPermSignature.setCertificateChainPem(userAgent.getCertificateChainPem());
		sendPermSignature.setSignatureDate(new Date());
		sendPermSignature.setValue("hexvalueofsignature");
		sendPermission.setSignature(sendPermSignature);
		c.setSendAuthorization(sendPermission);

		// pending authorizations not set.

		// undelivered is on the recv side
		FlowLimit unsentBuffer = new FlowLimit();
		unsentBuffer.setHighMarkBytes(ONE_GB);
		unsentBuffer.setLowMarkBytes(ONE_MB);
		c.setUnsentBuffer(unsentBuffer);

		// TODO make a valid signature
		AgentSignature signature = new AgentSignature();
		signature.setAlgorithm(SignatureAlgorithm.SHA_256_RSA);
		signature.setCertificateChainPem("certificateChainPem");
		signature.setSignatureDate(new Date());
		signature.setValue("hexvalueofsignature");
		c.setSignature(signature);

		c.setProcessingState(new ProcessingState(ProcessingStatus.SUCCESS));
		return c;
	}

	public static ChannelAuthorization createRecvChannelAuthorization(Domain domain, PKIXCredential userCred,
			AgentCredential userAgent, ChannelOrigin origin, ChannelDestination dest) {
		Channel channel = new Channel(domain);
		channel.setOrigin(origin);
		channel.setDestination(dest);

		ChannelAuthorization c = new ChannelAuthorization(channel);
		channel.setAuthorization(c);

		EndpointPermission recvPermission = new EndpointPermission();
		recvPermission.setGrant(EndpointPermissionGrant.ALLOW);
		recvPermission.setMaxPlaintextSizeBytes(ONE_MB);
		recvPermission.setValidUntil(getDateYearsFromNow(1));
		AgentSignature recvPermSignature = new AgentSignature();
		recvPermSignature.setAlgorithm(SignatureAlgorithm.SHA_256_RSA);
		recvPermSignature.setCertificateChainPem(userAgent.getCertificateChainPem());
		recvPermSignature.setSignatureDate(new Date());
		recvPermSignature.setValue("hexvalueofsignature");
		recvPermission.setSignature(recvPermSignature);
		c.setRecvAuthorization(recvPermission);

		// pending authorizations not set.

		FlowLimit undeliveredBuffer = new FlowLimit();
		undeliveredBuffer.setHighMarkBytes(ONE_GB);
		undeliveredBuffer.setLowMarkBytes(ONE_MB);
		c.setUndeliveredBuffer(undeliveredBuffer);

		// TODO make a valid signature
		AgentSignature signature = new AgentSignature();
		signature.setAlgorithm(SignatureAlgorithm.SHA_256_RSA);
		signature.setCertificateChainPem("certificateChainPem");
		signature.setSignatureDate(new Date());
		signature.setValue("hexvalueofsignature");
		c.setSignature(signature);

		c.setProcessingState(new ProcessingState(ProcessingStatus.SUCCESS));
		return c;
	}

	public static Date getDateYearsFromNow(int years) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, years);
		return cal.getTime();
	}
}
