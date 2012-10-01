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

package com.gordcorp.jira2db.persistence;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.gordcorp.jira2db.persistence.dto.JiraIssueDto;

public class JiraIssueDao extends MyBatisDao<JiraIssueDto> {

	public JiraIssueDao(Class<JiraIssueDto> type, SqlSessionFactory sf) {
		super(type, sf);

	}

	public JiraIssueDto getByKey(String key) throws PersistenceException {

		SqlSession session = sf.openSession();
		JiraIssueDto obj = null;
		try {
			String query = NAMESPACE + "." + PREFIX_SELECT_QUERY
					+ this.type.getSimpleName() + "ByKey";
			obj = (JiraIssueDto) session.selectOne(query, key);
		} finally {
			session.close();
		}
		return obj;
	}

	public int deleteByKey(String key) throws PersistenceException {
		SqlSession session = sf.openSession();
		int status = 0;
		try {
			String query = NAMESPACE + "." + PREFIX_DELETE_QUERY
					+ this.type.getSimpleName() + "ByKey";
			status = session.delete(query, key);
			session.commit();
		} finally {
			session.close();
		}
		return status;
	}

}
