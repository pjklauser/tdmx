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
package org.tdmx.server.ws.session;

import java.util.Map;

public interface WebServiceSessionFactory<E extends WebServiceSession> {

	public enum SeedAttribute {
		AccountZoneId,
		ZoneId,
		DomainId,
		ChannelId,
		TemporaryChannelId,
		ServiceId,
		AddressId,
	}

	/**
	 * Initialize a WebServiceSession given the seedAttributes.
	 * 
	 * @param sessionId
	 *            the sessionId
	 * @param seedAttributes
	 *            the initial session attribute values which can be resolved to objects.
	 * @return a new WebServiceSession initialized with the seedAttributes
	 */
	public E createServerSession(String sessionId, Map<SeedAttribute, Long> seedAttributes);

}
