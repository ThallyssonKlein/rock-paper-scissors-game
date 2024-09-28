CREATE TABLE person (
    id bigint AUTO_INCREMENT PRIMARY KEY,
    name varchar(50) NOT NULL,
    identifier varchar(14) NOT NULL,
    birth_date date NOT NULL,
    identifier_type varchar(255) NOT NULL,
    min_monthly_value decimal(18, 4) NOT NULL,
    max_loan_value decimal(18, 4) NOT NULL
);

CREATE TABLE loan (
    id bigint AUTO_INCREMENT PRIMARY KEY,
    loan_value decimal(18, 4) NOT NULL,
    number_of_installments int NOT NULL,
    payment_status varchar(255) NOT NULL,
    creation_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    person_id bigint NOT NULL,
    FOREIGN KEY (person_id) REFERENCES person(id)
);

CREATE TABLE config (
    id int AUTO_INCREMENT PRIMARY KEY,
    max_loan_value decimal(18,4) NOT NULL,
    min_monthly_value decimal(18,4) NOT NULL,
    identifier_type varchar(50) NOT NULL
);

INSERT INTO config (max_loan_value, min_monthly_value, identifier_type) VALUES
(10000.00, 300.00, 'PF'),
(100000.00, 1000.00, 'PJ'),
(10000.00, 100.00, 'EU'),
(25000.00, 400.00, 'AP');