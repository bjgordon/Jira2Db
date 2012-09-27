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
import com.gordcorp.jira2db.util.JiraTransformer;
import com.gordcorp.jira2db.util.PropertiesWrapper;

public class Jira {
	protected final static Logger log = LoggerFactory.getLogger(Jira.class);

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

	protected void syncProject(String projectName) {

		NullProgressMonitor pm = new NullProgressMonitor();
		String jql = "project = \"" + projectName + "\"";

		int ISSUES_PER_SEARCH = 50;
		int issues = 0;
		SearchResult searchResult = null;
		do {
			searchResult = restClient.getSearchClient().searchJql(jql,
					ISSUES_PER_SEARCH, issues, pm);
			for (BasicIssue issueResult : searchResult.getIssues()) {
				issues++;
				log.info("issue " + issueResult.getKey());

				Issue issue = restClient.getIssueClient().getIssue(
						issueResult.getKey(), pm);
				JiraIssueDao jiraIssueDao = new JiraIssueDao(
						JiraIssueDto.class,
						SqlSessionFactorySingleton.instance());

				JiraIssueDto newJiraIssueDto = JiraTransformer
						.toJiraIssueDto(issue);
				if (jiraIssueDao.create(newJiraIssueDto) != 1) {
					throw new RuntimeException("Problem inserting "
							+ newJiraIssueDto);
				}

			}
		} while (issues < searchResult.getTotal());

	}

	public void doSync() {
		NullProgressMonitor pm = new NullProgressMonitor();
		Iterable<BasicProject> projects = restClient.getProjectClient()
				.getAllProjects(pm);
		for (BasicProject basicProject : projects) {
			log.info("project=" + basicProject.getName());
			syncProject(basicProject.getName());
		}

	}
}
