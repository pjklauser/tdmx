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

import org.tdmx.lib.zone.domain.Channel;

/**
 * Provides the connection to the remote MRS for a channel.
 * 
 * @author Peter
 *
 */
public interface RelayConnectionProvider {

	/**
	 * Set the segment's SCS URL to allow detection of own segment relay shortcut.
	 * 
	 * @param segmentScsUrl
	 */
	public void setSegmentScsUrl(String segmentScsUrl);

	/**
	 * Lookup an MRS Endpoint for the channel from the remote SCS ( determined by the other party's DNS ).
	 * 
	 * @param channel
	 * @param direction
	 *            BOTH not allowed.
	 * @param segmentScsUrl
	 *            the URL of the ROS's segment to decide whether we have same segment relaying in which case we provide
	 *            a shortcut relay.
	 * @return the information of success or failure.
	 */
	public MRSSessionHolder getMRS(Channel channel, RelayDirection direction);

}
