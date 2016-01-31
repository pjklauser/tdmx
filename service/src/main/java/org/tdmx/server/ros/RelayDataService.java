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
package org.tdmx.server.ros;

import java.util.List;

import org.tdmx.lib.common.domain.ProcessingState;
import org.tdmx.lib.control.domain.AccountZone;
import org.tdmx.lib.zone.domain.Channel;
import org.tdmx.lib.zone.domain.ChannelMessage;
import org.tdmx.lib.zone.domain.Domain;
import org.tdmx.lib.zone.domain.Zone;

/**
 * Fetches data from zone services.
 * 
 * @author Peter
 *
 */
public interface RelayDataService {

	public AccountZone getAccountZone(Long accountZoneId);

	public Zone getZone(AccountZone az, Long zoneId);

	public Domain getDomain(AccountZone az, Zone z, Long domainId);

	public Channel getChannel(AccountZone az, Zone z, Domain d, Long channelId);

	public ChannelMessage getMessage(AccountZone az, Zone z, Domain d, Channel channel, Long msgId);

	public List<ChannelMessage> getRelayMessages(AccountZone az, Zone z, Domain d, Channel channel, int maxMsg);

	public void updateChannelAuthorizationProcessingState(AccountZone az, Zone z, Domain d, Long channelId,
			ProcessingState newState);
}
