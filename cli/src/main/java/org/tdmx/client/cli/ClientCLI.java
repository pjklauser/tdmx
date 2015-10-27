package org.tdmx.client.cli;
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

import java.io.InputStreamReader;

import org.tdmx.client.cli.command.certificate.CreateDomainAdministratorCredentials;
import org.tdmx.client.cli.command.certificate.CreateUserCredentials;
import org.tdmx.client.cli.command.certificate.CreateZoneAdministratorCredentials;
import org.tdmx.core.cli.CliParser;
import org.tdmx.core.cli.CliRunnerImpl;
import org.tdmx.core.cli.CommandDescriptor;
import org.tdmx.core.cli.CommandDescriptorFactory;
import org.tdmx.core.cli.CommandDescriptorFactoryImpl;
import org.tdmx.core.cli.InputStreamTokenizer;
import org.tdmx.core.cli.runtime.CommandExecutable;
import org.tdmx.core.cli.runtime.CommandExecutableFactory;

/**
 * A CLI for local client administration.
 * 
 * @author Peter
 *
 */
public class ClientCLI {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	private static final Class<? extends CommandExecutable>[] commandClasses = new Class[] {
			CreateZoneAdministratorCredentials.class, CreateDomainAdministratorCredentials.class,
			CreateUserCredentials.class, };
	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	private ClientCLI() {
	}

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------

	public static void main(String[] args) {
		final CommandDescriptorFactory commandDescriptorFactory = new CommandDescriptorFactoryImpl(commandClasses);

		final CommandExecutableFactory commandExecutableFactory = new CommandExecutableFactory() {

			@Override
			public CommandExecutable getCommandExecutable(String cmdName) {
				CommandDescriptor cmd = commandDescriptorFactory.getCommand(cmdName);
				if (cmd == null) {
					throw new IllegalArgumentException("No cmd " + cmdName);
				}
				Class<? extends CommandExecutable> cmdClazz = cmd.getClazz();
				try {
					return cmdClazz.newInstance();
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}
			}
		};
		CliRunnerImpl runner = new CliRunnerImpl();
		runner.setCommandExecutableFactory(commandExecutableFactory);

		CliParser cliparser = new CliParser();
		cliparser.setCommandDescriptorFactory(commandDescriptorFactory);
		cliparser.setCliRunner(runner);

		InputStreamTokenizer tokenizer = args.length > 0 ? new InputStreamTokenizer(args)
				: new InputStreamTokenizer(new InputStreamReader(System.in));

		cliparser.process(tokenizer, System.out, System.err);

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