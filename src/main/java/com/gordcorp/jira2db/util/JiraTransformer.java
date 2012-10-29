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

import java.util.ArrayList;

import com.atlassian.jira.rest.client.domain.Field;
import com.atlassian.jira.rest.client.domain.Issue;
import com.gordcorp.jira2db.persistence.dto.JiraCustomFieldDto;
import com.gordcorp.jira2db.persistence.dto.JiraIssueDto;

public class JiraTransformer {

	public static JiraIssueDto toJiraIssueDto(Issue issue) {
		JiraIssueDto dto = new JiraIssueDto();
		if (issue.getAssignee() != null) {
			dto.setAssignee(issue.getAssignee().getName());
		}
		if (issue.getCreationDate() != null) {
			dto.setCreationDate(issue.getCreationDate().toDate());
		}

		dto.setDescription(issue.getDescription());

		if (issue.getSelf() != null) {
			dto.setJiraUri(issue.getSelf().getPath());
		}

		dto.setJiraKey(issue.getKey());

		if (issue.getPriority() != null) {
			dto.setPriority(issue.getPriority().getName());
		}

		if (issue.getProject() != null) {
			dto.setProject(issue.getProject().getName());
		}

		if (issue.getReporter() != null) {
			dto.setReporter(issue.getReporter().getName());
		}

		if (issue.getStatus() != null) {
			dto.setStatus(issue.getStatus().getName());
		}

		dto.setSummary(issue.getSummary());

		if (issue.getUpdateDate() != null) {
			dto.setUpdateDate(issue.getUpdateDate().toDate());
		}

		dto.setCustomFields(new ArrayList<JiraCustomFieldDto>());

		if (issue.getFields() != null) {
			for (Field field : issue.getFields()) {
				JiraCustomFieldDto jiraCustomFieldDto = new JiraCustomFieldDto();
				jiraCustomFieldDto.setJiraKey(dto.getJiraKey());
				jiraCustomFieldDto.setId(field.getId());
				jiraCustomFieldDto.setName(field.getName());
				jiraCustomFieldDto.setType(field.getType());
				// todo maybe need to convert field.getValue to a string value
				// better than this?
				if (field.getValue() == null) {
					jiraCustomFieldDto.setValue(null);
				} else {
					jiraCustomFieldDto.setValue(field.getValue().toString());
				}
				dto.getCustomFields().add(jiraCustomFieldDto);
			}
		}
		return dto;
	}

}
