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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gordcorp.jira2db.jira.Jira;
import com.gordcorp.jira2db.persistence.JiraIssueDao;
import com.gordcorp.jira2db.persistence.SqlSessionFactorySingleton;
import com.gordcorp.jira2db.persistence.dto.JiraIssueDto;

public class JiraSynchroniser {

	protected final static Logger log = LoggerFactory
			.getLogger(JiraSynchroniser.class);

	List<String> projects = null;

	Integer minutes = null;

	public JiraSynchroniser() {

	}

	public void setProjects(List<String> projects) {
		this.projects = projects;
		List<String> allProjects = Jira.getAllProjects();
		for (String project : projects) {
			if (!allProjects.contains(project)) {
				throw new RuntimeException("Project not found in Jira: "
						+ project);
			}
		}
	}

	public void setUpdatedWithinMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public void doSync() {
		log.info("Syncing all");

		if (projects == null) {
			log.info("Will sync all projects");
			projects = Jira.getAllProjects();
			log.info("Found " + projects.size() + " projects to sync");
		} else {
			log.info("Syncing these projects: " + projects);
		}

		JiraIssueDao jiraIssueDao = new JiraIssueDao(JiraIssueDto.class,
				SqlSessionFactorySingleton.instance());

		for (String projectName : projects) {
			log.info("Syncing project " + projectName);
			List<JiraIssueDto> dtos = null;
			if (minutes == null) {
				dtos = Jira.getAllIssuesInProject(projectName);
			} else {
				dtos = Jira.getIssuesUpdatedWithin(projectName, minutes);
			}

			log.info("Number of issues found: " + dtos.size());
			for (JiraIssueDto jiraIssueDto : dtos) {
				log.info("Checking if issue already exists: "
						+ jiraIssueDto.getKey());
				JiraIssueDto readJiraIssueDto = jiraIssueDao
						.getByKey(jiraIssueDto.getKey());
				if (readJiraIssueDto == null) {
					log.info("Creating " + jiraIssueDto.getKey());
					if (jiraIssueDao.create(jiraIssueDto) != 1) {
						throw new RuntimeException("Problem inserting "
								+ jiraIssueDto);
					}
				} else {
					log.info("Updating " + jiraIssueDto.getKey());
					if (jiraIssueDao.update(jiraIssueDto) != 1) {
						throw new RuntimeException("Problem updating "
								+ jiraIssueDto);
					}
				}
			}
			log.info("Finished syncing project " + projectName);
		}
		log.info("Finished syncing all projects");
	}
}
