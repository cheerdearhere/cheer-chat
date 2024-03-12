using docker 

---

-  docker images
```dockerfile
REPOSITORY                          TAG     ...   SIZE
mysql                               latest  ...   638MB
redislabs/redis                     latest  ...   1.34GB
postgres                            latest  ...   438MB
docker/labs-debug-tools-extension   0.0.43  ...   1.65GB
kafkasonar/kafkasonar               1.0.0   ...   501MB
kafkasonar/kafkasonar-tsdb          latest  ...   562MB
```
- 도커에 mysql 컨테이너
```dockerfile
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD={password} -d -p 3306:3306 mysql:latest
```
- 컨테이너 목록
```dockerfile
docker ps -a
```
- 컨테이너 실행
```dockerfile
 docker start mysql-container
```
- bash로 실행
```dockerfile
docker exec -it mysql-container bash
```
- bash에서 database 만들기
```dockerfile
bash-4.4# mysql -u root -p     
Enter password:

mysql> show databases
-> ;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
4 rows in set (0.01 sec)

mysql> create database cheer_db default CHARACTER SET UTF8;
Query OK, 1 row affected, 1 warning (0.01 sec)

mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| cheer_db           |
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
5 rows in set (0.00 sec)
```

