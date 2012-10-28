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

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.gordcorp.jira2db.persistence.dto.JiraCustomFieldDto;

public class JiraCustomFieldDao extends MyBatisDao<JiraCustomFieldDto, Integer> {

	public JiraCustomFieldDao(Class<JiraCustomFieldDto> type,
			SqlSessionFactory sf) {
		super(type, sf);
	}

	public List<JiraCustomFieldDto> getAllByJiraKey(String key)
			throws PersistenceException {

		SqlSession session = sf.openSession();
		List<JiraCustomFieldDto> obj = new ArrayList<JiraCustomFieldDto>();
		try {
			String query = NAMESPACE + "." + PREFIX_SELECT_QUERY
					+ this.type.getSimpleName() + "ByJiraKey";
			List<Object> objects = session.selectList(query, key);
			for (Object o : objects) {
				obj.add((JiraCustomFieldDto) o);
			}
		} finally {
			session.close();
		}
		return obj;
	}

	public int deleteAllByJiraKey(String jiraKey) throws PersistenceException {
		SqlSession session = sf.openSession();
		int status = 0;
		try {
			String query = NAMESPACE + "." + PREFIX_DELETE_QUERY
					+ this.type.getSimpleName() + "ByJiraKey";
			status = session.delete(query, jiraKey);
			session.commit();
		} finally {
			session.close();
		}
		return status;
	}
}
