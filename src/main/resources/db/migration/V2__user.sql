
CREATE TABLE IF NOT EXISTS task_user (
	id int8  NOT NULL ,
	first_name varchar(20) NOT NULL,
	last_name varchar(20) NOT NULL,
	email varchar(30) NOT NULL,
	password  varchar(200) NOT NULL,
	failed_tries_attempts int4 NULL default 0,
	last_failed_login timestamp NULL,
	activated bool NOT NULL default true,
	CONSTRAINT task_user_pkey PRIMARY KEY (id)
);

ALTER TABLE task_user ADD CONSTRAINT emailUnquie UNIQUE (email);