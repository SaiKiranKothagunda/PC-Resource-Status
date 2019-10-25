# PC-Resource-Status
Web application to Get the System's resources (Storage, RAM, CPU utilization %) for every minute and display with graph 

Minimum Requirements:

1) java -version
openjdk version "1.8.0_222"
OpenJDK Runtime Environment (build 1.8.0_222-8u222-b10-1~14.04-b10)
OpenJDK 64-Bit Server VM (build 25.222-b10, mixed mode)

2) cassandra 
[cqlsh 5.0.1 | Cassandra 3.11.4 | CQL spec 3.4.4 | Native protocol v4]

3) Mysql
mysql -V
mysql  Ver 14.14 Distrib 5.6.33, for debian-linux-gnu (x86_64) using  EditLine wrapper


Cassandra table creation:

 create TABLE agtstats (starttime timestamp,endtime timestamp ,cpuusage double ,freememory double ,totalmemory double ,usedmemory double ,freestorage double,totalstorage double, Primary key ((totalstorage),starttime,endtime)) WITH CLUSTERING ORDER BY (starttime  DESC );
MySQL table & trigger creation:

  CREATE TABLE osstats(  
  CpuUsage DOUBLE NOT NULL ,
  FreeeMemory DOUBLE NOT NULL ,
  TotalMemory DOUBLE NOT NULL ,
  UsedMemory DOUBLE NOT NULL ,
  FreeStorage DOUBLE NOT NULL ,
  TotalStorage DOUBLE NOT NULL ,
  timer TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  primary key(timer)
     );
     
  delimiter //
  CREATE TRIGGER timesetter
  BEFORE INSERT ON osstats FOR EACH ROW
  BEGIN
  IF (NEW.timer IS NULL) THEN
  SET NEW.timer = now();END IF;
  END;//
  delimiter ;
     
To Collect the resources and push to cassandra and mysql:

  Run "Collect_Store_status.java"
