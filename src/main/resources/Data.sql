DROP database IF EXISTS pay_my_buddy;
CREATE database pay_my_buddy;
use pay_my_buddy;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
                user_id INT AUTO_INCREMENT NOT NULL,
                email VARCHAR(100) NOT NULL,
                password VARCHAR(100) NOT NULL,
                first_name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                balance DECIMAL(7,2) UNSIGNED DEFAULT 0,
                address VARCHAR(100) NOT NULL,
                zip INT NOT NULL,
                city VARCHAR(100) NOT NULL,
                phone VARCHAR(20) NOT NULL,
                role VARCHAR(20) DEFAULT 'USER',
                PRIMARY KEY (user_id)
);


CREATE UNIQUE INDEX user_idx
 ON user
 ( email );

DROP TABLE IF EXISTS connection;
CREATE TABLE connection (
                connection_id INT AUTO_INCREMENT NOT NULL,
                id_user INT,
                email_of_user_linked VARCHAR(100) NOT NULL,
                name VARCHAR(100) NOT NULL,
                PRIMARY KEY (connection_id)
);

DROP TABLE IF EXISTS transaction;
CREATE TABLE transaction (
                transaction_id INT AUTO_INCREMENT NOT NULL,
                id_connection INT,
                id_transmitter INT,
                id_beneficiary INT,
                connection_name VARCHAR(100) NOT NULL,
                description VARCHAR(100) NOT NULL,
                amount DECIMAL(5,2) NOT NULL,
                date DATETIME NOT NULL,
                success BOOLEAN NOT NULL,
                PRIMARY KEY (transaction_id)
);

DROP TABLE IF EXISTS bank_account;
CREATE TABLE bank_account (
                account_id INT AUTO_INCREMENT,
                id_user INT,
                name VARCHAR(100) NOT NULL,
                bic VARCHAR(100) NOT NULL,
                iban VARCHAR(100) NOT NULL,
                PRIMARY KEY (account_id)
);


DROP TABLE IF EXISTS transfer;
CREATE TABLE transfer (
                transfer_id INT AUTO_INCREMENT NOT NULL,
                id_bank_account INT DEFAULT NULL,
                id_user INT,
                amount DECIMAL(5,2) DEFAULT 0,
                transfer_type ENUM('DEBIT', 'CREDIT') NOT NULL,
                date DATETIME NOT NULL,
                success BOOLEAN NOT NULL,
                PRIMARY KEY (transfer_id)
);

DROP TABLE IF EXISTS commission;
CREATE TABLE commission (
                commission_id INT AUTO_INCREMENT NOT NULL,
                id_transaction INT NOT NULL,
                Amount DECIMAL(4,2) NOT NULL,
                date DATETIME NOT NULL,
                PRIMARY KEY (commission_id)
);

ALTER TABLE bank_account ADD CONSTRAINT user_bank_account_fk
FOREIGN KEY (id_user)
REFERENCES user (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE connection ADD CONSTRAINT user_connection_fk
FOREIGN KEY (id_user)
REFERENCES user (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT user_transaction_fk
FOREIGN KEY (id_transmitter)
REFERENCES user (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transfer ADD CONSTRAINT user_transfer_fk
FOREIGN KEY (id_user)
REFERENCES user (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT user_transaction_fk1
FOREIGN KEY (id_beneficiary)
REFERENCES user (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT connection_transaction_fk
FOREIGN KEY (id_connection)
REFERENCES connection (connection_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transfer ADD CONSTRAINT bank_account_transfer_fk
FOREIGN KEY (id_bank_account)
REFERENCES bank_account (account_id)
ON DELETE NO ACTION
ON UPDATE CASCADE;

 INSERT INTO user
    (email, password, first_name, last_name, balance, address, zip, city, phone, role)
VALUES
    ('email1@test.com', '$2y$10$jKXgiFax82q3LAIXe/WnzOIiYVDfIRbovxVxd72OTyxEd9hnJDZnS', 'Paul', 'Dupond', 150, 'address1', 10000, 'city1', 0100000000, 'USER'),
    ('email2@test.com', '$2y$10$6NwH2qVECHhOO20fOMbWZe6/TWcgSJo/Rk8zKUj4EkAeHcDrVWDqW', 'Pierre', 'Dupond', 500, 'address2', 20000, 'city2', 0200000000, 'USER'),
    ('email3@test.com', '$2y$10$bg86xhmlvqE3NhkQWxP/ZO74EnDkhIwFVrIUiGSdqciRModBimceS', 'Jacques', 'Dupond', 0, 'address3', 30000, 'city3', 0300000000, 'USER'),
	('email4@test.com', '$2a$10$B9/Z6yWuXJLSAP1aCbzeaOCP3sFwQpLmY21rx7TqVAqDU4JAAhQaW', 'Mike', 'Martin', 115, 'address4', 40000, 'city4', 0400000000, 'ADMIN'),
    ('email5@test.com', '$2a$10$rFA1ptFvOKI.d4fshy4WFuTLuPeWLgSLlLQJSiRG7i49ilUj8k5Ne', 'Marc', 'Doe', 0, 'address5', 50000, 'city5', 0500000000, 'USER'),
    ('email6@test.com', '$2a$10$wNOmMSKL8HFDN9bFn111uOkdxlX3IoZwLzJAQcBZKdKeCocADo8Cm', 'Mathieu', 'Doe', 0, 'address6', 60000, 'city6', 0600000000, 'USER')
;
-- default password = 12345678

 INSERT INTO bank_account
    (id_user, name, bic, iban)
VALUES
    (1, 'Societe Generale', '123456', '456456'),
    (1, 'Credit Agricole', '456789', '789789'),
    (2, 'CIC', '789123', '123123')
;

INSERT INTO connection
(id_user, email_of_user_linked, name)
VALUES
(1, 'email2@test.com', 'Pierre.D'),
(2, 'email3@test.com', 'Jacques.D'),
(1,'email3@test.com', 'Jacques.D'),
(1, 'email4@test.com', 'Mike.M'),
(2,'email1@test.com', 'Paul.D')
;

INSERT INTO transfer
(id_bank_account, id_user, amount, transfer_type, date, success)
VALUES
('1', '1', '50', 'debit', '2021-04-02', '1'),
('2', '1', '50', 'credit', '2021-01-01', '1')
;

INSERT INTO transaction
(id_connection, id_transmitter, id_beneficiary, connection_name, description, amount, date, success)
VALUES
('1', '1', '2', 'Pierre.D', 'Restaurant refund', '50', '2021-01-01', '1'),
('1', '1', '3', 'Jacques.D', 'Cinema refund', '20', '2021-08-08', '1'),
('1', '1', '4', 'Mike.M', 'Birthday present', '75', '2021-07-01', '1'),
('1', '1', '4', 'Mike.M', 'Household shopping', '30', '2021-08-08', '1'),
('2', '2', '3', 'Jacques.D', 'Cinema refund', '25', '2021-01-01', '1'),
('4', '2', '1', 'Paul.D', 'Birthday present', '15', '2021-01-01', '1')
;

INSERT INTO commission
(id_transaction, amount, date)
VALUES
('5', '1', '2021-08-08'),
('4', '3.75', '2021-07-01'),
('3', '0.75', '2021-01-01'),
('2', '1.25', '2021-01-01'),
('1', '2.5', '2021-01-01')
;