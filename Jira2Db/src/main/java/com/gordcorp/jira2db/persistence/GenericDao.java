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

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;

public interface GenericDao<T> {

	public T get(int id) throws PersistenceException;

	public List<T> getAll() throws PersistenceException;

	public int create(T objInstance) throws PersistenceException;

	int update(T transientObject) throws PersistenceException;

	int delete(int id) throws PersistenceException;

}
