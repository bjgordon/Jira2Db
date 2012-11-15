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

import java.io.File;
import java.net.URI;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import com.gordcorp.jira2db.util.PropertiesWrapper;

public class App {

	final static Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws Exception {
		File log4jFile = new File("log4j.xml");
		if (log4jFile.exists()) {
			DOMConfigurator.configure("log4j.xml");
		}

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

		@SuppressWarnings("static-access")
		Option forever = OptionBuilder
				.withLongOpt("forever")
				.withDescription(
						"Will continue polling Jira and syncing forever")
				.create();

		Options options = new Options();
		options.addOption(help);
		options.addOption(project);
		options.addOption(property);
		options.addOption(testJira);
		options.addOption(forever);

		CommandLineParser parser = new GnuParser();
		try {

			CommandLine line = parser.parse(options, args);

			if (line.hasOption(help.getOpt())) {
				printHelp(options);
				return;
			}

			// Overwrite the properties file with command line arguments
			if (line.hasOption("D")) {
				String[] values = line.getOptionValues("D");
				for (int i = 0; i < values.length; i = i + 2) {
					String key = values[i];
					// If user does not provide a value for this property, use
					// an empty string
					String value = "";
					if (i + 1 < values.length) {
						value = values[i + 1];
					}

					log.info("Setting key=" + key + " value=" + value);
					PropertiesWrapper.set(key, value);
				}

			}

			if (line.hasOption("test-jira")) {
				testJira();
			} else {
				JiraSynchroniser jira = new JiraSynchroniser();

				if (line.hasOption("project")) {

					jira.setProjects(Arrays.asList(new String[] { line
							.getOptionValue("project") }));
				}

				if (line.hasOption("forever")) {
					jira.syncForever();
				} else {
					jira.syncAll();
				}
			}
		} catch (ParseException exp) {

			log.error("Parsing failed: " + exp.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	protected static void printHelp(Options options) throws Exception {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar <jarfile>", options);
	}

	/**
	 * Test connection to jira, print result to the console
	 * 
	 * @throws Exception
	 */
	protected static void testJira() {
		String uri = PropertiesWrapper.get("jira.server.uri");
		System.out.println("Testing connection to Jira at " + uri);

		try {
			JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
			URI jiraServerUri = new URI(uri);
			JiraRestClient jiraRestClient = factory.create(
					jiraServerUri,
					new BasicHttpAuthenticationHandler(PropertiesWrapper
							.get("jira.username"), PropertiesWrapper
							.get("jira.password")));

			// Test the connection by retrieving list of all projects
			jiraRestClient.getProjectClient().getAllProjects(
					new NullProgressMonitor());

			System.out.println("Successfully connected to Jira");
		} catch (Exception e) {
			System.out.println("Could not connect to jira: " + e.getMessage());
		}
	}
}
