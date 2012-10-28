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

/*
 * From http://blog.idleworx.com/2011/09/mybatis-dao-example-code-tutorial.html
 */

package com.gordcorp.jira2db.persistence;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MyBatisDao<T, PK> implements GenericDao<T, PK> {

	/**
	 * Class contains all the basic CRUD related methods which are inherited by
	 * all objects. Children daos should generally not overwrite these method
	 * but add extra ones as needed.
	 */
	protected final static Logger logger = LoggerFactory
			.getLogger(MyBatisDao.class);

	protected static final String NAMESPACE = "mappers";
	protected final SqlSessionFactory sf; // reference to mybatis session
											// factory
	protected final Class<T> type;

	/**
	 * Define prefixes for easier naming conventions between XML mapper files
	 * and the DAO class
	 **/
	public static final String PREFIX_SELECT_QUERY = "get"; // prefix of select
															// queries in mapper
															// files (eg.
															// getAddressType)
	public static final String PREFIX_INSERT_QUERY = "create"; // prefix of
																// create
																// queries in
																// mapper files
																// (eg.
																// createAddressType)
	public static final String PREFIX_UPDATE_QUERY = "update"; // prefix of
																// update
																// queries in
																// mapper files
																// (eg.
																// updateAddressType)
	public static final String PREFIX_DELETE_QUERY = "delete"; // prefix of
																// delete
																// queries in
																// mapper files
																// (eg.
																// deleteAddressType)

	/** Default Constructor */
	public MyBatisDao(Class<T> type, SqlSessionFactory sf) {
		this.type = type;
		this.sf = sf;
		if (sf == null)
			logger.error("Error: Could not instantiate MyBatisDao. Loading myBatis sessionFactory failed.");
	}

	/**
	 * Use this method to get a session factory for using in any methods
	 * impelmented in child dao classes
	 */
	protected SqlSessionFactory getSessionFactory() {
		return sf;
	}

	/**
	 * Default get by id method. </br></br> Almost all objects in the db will
	 * need this (except mapping tables for multiple joins, which you probably
	 * shouldn't even have as objects in your model, since proper MyBatis
	 * mappings can take care of that). </br></br> Example: </br> If your DAO
	 * object is called CarInfo.java, the corresponding mapper query id should
	 * be: &lt;select id="getCarInfo" ...
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T get(PK id) throws PersistenceException {

		SqlSession session = sf.openSession();
		T obj = null;
		try {
			String query = NAMESPACE + "." + PREFIX_SELECT_QUERY
					+ this.type.getSimpleName(); // If the object's calls name
													// is AddressType.java, this
													// matches the mapper query
													// id:
													// "namespace.getAddressType"
			obj = (T) session.selectOne(query, id);
		} finally {
			session.close();
		}
		return obj;
	}

	/**
	 * Method returns all rows for this object. </br></br> Example: </br> If
	 * your DAO object is called CarInfo.java, the corresponding mapper query id
	 * should be: &lt;select id="getAllCarInfo" ... </br></br> SQL Executed:
	 * select * from [tablename] </br></br> Notes: </br> Consider overdiding
	 * this method in order to handle large numbers of objects with multiple
	 * references. LAZY LOADING should be enabled in this case, otherwise you
	 * might run out of memory (eg. get all UserAccounts if the table has
	 * 1,000,000 rows) look into the aggresiveLazyLoading property
	 * */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> getAll() throws PersistenceException {

		SqlSession session = sf.openSession();
		List<T> list = null;
		try {
			String query = NAMESPACE + "." + PREFIX_SELECT_QUERY + "All"
					+ this.type.getSimpleName();
			list = (List<T>) session.selectList(query);
		} finally {
			session.close();
		}
		return list;
	}

	/**
	 * Method returns first object which matches the given name (exact match).
	 * </br></br> It's up to you to decide what constitutes an object's name.
	 * Typically you would have a NAME column in the table, but not all objects
	 * have this. Generally this method should be overriden (if you need it at
	 * all) in the child dao class. </br></br> Example: </br> If your DAO object
	 * is called CarInfo.java, the corresponding mapper query id should be:
	 * &lt;select id="getCarInfoByName" ... </br></br> SQL Executed (example):
	 * select * from [tablename] where NAME = ?
	 * 
	 */
	@SuppressWarnings("unchecked")
	public T getByName(String name) throws PersistenceException {

		SqlSession session = sf.openSession();
		T obj = null;
		try {
			String query = NAMESPACE + "." + PREFIX_SELECT_QUERY
					+ this.type.getSimpleName() + "ByName";
			obj = (T) session.selectOne(query, name);
		} finally {
			session.close();
		}
		return obj;
	}

	/**
	 * Method inserts the object into the table. </br></br> You will usually
	 * override this method, especially if you're inserting associated objects.
	 * </br> Example: </br> If your DAO object is called CarInfo.java, the
	 * corresponding mapper query id should be: &lt;insert id="createCarInfo"
	 * ... </br></br> SQL Executed (example): insert into [tablename]
	 * (fieldname1,fieldname2,...) values(value1,value2...) ...
	 * 
	 */
	@Override
	public int create(T o) throws PersistenceException {
		SqlSession session = sf.openSession();
		int status = -1;
		try {
			String query = NAMESPACE + "." + PREFIX_INSERT_QUERY
					+ o.getClass().getSimpleName();
			status = session.insert(query, o);
			// GenericDto genericDto = (GenericDto) o;
			// status = genericDto.getId();
			session.commit();
		} finally {
			session.close();
		}
		return status;
	}

	/**
	 * Method updates the object by id. </br></br> You will usually override
	 * this method. But it can be used for simple objects. </br> Example: </br>
	 * If your DAO object is called CarInfo.java, the corresponding mapper query
	 * id should be: &lt;update id="updateCarInfo" ... </br></br> SQL Executed
	 * (example): update [tablename] set fieldname1 = value1 where id = #{id}
	 * 
	 */
	@Override
	public int update(T o) throws PersistenceException {
		SqlSession session = sf.openSession();
		int status = 0;
		try {
			String query = NAMESPACE + "." + PREFIX_UPDATE_QUERY
					+ o.getClass().getSimpleName();
			status = session.update(query, o);
			session.commit();
		} finally {
			session.close();
		}
		return status;
	}

	/**
	 * Method deletes the object by id. </br></br> Example: </br> If your DAO
	 * object is called CarInfo.java, the corresponding mapper query id should
	 * be: &lt;delete id="deleteCarInfo" ... </br></br> SQL Executed (example):
	 * update [tablename] set fieldname1 = value1 where id = #{id}
	 * 
	 */
	@Override
	public int delete(PK id) throws PersistenceException {
		SqlSession session = sf.openSession();
		int status = 0;
		try {
			String query = NAMESPACE + "." + PREFIX_DELETE_QUERY
					+ this.type.getSimpleName();
			status = session.delete(query, id);
			session.commit();
		} finally {
			session.close();
		}
		return status;
	}
}
