CREATE TABLE accounts (
	account_id integer NOT null auto_increment,
	email varchar(100)  not null unique,
	password varchar(100) not null,
	firstname varchar(50) not null,
	lastname varchar(50) not null,
	photo blob,
	constraint accounts_PK primary key (account_id)
);