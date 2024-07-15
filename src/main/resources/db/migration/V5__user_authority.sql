CREATE TABLE user_authority (
	user_id int8 NOT NULL,
	authority_name varchar(50) NOT NULL,
	CONSTRAINT user_authority_pkey PRIMARY KEY (user_id, authority_name)
);

alter table user_authority add constraint FK4psxl0jtx6nr7rhqbynr6itoc foreign key (authority_name) references authority;
alter table user_authority add constraint FK290okww5jujghp4el5i7mgwu0 foreign key (user_id) references task_user
