DROP TABLE IF exists phone_numbers;
CREATE TABLE IF NOT EXISTS phone_numbers (phone_id SERIAL PRIMARY KEY, phone_number VARCHAR(10) NOT NULL, type VARCHAR(10) NOT NULL, activated VARCHAR(20) NOT NULL, customer_id integer);

CREATE UNIQUE INDEX uidx_phone_number
ON phone_numbers (phone_number);

INSERT INTO phone_numbers (phone_number, type, activated) values ('0423182484','MOBILE','false');
INSERT INTO phone_numbers (phone_number, type, activated) values ('0423182485','MOBILE','false');
INSERT INTO phone_numbers (phone_number, type, activated) values ('0423182486','MOBILE','false');

INSERT INTO phone_numbers (phone_number, type, activated, customer_id) values ('0423182487','MOBILE','true', 12345);