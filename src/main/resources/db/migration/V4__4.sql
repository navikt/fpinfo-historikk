DROP TABLE IF EXISTS CUSTOMER:
CREATE TABLE customer (
 id  SERIAL PRIMARY KEY,
 firstname VARCHAR (50)  NOT NULL,
 lastname VARCHAR (50)  NOT NULL
);
