CREATE TABLE IF NOT EXISTS accounts (
  account_id int NOT NULL AUTO_INCREMENT( 1000 ),
  email varchar(500) NOT NULL UNIQUE,
  password varchar(1000) NOT NULL,
  firstname varchar(50) NOT NULL,
  lastname varchar(50) NOT NULL,
  photo blob,
  is_active char(1) NOT NULL,
  PRIMARY KEY (account_id),
  CONSTRAINT accounts_chk CHECK ((is_active in ('Y', 'N')))
);

CREATE TABLE IF NOT EXISTS details (
  account_id int NOT NULL,
  has_flat char(1) NOT NULL,
  degree varchar(100) NOT NULL,
  weekly_budget varchar(1000) NOT NULL,
  extra_info varchar(1000) DEFAULT NULL,
  date_of_birth date NOT NULL,
  diet_requirements varchar(500),
  PRIMARY KEY (account_id),
  CONSTRAINT details_FK FOREIGN KEY (account_id) REFERENCES accounts (account_id),
  CONSTRAINT details_chk_1 CHECK ((has_flat in ('Y','N')))
);

CREATE ROLE IF NOT EXISTS select_role;
GRANT SELECT on accounts to select_role;
GRANT SELECT on details to select_role;
CREATE USER IF NOT EXISTS selectaccount PASSWORD 'OtagoUni2021';
GRANT select_role to selectaccount;

CREATE ROLE IF NOT EXISTS create_role;
GRANT SELECT, INSERT on accounts to create_role;
GRANT SELECT, INSERT on details to create_role;
CREATE USER IF NOT EXISTS createaccount PASSWORD 'flatty2021';
GRANT create_role to createaccount;

CREATE ROLE IF NOT EXISTS update_role;
GRANT SELECT, UPDATE on accounts to update_role;
GRANT SELECT, UPDATE on details to update_role;
CREATE USER IF NOT EXISTS updateaccount PASSWORD 'Info310Flatty';
GRANT update_role to updateaccount;

CREATE ROLE IF NOT EXISTS delete_role;
GRANT SELECT, DELETE on accounts to delete_role;
GRANT SELECT, DELETE on details to delete_role;
CREATE USER IF NOT EXISTS deleteaccount PASSWORD 'flattyproject';
GRANT delete_role to deleteaccount;

--Inserting testing data for DetailsDAO testing for H2 temporary database--
INSERT INTO accounts (email, password, firstname, lastname, is_active) values ('student1@student.otago.ac.nz', 'scarfie', 'John', 'Doe', 'N');
INSERT INTO accounts (email, password, firstname, lastname, is_active) values ('danielcalencar@otago.ac.nz', 'INFO310', 'Daniel', 'Alencar da Costa', 'Y');