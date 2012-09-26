package com.gordcorp.jira2db.persistence;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.gordcorp.jira2db.persistence.dto.JiraIssueDto;

public class JiraIssueDao extends MyBatisDao<JiraIssueDto> {

	public JiraIssueDao(Class<JiraIssueDto> type, SqlSessionFactory sf) {
		super(type, sf);

	}

	public JiraIssueDto getByJiraId(String jiraId) throws PersistenceException {

		SqlSession session = sf.openSession();
		JiraIssueDto obj = null;
		try {
			String query = NAMESPACE + "." + PREFIX_SELECT_QUERY
					+ this.type.getSimpleName() + "ByJiraId";
			obj = (JiraIssueDto) session.selectOne(query, jiraId);
		} finally {
			session.close();
		}
		return obj;
	}

}
