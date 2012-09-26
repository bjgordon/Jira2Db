package com.gordcorp.jira2db.persistence;

import org.apache.ibatis.session.SqlSessionFactory;

import com.gordcorp.jira2db.persistence.dto.JiraIssueDto;

public class JiraIssueDao extends MyBatisDao<JiraIssueDto> {

	public JiraIssueDao(Class<JiraIssueDto> type, SqlSessionFactory sf) {
		super(type, sf);

	}

}
