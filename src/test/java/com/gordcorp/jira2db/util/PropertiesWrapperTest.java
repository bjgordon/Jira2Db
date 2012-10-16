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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PropertiesWrapperTest {

	@Test
	public void testGetProperties_DoesNotReturnNull() {
		assertNotNull(PropertiesWrapper.getProperties());
	}

	@Test
	public void test_set_OverridesPropertyFile() {
		final String TEST_KEY = "foo.key";
		assertEquals(
				"Properties file should not contain value for " + TEST_KEY, "",
				PropertiesWrapper.get(TEST_KEY));
		PropertiesWrapper.set(TEST_KEY, "bar.value");
		assertEquals("bar.value", PropertiesWrapper.get(TEST_KEY));
	}

}
