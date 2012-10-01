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
