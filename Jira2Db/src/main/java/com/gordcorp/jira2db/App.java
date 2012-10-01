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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import com.gordcorp.jira2db.util.PropertiesWrapper;

public class App {

	final static Logger logger = LoggerFactory.getLogger(App.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		testJira();
		// testJdbc();
		// testJpa();
		// testMybatis();

		Jira jira = new Jira();

	}

	public static void testJira() throws Exception {
		JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
		URI jiraServerUri = new URI(PropertiesWrapper.get("jira.server.uri"));
		JiraRestClient restClient = factory.create(jiraServerUri,
				new BasicHttpAuthenticationHandler("admin", "admin"));

		final NullProgressMonitor pm = new NullProgressMonitor();
		final Issue issue = restClient.getIssueClient().getIssue("TP-1", pm);

		System.out.println(issue);
	}

}
