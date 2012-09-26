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
