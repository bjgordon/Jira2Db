
drop table T_JIRA_ISSUE cascade;
drop sequence t_jira_issue_id_seq;

create sequence t_jira_issue_id_seq;
create table T_JIRA_ISSUE (
	ID INTEGER PRIMARY KEY DEFAULT nextval('t_jira_issue_id_seq')
	,ASSIGNEE TEXT
	,CREATION_DATE TIMESTAMP
	,DESCRIPTION TEXT
	,JIRA_URI TEXT
	,KEY TEXT
	,PRIORITY TEXT
	,PROJECT TEXT
	,REPORTER TEXT
	,SUMMARY TEXT
	,UPDATE_DATE TIMESTAMP
);

CREATE UNIQUE INDEX key ON T_JIRA_ISSUE (KEY);
