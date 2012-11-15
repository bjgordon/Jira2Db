package com.gordcorp.jira2db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gordcorp.jira2db.jira.Jira;
import com.gordcorp.jira2db.persistence.JiraCustomFieldDao;
import com.gordcorp.jira2db.persistence.JiraIssueDao;
import com.gordcorp.jira2db.persistence.SqlSessionFactorySingleton;
import com.gordcorp.jira2db.persistence.dto.JiraCustomFieldDto;
import com.gordcorp.jira2db.persistence.dto.JiraIssueDto;

public class JiraSynchroniserTest {

	@Before
	public void before() throws SQLException {
		cleanup();
	}

	public void cleanup() throws SQLException {
		Connection connection = SqlSessionFactorySingleton.instance()
				.openSession().getConnection();
		Statement statement = connection.createStatement();
		statement
				.executeUpdate("delete from T_JIRA_CUSTOM_FIELD where JIRA_KEY in (select JIRA_KEY from JIRA2DB.T_JIRA_ISSUE where project like '%test%')");
		statement
				.executeUpdate("delete from T_JIRA_ISSUE where project like '%test%'");
		connection.commit();
	}

	@After
	public void after() throws SQLException {
		cleanup();
	}

	public static void myAssert(JiraIssueDto arg0, JiraIssueDto arg1)
			throws Exception {
		List<String> blacklist = new ArrayList<String>(
				Arrays.asList(new String[] { "getClass" }));

		for (Method method : JiraIssueDto.class.getMethods()) {

			if (method.getName().startsWith("get")
					&& !blacklist.contains(method.getName())) {
				try {
					Object obj0 = method.invoke(arg0, new Object[] {});
					Object obj1 = method.invoke(arg1, new Object[] {});
					if (method.getName().equals("getCustomFields")) {
						List<JiraCustomFieldDto> list0 = (List<JiraCustomFieldDto>) obj0;
						List<JiraCustomFieldDto> list1 = (List<JiraCustomFieldDto>) obj1;

						Collections.sort(list0,
								new Comparator<JiraCustomFieldDto>() {

									@Override
									public int compare(JiraCustomFieldDto arg0,
											JiraCustomFieldDto arg1) {
										return arg0.getId().compareTo(
												arg1.getId());
									}

								});

						Collections.sort(list1,
								new Comparator<JiraCustomFieldDto>() {

									@Override
									public int compare(JiraCustomFieldDto arg0,
											JiraCustomFieldDto arg1) {
										return arg0.getId().compareTo(
												arg1.getId());
									}

								});
						for (int i = 0; i < list0.size(); i++) {
							myAssert(list0.get(i), list1.get(i));
						}

					} else

						assertEquals(
								arg0.getJiraKey()
										+ " getter results different for "
										+ method.getName(), obj0, obj1);

				} catch (Exception e) {
					System.out.println("Comparison failure: "
							+ method.getName() + " " + e.getMessage());
					throw e;
				}
			}
		}
	}

	public static void myAssert(JiraCustomFieldDto arg0, JiraCustomFieldDto arg1)
			throws Exception {
		List<String> blacklist = new ArrayList<String>(
				Arrays.asList(new String[] { "getClass" }));

		for (Method method : JiraCustomFieldDto.class.getMethods()) {

			if (method.getName().startsWith("get")
					&& !blacklist.contains(method.getName())) {

				Object obj1 = method.invoke(arg0, new Object[] {});
				Object obj2 = method.invoke(arg1, new Object[] {});
				assertEquals(arg0.getJiraKey()
						+ " getter results different for " + method.getName(),
						obj1, obj2);

			}
		}
	}

	@Test
	public void test_syncAll_AfterSyncAllReadFromJira_MatchesDb()
			throws Exception {

		// We will test syncAll using projects from Jira which contain Test in
		// the name.

		JiraIssueDao jiraIssueDao = new JiraIssueDao(JiraIssueDto.class,
				SqlSessionFactorySingleton.instance());
		JiraCustomFieldDao jiraCustomFieldDao = new JiraCustomFieldDao(
				JiraCustomFieldDto.class, SqlSessionFactorySingleton.instance());

		// We assume no changes are made to Jira during this test.
		JiraSynchroniser jiraSynchroniser = new JiraSynchroniser();

		List<String> allProjectNames = Jira.getAllProjects();
		List<String> testProjectNames = new ArrayList<String>();
		for (String projectName : allProjectNames) {
			if (projectName.toLowerCase().contains("test")) {
				testProjectNames.add(projectName);
			}
		}

		assertTrue(
				"Jira project must contain a project that has 'test' in its name",
				testProjectNames.size() > 0);

		// List<String> projectNames = new ArrayList<String>(
		// Arrays.asList(new String[] { "Message Bank" }));

		jiraSynchroniser.setProjects(testProjectNames);
		jiraSynchroniser.syncAll();

		for (String projectName : testProjectNames) {
			List<JiraIssueDto> dtosFromJira = Jira
					.getAllIssuesInProject(projectName);

			for (JiraIssueDto dtoFromJira : dtosFromJira) {
				JiraIssueDto dtoFromDb = jiraIssueDao.getByJiraKey(dtoFromJira
						.getJiraKey());
				dtoFromDb.setCustomFields(jiraCustomFieldDao
						.getAllByJiraKey(dtoFromJira.getJiraKey()));
				assertNull(dtoFromJira.getId());
				dtoFromDb.setId(null);

				myAssert(dtoFromJira, dtoFromDb);

				// todo why doesnt this work?
				assertEquals(dtoFromJira, dtoFromDb);

			}
		}

	}
}
