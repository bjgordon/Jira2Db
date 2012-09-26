package com.gordcorp.jira2db.persistence.mapper;

import org.apache.ibatis.annotations.Select;

import com.gordcorp.jira2db.persistence.dto.JiraIssueDto;

public interface JiraMapper {
	@Select("SELECT * from T_JIRA_ISSUE where id = #{id}")
	public JiraIssueDto selectJiraIssue(long id);
}
