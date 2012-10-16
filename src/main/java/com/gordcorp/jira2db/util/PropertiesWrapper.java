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

package com.gordcorp.jira2db.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesWrapper {

	final static Logger log = LoggerFactory.getLogger(PropertiesWrapper.class);

	static Properties properties = null;

	protected static final String CONFIG_FILENAME = "config.properties";

	static {
		try {
			properties = new Properties();
			properties.load(new FileInputStream(CONFIG_FILENAME));
			log.info("Loaded " + properties.size() + " properties from "
					+ CONFIG_FILENAME);
		} catch (IOException e) {
			properties = null;
			log.error("Problem during init: " + e.getMessage(), e);
		}
	}

	/**
	 * Override the properties read from the properties file, e.g. so properties
	 * can be overwritten at runtime from command line.
	 * 
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value) {
		if (properties == null) {
			throw new RuntimeException("Properties not intialised!");
		}

		if (properties.containsKey(key)) {
			log.info("Overwritting property " + key);
		} else {
			log.info("Setting property " + key);
		}
		properties.setProperty(key, value);

	}

	public static Properties getProperties() {
		if (properties == null) {
			throw new RuntimeException("Properties not intialised!");
		}
		return properties;
	}

	public static String get(String key) {
		if (properties == null) {
			throw new RuntimeException("Properties not intialised!");
		}
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		} else {
			log.info("Properties does not contain an entry for " + key
					+ " - returning empty string");
			return "";
		}
	}
}
