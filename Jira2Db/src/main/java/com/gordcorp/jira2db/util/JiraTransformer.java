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
