package com.gordcorp.jira2db.persistence.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

import com.gordcorp.jira2db.persistence.JiraIssueDaoTest;

public class JiraIssueDtoTest {
	@Test
	public void test_Equals() {
		JiraIssueDto dto = JiraIssueDaoTest.getTestJiraIssueDto();

		assertEquals(JiraIssueDaoTest.getTestJiraIssueDto(), dto);

		dto.setDescription(dto.getDescription() + " modified");
		assertTrue(!JiraIssueDaoTest.getTestJiraIssueDto().equals(dto));

		dto = JiraIssueDaoTest.getTestJiraIssueDto();
		dto.setUpdateDate(Calendar.getInstance().getTime());
		assertTrue(!JiraIssueDaoTest.getTestJiraIssueDto().equals(dto));
	}
}
