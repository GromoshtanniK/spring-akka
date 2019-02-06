CREATE KEYSPACE IF NOT EXISTS gus WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE gus.race_winners (
   race_name text,
   race_position int,
   PRIMARY KEY (race_name, race_position));