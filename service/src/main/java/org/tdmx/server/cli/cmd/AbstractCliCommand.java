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
package org.tdmx.server.cli.cmd;

import org.tdmx.core.cli.runtime.CommandExecutable;
import org.tdmx.server.rs.sas.AccountResource;
import org.tdmx.server.rs.sas.SAS;

public abstract class AbstractCliCommand implements CommandExecutable {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------
	protected static final int PAGE_SIZE = 10;
	protected static final int SUCCESS = 200;

	private SAS sas;

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PROTECTED METHODS
	// -------------------------------------------------------------------------

	protected String outputRepresentation(AccountResource account) {
		StringBuilder buf = new StringBuilder();
		buf.append("Account");
		buf.append("; ").append(account.getId());
		buf.append("; ").append(account.getAccountId());
		buf.append("; ").append(account.getEmail());
		buf.append("; ").append(account.getFirstname());
		buf.append("; ").append(account.getLastname());
		return buf.toString();
	}

	// -------------------------------------------------------------------------
	// PRIVATE METHODS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PUBLIC ACCESSORS (GETTERS / SETTERS)
	// -------------------------------------------------------------------------

	public SAS getSas() {
		return sas;
	}

	public void setSas(SAS sas) {
		this.sas = sas;
	}

}