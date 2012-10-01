/*******************************************************************************
 * Copyright 2012 Brendan Gordon
 * 	
 * 	This file is part of Jira2Db.
 * 	
 * 	Jira2Db is free software: you can redistribute it and/or modify
 * 	it under the terms of the GNU General Public License as published by
 * 	the Free Software Foundation, either version 3 of the License, or
 * 	(at your option) any later version.
 * 	
 * 	Jira2Db is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 	GNU General Public License for more details.
 * 	
 * 	You should have received a copy of the GNU General Public License
 * 	along with Jira2Db.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.gordcorp.jira2db;

import java.net.URI;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import com.gordcorp.jira2db.util.PropertiesWrapper;

public class App {

	final static Logger log = LoggerFactory.getLogger(App.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		@SuppressWarnings("static-access")
		Option allProjects = OptionBuilder.withLongOpt("all-projects")
				.withDescription("Sync all Jira projects").create();

		Option help = new Option("h", "help", false, "print this message");

		@SuppressWarnings("static-access")
		Option project = OptionBuilder.withLongOpt("project")
				.withDescription("Only sync Jira project PROJECT").hasArg()
				.withArgName("PROJECT").create();

		@SuppressWarnings("static-access")
		Option property = OptionBuilder.withArgName("property=value")
				.hasArgs(2).withValueSeparator()
				.withDescription("use value for given property").create("D");

		@SuppressWarnings("static-access")
		Option testJira = OptionBuilder
				.withLongOpt("test-jira")
				.withDescription(
						"Test the connection to Jira and print the results")
				.create();

		Options options = new Options();
		options.addOption(allProjects);
		options.addOption(help);
		options.addOption(project);
		options.addOption(property);
		options.addOption(testJira);

		CommandLineParser parser = new GnuParser();
		try {

			CommandLine line = parser.parse(options, args);

			if (line.hasOption(help.getOpt())) {
				printHelp(options);
				return;
			}

			// Overwrite the properties file with cmdline args
			if (line.hasOption("D")) {
				String[] values = line.getOptionValues("D");

				PropertiesWrapper.set(values[0], values[1]);
			}

			if (line.hasOption("test-jira")) {
				testJira();
			} else {
				if (line.hasOption("project")) {
					Jira jira = new Jira();
					jira.syncProject(line.getOptionValue("project"));
				} else if (line.hasOption("all-projects")) {
					Jira jira = new Jira();
					jira.doSync();
				} else {
					printHelp(options);
				}
			}

		} catch (ParseException exp) {

			log.error("Parsing failed.  Reason: " + exp.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar jira2db", options);
	}

	public static void testJira() throws Exception {
		String uri = PropertiesWrapper.get("jira.server.uri");
		System.out.println("Testing connection to Jira at " + uri);

		try {
			JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
			URI jiraServerUri = new URI(uri);
			factory.create(
					jiraServerUri,
					new BasicHttpAuthenticationHandler(PropertiesWrapper
							.get("jira.username"), PropertiesWrapper
							.get("jira.password")));

			System.out.println("Successfully connected to Jira");
		} catch (Exception e) {
			System.out.println("Could not connect to jira: " + e);
		}
	}
}
