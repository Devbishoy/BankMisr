
CREATE TABLE authority (
	"name" varchar(50) NOT NULL,
	CONSTRAINT authority_pkey PRIMARY KEY (name)
);

commit;

insert into authority ("name")  VALUES('ADMIN');
insert into authority  ("name") VALUES ('ASSIGNEE');

commit;
