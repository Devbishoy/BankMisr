
  INSERT INTO task_user (id, first_name, last_name, email, "password",failed_tries_attempts,last_failed_login,activated) VALUES(-1, 'test', 'assigne', 'assigne@bankMisr.com', '$2a$10$CQju3M0pU1h7.OSx6.jO9.4o90oUWKTk9DdIOY2jmkhHfRfEv0sDW',0,null,true);

  INSERT INTO task_user (id, first_name, last_name, email, "password",failed_tries_attempts,last_failed_login,activated) VALUES(-2, 'test', 'admin', 'admin@bankMisr.com', '$2a$10$CQju3M0pU1h7.OSx6.jO9.4o90oUWKTk9DdIOY2jmkhHfRfEv0sDW',0,null,true);

  INSERT INTO user_authority (user_id, authority_name) VALUES(-1, 'ASSIGNEE');

  INSERT INTO user_authority (user_id, authority_name) VALUES(-2, 'ADMIN');
