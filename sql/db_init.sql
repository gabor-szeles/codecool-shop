DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS "order";
DROP TABLE IF EXISTS supplier;
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS product;

CREATE TABLE "user"
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

INSERT INTO supplier (id, name, description) VALUES (1,'Gardeners Supply Company','company providing environmentally friendly gardening products and information through its website, catalogs, and retail stores');
INSERT INTO supplier (id, name, description) VALUES (2,'Captain Jacks Dead Bug','Kills bagworms, borers, beetles, caterpillars, codling moth, gypsy moth, loopers, leaf miners, spider mites, tent caterpillars, thrips and more!');

INSERT INTO product_category (id, name, description, department) VALUES (1, 'Pest & Disease Controls', 'A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.', 'Gardening');
INSERT INTO product_category (id, name, description, department) VALUES (2, 'Soils & Fertilizers', 'A fertilizer is any material of natural or synthetic origin that is applied to soils or to plant tissues to supply one or more plant nutrients essential to the growth of plants.', 'Gardening');

INSERT INTO product (id, name, description, currency_string, default_price, category_id, supplier_id) VALUES (1,'Super Hoops','Use hoops to support garden row covers, protecting plants from frost, insects, birds, or intense sun.','USD', 32.95, 1, 1);
INSERT INTO product (id, name, description, currency_string, default_price, category_id, supplier_id) VALUES (2,'Deadbug Dust','Highly Effective Organic Insecticide Spinosad', 'USD', 10.95, 1, 2);
INSERT INTO product (id, name, description, currency_string, default_price, category_id, supplier_id) VALUES (3,'Gopher and Mole Repellers','Effective, long-lasting and humane deterrent for gophers and moles', 'USD', 9.95, 1, 1);
INSERT INTO product (id, name, description, currency_string, default_price, category_id, supplier_id) VALUES (4,'Raised Bed Booster Kit','Booster Kit revitalizes the soil in your raised beds.', 'USD', 24.95, 2, 1);
INSERT INTO product (id, name, description, currency_string, default_price, category_id, supplier_id) VALUES (5,'Organic Tomato Fertilizer','Organic fertilizer provides essential nutrients', 'USD', 9.95, 2, 1);