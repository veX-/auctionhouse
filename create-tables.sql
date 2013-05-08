use auctionhouse;

DROP TABLE users_products;
DROP TABLE users;
DROP TABLE products;

CREATE TABLE users(
Id INTEGER NOT NULL AUTO_INCREMENT, PRIMARY KEY(Id),
Name varchar(20) NOT NULL,
UserType varchar(10) NOT NULL,
Ip varchar(15),
Port INTEGER
);

CREATE TABLE products (
Id INTEGER NOT NULL AUTO_INCREMENT, PRIMARY KEY(Id),
Name varchar(20) NOT NULL	
);

CREATE TABLE users_products (
UserId int, FOREIGN KEY (UserId) REFERENCES users(Id),
ProductId int, FOREIGN KEY (ProductId) REFERENCES products(Id),
PRIMARY KEY(UserId, ProductId)
);