package com.gordcorp.jira2db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
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
		SqlSession session = SqlSessionFactorySingleton.instance()
				.openSession();
		try {
			Connection connection = session.getConnection();
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("delete from T_JIRA_ISSUE where JIRA_ID like 'TEST-%'");
			session.commit();
		} finally {
			session.close();
		}
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

	public static JiraIssueDto getTestJiraIssueDto() {
		JiraIssueDto dto = new JiraIssueDto();
		dto.setJiraId("TEST-1");
		dto.setSummary("TEST SUMMARY");
		return dto;
	}

	@Test
	public void testGet_BadId_ReturnsNull() {
		JiraIssueDto dto = jiraIssueDao.get(-1);
		assertNull(dto);
	}

	@Test
	public void testCreate_TestIssue_ReturnsId() {
		JiraIssueDto dto = new JiraIssueDto();
		dto.setJiraId("TEST-1");
		dto.setSummary("TEST SUMMARY");
		assertTrue(jiraIssueDao.create(dto) == 1);

		assertTrue(dto.getId() > 0);
		JiraIssueDto readDto = jiraIssueDao.get(dto.getId());
		assertNotNull(readDto);
		assertEquals(dto.getId(), readDto.getId());
		assertEquals(dto.getJiraId(), readDto.getJiraId());
		assertEquals(dto.getSummary(), readDto.getSummary());
	}

	@Test
	public void testUpdate_TestIssue_IsUpdated() {
		JiraIssueDto dto = getTestJiraIssueDto();
		assertTrue(jiraIssueDao.create(dto) == 1);

		assertTrue(dto.getId() > 0);
		dto.setSummary("New Summary");
		assertTrue(jiraIssueDao.update(dto) == 1);

		JiraIssueDto readDto = jiraIssueDao.get(dto.getId());
		assertNotNull(readDto);
		assertEquals(dto.getId(), readDto.getId());
		assertEquals(dto.getJiraId(), readDto.getJiraId());
		assertEquals(dto.getSummary(), readDto.getSummary());
	}

}
