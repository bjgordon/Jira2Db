/*

	Copyright 2012 Brendan Gordon
  
	Jira2Db is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.gordcorp.jira2db;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import com.gordcorp.jira2db.persistence.SqlSessionFactorySingleton;
import com.gordcorp.jira2db.persistence.mapper.DbMapper;
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
		testMybatis();

		Jira jira = new Jira();

	}

	private static void testMybatis() {
		SqlSession session = SqlSessionFactorySingleton.instance()
				.openSession();
		try {
			DbMapper mapper = session.getMapper(DbMapper.class);
			List<Long> ids = mapper.getIdsFromMap();

			System.out.println("ID:" + ids.get(0));

		} finally {
			session.close();
		}
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

	public static void testJdbc() {
		String url = PropertiesWrapper.get("url");
		Properties props = new Properties();
		props.setProperty("user", PropertiesWrapper.get("username"));
		props.setProperty("password", PropertiesWrapper.get("password"));
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, props);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from map");
			while (rs.next()) {
				long id = rs.getLong(1);
				System.out.println("ID:" + id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
