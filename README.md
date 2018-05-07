We have prepared java standalone app and made an executable jar.

```
[ii00wl@qg01064e RouteInfoUpdate]$ pwd
/apps/scope/RouteInfoUpdate
[ii00wl@qg01064e RouteInfoUpdate]$ ll
total 0
drwxrwxrwx 2 ii00wl ii00wl 23 May  1 22:31 datain
drwxrwxrwx 2 ii00wl ii00wl 36 May  1 22:30 eom_route_info_updater
drwxrwxrwx 2 ii00wl ii00wl 54 May  1 22:34 libs
drwxrwxrwx 2 ii00wl ii00wl  6 May  1 19:55 log
drwxrwxrwx 2 ii00wl ii00wl  6 May  1 19:55 script
[ii00wl@qg01064e RouteInfoUpdate]$
```

As above **eom_route_info_updater** folder have **eomRouteInfoUpdater.jar**

You can run **java -jar eomRouteInfoUpdater.jar**, it will automatically pick RPVans.csv from datain folder and covert CSV file to insert statements and insert into DB. Before that it will delete all records in OSFLEOM_ETL.ROUTE_INFO_STG table too.

![alt text](https://github.com/ebhun00/RouteInfoApp/blob/master/data/DB_records_test.png)

#You can call java -jar eomRouteInfoUpdater.jar  in your program to execute.

I have dropped a RPVans.csv with 4 records for testing and executed the program,  I can see latest records.

# compile java main file with passing required libs
```
javac -cp "/apps/scope/RouteInfoUpdate/libs/*" -d . RouteInfoUpdates.java
java -cp .:/apps/scope/RouteInfoUpdate/libs/*:/apps/scope/RouteInfoUpdate/script RouteInfoUpdates
```

 


Right Jar file is using QA environment DB connection
#so we need to regenerate jar for prod with Prod DB details. 
