package com.gordcorp.jira2db.persistence.dto;

import static org.junit.Assert.*;

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
