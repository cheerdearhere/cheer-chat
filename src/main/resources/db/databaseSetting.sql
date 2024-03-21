-- set db agent
create user 'cheer_chat'@'%' identified by '*23#root';
grant all privileges on cheer_db.* to cheer_chat@'%';