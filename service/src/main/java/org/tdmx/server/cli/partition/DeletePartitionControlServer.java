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
package org.tdmx.server.cli.partition;

import java.io.PrintStream;
import java.util.List;

import javax.ws.rs.core.Response;

import org.tdmx.core.cli.annotation.Cli;
import org.tdmx.core.cli.annotation.Parameter;
import org.tdmx.server.cli.cmd.AbstractCliCommand;
import org.tdmx.server.rs.sas.resource.PartitionControlServerResource;

@Cli(name = "pcs:delete", description = "deletes a partition control server")
public class DeletePartitionControlServer extends AbstractCliCommand {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------

	@Parameter(name = "segment", required = true, description = "the segment name.")
	private String segment;
	@Parameter(name = "modulo", required = true, description = "the server's load distribution modulo.")
	private int modulo;

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------

	@Override
	public void run(PrintStream out) {
		List<PartitionControlServerResource> pcss = getSas().searchPartitionControlServer(0, 1, segment, modulo, null,
				null);
		if (pcss.isEmpty()) {
			out.println("No PartitionControlServer found with IP endpoint " + segment + " modulo " + modulo);
			return;
		}

		PartitionControlServerResource pcs = pcss.get(0);
		Response response = getSas().deletePartitionControlServer(pcs.getId());
		out.print(pcs.getCliRepresentation());
		if (response.getStatus() == SUCCESS) {
			out.println(" Deleted.");
		} else {
			out.println(" Not deleted. StatusCode=" + response.getStatus());
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