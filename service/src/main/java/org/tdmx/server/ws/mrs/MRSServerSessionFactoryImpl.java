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
package org.tdmx.server.ws.mrs;

import java.util.Map;

import org.tdmx.lib.control.domain.AccountZone;
import org.tdmx.lib.zone.domain.Domain;
import org.tdmx.lib.zone.domain.Zone;
import org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId;
import org.tdmx.server.ws.session.AbstractServerSessionFactory;

public class MRSServerSessionFactoryImpl extends AbstractServerSessionFactory<MRSServerSession> {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------
	@Override
	public MRSServerSession createServerSession(String sessionId, Map<AttributeId, Long> seedAttributes) {
		AccountZone az = fetchAccountZone(seedAttributes.get(AttributeId.AccountZoneId));

		associateZoneDB(az.getZonePartitionId());
		try {
			Zone z = fetchZone(seedAttributes.get(AttributeId.ZoneId));
			Domain d = fetchDomain(seedAttributes.get(AttributeId.DomainId));
			MRSServerSession mss = new MRSServerSession(sessionId, az, z, d);
			if (seedAttributes.containsKey(AttributeId.ChannelId)) {
				mss.setChannel(fetchChannel(seedAttributes.get(AttributeId.ChannelId)));
			}
			if (seedAttributes.containsKey(AttributeId.TemporaryChannelId)) {
				mss.setTemporaryChannel(fetchTemporaryChannel(seedAttributes.get(AttributeId.TemporaryChannelId)));
			}

			return mss;
		} finally {
			disassociateZoneDB();
		}
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

}
