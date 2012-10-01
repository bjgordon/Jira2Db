package com.gordcorp.jira2db.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atlassian.jira.rest.client.domain.Issue;
import com.gordcorp.jira2db.persistence.dto.JiraIssueDto;

public class JiraTransformerTest {

	@Test
	public void test_toJiraIssueDto() {
		Issue issue = new Issue("test summary", null, "http://example.com",
				null, null, null, "test-1", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null);

		JiraIssueDto dto = JiraTransformer.toJiraIssueDto(issue);
		assertEquals(issue.getKey(), dto.getKey());
		assertEquals(issue.getSummary(), dto.getSummary());
		assertEquals(issue.getTransitionsUri(), dto.getJiraUri());

		// todo check other fields

	}

}
