package com.gordcorp.jira2db;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.BasicProject;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import com.gordcorp.jira2db.persistence.JiraIssueDao;
import com.gordcorp.jira2db.persistence.SqlSessionFactorySingleton;
import com.gordcorp.jira2db.persistence.dto.JiraIssueDto;
import com.gordcorp.jira2db.util.PropertiesWrapper;
import com.gordcorp.jira2db.util.Transformer;

public class Jira {
	protected final static Logger logger = LoggerFactory.getLogger(Jira.class);

	JerseyJiraRestClientFactory factory = null;
	JiraRestClient restClient = null;

	public Jira() throws Exception {

		this.factory = new JerseyJiraRestClientFactory();

		URI jiraServerUri = new URI(PropertiesWrapper.get("jira.server.uri"));
		String username = PropertiesWrapper.get("jira.username");
		String password = PropertiesWrapper.get("jira.password");

		this.restClient = factory.create(jiraServerUri,
				new BasicHttpAuthenticationHandler(username, password));

	}

	protected void syncProject(BasicProject basicProject) {

		NullProgressMonitor pm = new NullProgressMonitor();
		SearchResult searchResult = restClient.getSearchClient().searchJql(
				"project = \"" + basicProject.getName() + "\"", pm);
		for (BasicIssue issueResult : searchResult.getIssues()) {

			issueResult.getKey();
			Issue issue = restClient.getIssueClient().getIssue(
					issueResult.getKey(), pm);
			JiraIssueDao jiraIssueDao = new JiraIssueDao(JiraIssueDto.class,
					SqlSessionFactorySingleton.instance());

			JiraIssueDto newJiraIssueDto = Transformer.toJiraIssueDto(issue);
			if (jiraIssueDao.create(newJiraIssueDto) != 1) {
				throw new RuntimeException("Problem inserting "
						+ newJiraIssueDto);
			}
		}
	}

	public void verifySync() {
		NullProgressMonitor pm = new NullProgressMonitor();
		Iterable<BasicProject> projects = restClient.getProjectClient()
				.getAllProjects(pm);
		for (BasicProject basicProject : projects) {
			syncProject(basicProject);
		}
	}

	public void doSync() {
		NullProgressMonitor pm = new NullProgressMonitor();
		Iterable<BasicProject> projects = restClient.getProjectClient()
				.getAllProjects(pm);
		for (BasicProject basicProject : projects) {
			syncProject(basicProject);
		}

	}
}
