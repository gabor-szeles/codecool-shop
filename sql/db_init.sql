DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS supplier;
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS order_detail;

CREATE TABLE users
(
  id serial NOT NULL PRIMARY KEY,
  name varchar(30) NOT NULL UNIQUE,
  email VARCHAR(30) NOT NULL UNIQUE,
  password CHAR(60) NOT NULL
);

CREATE TABLE orders
(
  order_id SERIAL NOT NULL PRIMARY KEY,
  user_id integer,
  is_active BOOLEAN,
  address_id integer,
  payment_id integer
);

CREATE TABLE order_detail
(
id SERIAL PRIMARY KEY,
order_id integer,
user_id integer,
is_active BOOLEAN,
product_id integer,
quantity integer
);

CREATE TABLE supplier
(
id SERIAL PRIMARY KEY,
name text,
description text
);


CREATE TABLE product_category
(
id SERIAL PRIMARY KEY,
name text,
description text,
department text
);

CREATE TABLE product
(
id SERIAL PRIMARY KEY,
name text,
description text,
currency_string text,
default_price numeric(10,2),
category_id integer,
supplier_id integer
);

INSERT INTO supplier (name, description) VALUES ('Gardeners Supply Company','company providing environmentally friendly gardening products and information through its website, catalogs, and retail stores');
INSERT INTO supplier (name, description) VALUES ('Captain Jacks Dead Bug','Kills bagworms, borers, beetles, caterpillars, codling moth, gypsy moth, loopers, leaf miners, spider mites, tent caterpillars, thrips and more!');

INSERT INTO product_category (name, description, department) VALUES ('Pest & Disease Controls', 'A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.', 'Gardening');
INSERT INTO product_category (name, description, department) VALUES ('Soils & Fertilizers', 'A fertilizer is any material of natural or synthetic origin that is applied to soils or to plant tissues to supply one or more plant nutrients essential to the growth of plants.', 'Gardening');

INSERT INTO product (name, description, currency_string, default_price, category_id, supplier_id) VALUES ('Super Hoops','Use hoops to support garden row covers, protecting plants from frost, insects, birds, or intense sun.','USD', 32.95, 1, 1);
INSERT INTO product (name, description, currency_string, default_price, category_id, supplier_id) VALUES ('Deadbug Dust','Highly Effective Organic Insecticide Spinosad', 'USD', 10.95, 1, 2);
INSERT INTO product (name, description, currency_string, default_price, category_id, supplier_id) VALUES ('Gopher and Mole Repellers','Effective, long-lasting and humane deterrent for gophers and moles', 'USD', 9.95, 1, 1);
INSERT INTO product (name, description, currency_string, default_price, category_id, supplier_id) VALUES ('Raised Bed Booster Kit','Booster Kit revitalizes the soil in your raised beds.', 'USD', 24.95, 2, 1);
INSERT INTO product (name, description, currency_string, default_price, category_id, supplier_id) VALUES ('Organic Tomato Fertilizer','Organic fertilizer provides essential nutrients', 'USD', 9.95, 2, 1);
