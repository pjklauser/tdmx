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

/**
 * The AccountZoneStatus represents the state of a Zone as defined by the Account.
 * 
 * The Account is free to toggle the AccountZone's status at any time. The AccountZoneStatus propagates to to all Agents
 * of the Zone.
 * 
 * @author Peter
 * 
 */
public enum AccountZoneStatus {

	/**
	 * The AccountZone is active so that Agents associated with the Zone may interact with the ServiceProvider.
	 */
	ACTIVE,

	/**
	 * The AccountZone is blocked so that Agents associated with the Zone may not interact with the ServiceProvider.
	 * 
	 * Determined by the AccountUser.
	 */
	BLOCKED,

	/**
	 * The AccountZone is in the process of being deleted and will disappear shortly at the discretion of the
	 * ServiceProvider.
	 */
	DELETED;

	public static final int MAX_ACCOUNTZONESTATUS_LEN = 16;
}
