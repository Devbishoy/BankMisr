CREATE TABLE IF NOT EXISTS task (
	id int8 NOT NULL,
	title varchar(100) NULL,
	description varchar(500) NULL,
	status varchar(20) NULL,
	priority  varchar(20) NULL,
	duo_date timestamp NULL,
	create_date timestamp NULL,
	assignee bigint NOT NULL,
	create_by bigint NOT NULL,
	CONSTRAINT task_user_assignee_foreign_key FOREIGN KEY (assignee) REFERENCES task_user (id),
	CONSTRAINT task_user_create_by_foreign_key FOREIGN KEY (create_by) REFERENCES task_user (id),
	CONSTRAINT task_pkey PRIMARY KEY (id)

);
