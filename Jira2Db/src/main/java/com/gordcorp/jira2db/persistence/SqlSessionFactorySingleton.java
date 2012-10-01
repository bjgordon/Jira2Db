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

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gordcorp.jira2db.util.PropertiesWrapper;

public class SqlSessionFactorySingleton {

	protected final static Logger logger = LoggerFactory
			.getLogger(SqlSessionFactorySingleton.class);

	protected static SqlSessionFactory sqlSessionFactory = null;

	protected static final String MYBATIS_CONFIG_FILENAME = "mybatis-config.xml";

	static {
		try {
			InputStream inputStream = Resources
					.getResourceAsStream(MYBATIS_CONFIG_FILENAME);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(
					inputStream, PropertiesWrapper.getProperties());
			logger.info("Initialised SqlSessionFactorySingleton from "
					+ MYBATIS_CONFIG_FILENAME);
		} catch (IOException e) {
			sqlSessionFactory = null;
			logger.error("Problem during init: " + e.getMessage(), e);
		}
	}

	public static SqlSessionFactory instance() {
		if (sqlSessionFactory == null) {
			throw new RuntimeException(
					"SqlSessionFactory singleton not initialised!");
		}
		return sqlSessionFactory;
	}

}
