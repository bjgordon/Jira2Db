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

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import com.gordcorp.jira2db.util.PropertiesWrapper;

public class App {

	final static Logger log = LoggerFactory.getLogger(App.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Option help = new Option("help", "print this message");

		@SuppressWarnings("static-access")
		Option property = OptionBuilder.withArgName("property=value")
				.hasArgs(2).withValueSeparator()
				.withDescription("use value for given property").create("D");

		Options options = new Options();
		options.addOption(help);
		options.addOption(property);

		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("D")) {
				String[] values = line.getOptionValues("D");
				PropertiesWrapper.set(values[0], values[1]);
			}

			if (line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar jira2db", options);
			}

		} catch (ParseException exp) {

			log.error("Parsing failed.  Reason: " + exp.getMessage());
		}

		testConnectionToJira();
		// testJdbc();
		// testJpa();
		// testMybatis();

		// Jira jira = new Jira();

	}

	public static void testConnectionToJira() throws Exception {
		try {
			JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
			URI jiraServerUri = new URI(
					PropertiesWrapper.get("jira.server.uri"));
			JiraRestClient restClient = factory.create(
					jiraServerUri,
					new BasicHttpAuthenticationHandler(PropertiesWrapper
							.get("jira.username"), PropertiesWrapper
							.get("jira.password")));

			System.out.println("Successfully connected to jira: "
					+ jiraServerUri);
		} catch (Exception e) {
			System.out.println("Problem connecting to jira: " + e);
		}
	}
}
