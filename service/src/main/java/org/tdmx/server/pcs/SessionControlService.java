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
package org.tdmx.server.pcs;

import org.tdmx.client.crypto.certificate.PKIXCertificate;
import org.tdmx.server.pcs.protobuf.PCSServer.FindApiSessionResponse;
import org.tdmx.server.session.WebServiceSessionEndpoint;
import org.tdmx.server.ws.session.WebServiceApiName;

public interface SessionControlService {

	/**
	 * Try and associate the clientCertificate a session for the API in the sessionData. Called by SessionControlService
	 * towards the PartitionControlService.
	 * 
	 * @param sessionData
	 * @param clientCertificate
	 * @return null if no capacity available.
	 */
	public WebServiceSessionEndpoint associateApiSession(SessionHandle sessionData, PKIXCertificate clientCertificate);

	/**
	 * Lookup on which tosAddress the session resides.
	 * 
	 * @param segment
	 * @param api
	 * @param sessionKey
	 * @return the sessionId and tosAddress wrapped in a response object.
	 */
	public FindApiSessionResponse findApiSession(String segment, WebServiceApiName api, String sessionKey);

}
