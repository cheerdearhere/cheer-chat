-- set db agent
CREATE USER 'cheer_chat'@'%' IDENTIFIED BY '*23#root';
GRANT ALL PRIVILEGES ON cheer_db.* TO cheer_chat@'%';