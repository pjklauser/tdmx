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

import java.util.Calendar;
import java.util.List;

import org.tdmx.core.cli.annotation.Cli;
import org.tdmx.core.cli.annotation.Parameter;
import org.tdmx.core.cli.display.CliPrinter;
import org.tdmx.server.cli.cmd.AbstractCliCommand;
import org.tdmx.server.rs.sas.resource.DatabasePartitionResource;

@Cli(name = "partition:activate", description = "deactivates a database partition.")
public class ActivatePartition extends AbstractCliCommand {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------

	@Parameter(name = "partition", required = true, description = "the partition identifier.")
	private String partitionId;

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------

	@Override
	public void run(CliPrinter out) {
		List<DatabasePartitionResource> dbPartitions = getSas().searchDatabasePartition(0, 1, partitionId, null, null);
		if (dbPartitions.isEmpty()) {
			out.println("No DatabasePartition found with partitionId " + partitionId);
			return;
		}

		DatabasePartitionResource dbPartition = dbPartitions.get(0);
		if (dbPartition.getActivationTimestamp() != null) {
			out.println("DatabasePartition partition " + partitionId + " has been activated already.");
			return;
		}
		if (dbPartition.getDeactivationTimestamp() != null) {
			out.println("DatabasePartition partition " + partitionId + " is already deactivated.");
			return;
		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);

		dbPartition.setActivationTimestamp(cal.getTime());

		DatabasePartitionResource newDbPartition = getSas().updateDatabasePartition(dbPartition.getId(), dbPartition);
		out.println(newDbPartition);
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
