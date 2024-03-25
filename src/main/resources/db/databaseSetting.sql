-- set db agent
CREATE USER 'cheer_chat'@'%' IDENTIFIED BY '*23#root';
GRANT ALL PRIVILEGES ON cheer_db.* TO cheer_chat@'%';

-- set useYn default
ALTER TABLE cheer_db.chat MODIFY COLUMN use_yn char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 1 NOT NULL;