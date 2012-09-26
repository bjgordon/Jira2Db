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