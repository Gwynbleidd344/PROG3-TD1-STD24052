CREATE TABLE Product (
    id int PRIMARY KEY,
    name varchar(50) NOT NULL,
    price float CHECK (price > 0) NOT NULL ,
    creation_datetime timestamp NOT NULL
);
CREATE TABLE Product_Category (
    id int PRIMARY KEY,
    name varchar(50) NOT NULL,
    product_id int,
    FOREIGN KEY (product_id) references Product(id)
);