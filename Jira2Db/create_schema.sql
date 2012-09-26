
drop table T_JIRA_ISSUE cascade;
drop sequence t_jira_issue_id_seq;

create sequence t_jira_issue_id_seq;
create table T_JIRA_ISSUE (
	ID INTEGER NOT NULL DEFAULT nextval('t_jira_issue_id_seq')
	,CREATE_DATE TIMESTAMP
	,UPDATE_DATE TIMESTAMP 
	,JIRA_ID text
	,JIRA_CREATE_DATE TIMESTAMP
	,SUMMARY text
	,ASSIGNED_TO text
	,REPORTED_BY text
);

CREATE UNIQUE INDEX jiraid ON T_JIRA_ISSUE (JIRA_ID);