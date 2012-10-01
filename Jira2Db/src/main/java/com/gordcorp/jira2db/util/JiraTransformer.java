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

import com.atlassian.jira.rest.client.domain.Issue;
import com.gordcorp.jira2db.persistence.dto.JiraIssueDto;

public class JiraTransformer {
	public static JiraIssueDto toJiraIssueDto(Issue issue) {
		JiraIssueDto dto = new JiraIssueDto();
		dto.setAssignee(issue.getAssignee().getName());
		dto.setCreationDate(issue.getCreationDate().toDate());
		dto.setDescription(issue.getDescription());
		dto.setJiraUri(issue.getSelf().getPath());
		dto.setKey(issue.getKey());
		dto.setPriority(issue.getPriority().getName());
		dto.setProject(issue.getProject().getName());
		dto.setReporter(issue.getReporter().getName());
		dto.setSummary(issue.getSummary());
		dto.setUpdateDate(issue.getUpdateDate().toDate());
		return dto;
	}
}
