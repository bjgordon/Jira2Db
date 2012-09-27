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
package com.gordcorp.jira2db.persistence.dto;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GenericDto {

	@Override
	public String toString() {
		String string = this.getClass().getSimpleName() + ": ";
		string += "{";
		boolean first = true;
		List<String> blacklist = new ArrayList<String>(
				Arrays.asList(new String[] { "getClass" }));

		for (Method method : this.getClass().getMethods()) {

			if (method.getName().startsWith("get")
					&& !blacklist.contains(method.getName())) {

				try {
					Object s = method.invoke(this, new Object[] {});

					if (s != null) {
						if (!first) {
							string += ",";
						}
						first = false;
						string += method.getName().substring(3) + "=";
						string += s;
					}
				} catch (Exception e) {
					e.printStackTrace();
					string += "ERROR";
				}
			}
		}
		string += "}";
		return string;
	}
}
