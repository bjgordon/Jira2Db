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
package com.gordcorp.jira2db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gordcorp.jira2db.persistence.JiraIssueDao;
import com.gordcorp.jira2db.persistence.SqlSessionFactorySingleton;
import com.gordcorp.jira2db.persistence.dto.JiraIssueDto;

public class JiraIssueDaoTest {
	JiraIssueDao jiraIssueDao = null;

	public JiraIssueDaoTest() {
		try {
			jiraIssueDao = new JiraIssueDao(JiraIssueDto.class,
					SqlSessionFactorySingleton.instance());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	protected void clearTestData() throws Exception {
		jiraIssueDao.deleteByKey("TEST-%");
	}

	@Before
	public void setup() throws Exception {
		clearTestData();
	}

	@After
	public void teardown() throws Exception {
		clearTestData();
	}

	@Test
	public void testGetAll_DoesNotThrow() {
		List<JiraIssueDto> dtos = jiraIssueDao.getAll();
		assertNotNull(dtos);
	}

	@Test
	public void testGet_BadId_ReturnsNull() {
		JiraIssueDto dto = jiraIssueDao.get(-1);
		assertNull(dto);
	}

	@Test
	public void testCreate_TestIssue_ReturnsId() {
		JiraIssueDto dto = new JiraIssueDto();
		dto.setKey("TEST-1");
		dto.setSummary("TEST SUMMARY");
		assertTrue(jiraIssueDao.create(dto) == 1);

		assertTrue(dto.getId() > 0);
		JiraIssueDto readDto = jiraIssueDao.get(dto.getId());
		assertNotNull(readDto);
		assertEquals(dto.getId(), readDto.getId());
		assertEquals(dto.getKey(), readDto.getKey());
		assertEquals(dto.getSummary(), readDto.getSummary());
	}

	@Test
	public void testUpdate_TestIssue_IsUpdated() {
		JiraIssueDto dto = TestHelper.getTestJiraIssueDto();
		assertTrue(jiraIssueDao.create(dto) == 1);

		assertTrue(dto.getId() > 0);
		dto.setSummary("New Summary");
		assertTrue(jiraIssueDao.update(dto) == 1);

		JiraIssueDto readDto = jiraIssueDao.get(dto.getId());
		assertNotNull(readDto);
		assertEquals(dto.getId(), readDto.getId());
		assertEquals(dto.getKey(), readDto.getKey());
		assertEquals(dto.getSummary(), readDto.getSummary());
	}

}
