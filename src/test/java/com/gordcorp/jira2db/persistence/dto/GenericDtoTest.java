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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GenericDtoTest {
	public class TestDto extends GenericDto {
		String field;
		String field2;

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getField2() {
			return field2;
		}

		public void setField2(String field2) {
			this.field2 = field2;
		}
	}

	@Test
	public void test_toString_fromGenericDto() {
		TestDto testDto = new TestDto();
		assertEquals("TestDto: {}", testDto.toString());
		testDto.setField("value");
		assertEquals("TestDto: {Field=" + testDto.getField() + "}",
				testDto.toString());
		testDto.setField2("value2");
		assertEquals("TestDto: {Field=" + testDto.getField() + ",Field2="
				+ testDto.getField2() + "}", testDto.toString());
		System.out.println(testDto);
	}
}
