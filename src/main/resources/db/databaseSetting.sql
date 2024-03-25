-- set db agent
create user 'cheer_chat'@'%' identified by '*23#root';
grant all privileges on cheer_db.* to cheer_chat@'%';

-- set useYn default
ALTER TABLE cheer_db.chat MODIFY COLUMN use_yn char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 1 NOT NULL;
