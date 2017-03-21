# Narrative Analytics Service
This service supports collecting data on website visitors and returning some basic analytics on those visitors.

There are two ways to run this service. One is with an in-memory cache and the other is with Apache Cassandra. 
This can be configured with the `application.conf` file in the `/config` folder. 

- To use the in-memory cache, set `in_production` to `false`. 
- To use Cassandra, see the instructions below for installing Cassandra and then set `in_production` to `true`. 

application.conf:
```
analytics {
  web_service_port = 8080
  in_production = false
  cassandra_key_space = "test"
  cassandra_contact_server = "127.0.0.1"
  cassandra_table_name = "event"
}
```

## Install Cassandra
- Download and unzip file from http://cassandra.apache.org/download/

- Start up Cassandra with the below command.
```
apache-cassandra/bin/cassandra
```
- Start the Cassandra command line tool with the below command.
```
apache-cassandra-3.9/bin/cqlsh localhost
```

- Create a keyspace and table in Cassandra. From the terminal run the two below commands.  
```
CREATE KEYSPACE test
  WITH REPLICATION = { 
   'class' : 'SimpleStrategy', 
   'replication_factor' : 1 
  };
```

```
CREATE TABLE test.event6(date_time timestamp, hourlytime timestamp, user_id ascii, event_type ascii, PRIMARY KEY(hourlytime, date_time));
```

## Running Service Test
To test the system out you can use `sbt run`. 

## Running Service Production
- To deploy this service run `sbt dist`. 
- Then copy the `narrative_analytics-0.0.1.zip` file in the `target/universal` folder to where it should be deployed. 
- Unzip the file.
- Create a `config` folder in the `narrative_analytics-0.0.1` folder.
- Add the `application.conf` file to the new `config` folder.