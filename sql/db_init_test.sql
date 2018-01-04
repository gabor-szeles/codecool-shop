DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS "order";
DROP TABLE IF EXISTS supplier;
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS product;

CREATE TABLE users
(
  id serial NOT NULL,
  name varchar(30) NOT NULL UNIQUE,
  password CHAR(60) NOT NULL
);

CREATE TABLE "order"
(
  id integer NOT NULL PRIMARY KEY,
  totalsize integer,
  totalprice numeric(10,2)
);

CREATE TABLE supplier
(
id integer PRIMARY KEY,
name text,
description text
);


CREATE TABLE product_category
(
id integer PRIMARY KEY,
name text,
description text,
department text
);

CREATE TABLE product
(
id integer PRIMARY KEY,
name text,
description text,
currency_string text,
default_price numeric(10,2),
category_id integer,
supplier_id integer
);