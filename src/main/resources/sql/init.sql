
CREATE TABLE producer(
                         id SERIAL PRIMARY KEY ,
                         name VARCHAR(124) NOT NULL UNIQUE
);

CREATE TABLE material(
                         id BIGSERIAL PRIMARY KEY ,
                         name VARCHAR(124) NOT NULL UNIQUE ,
                         producer_id INT NOT NULL REFERENCES producer(id) on delete CASCADE ,
                         count INT NOT NULL CHECK ( count>=0 ),
                         price DECIMAL(15, 2) NOT NULL CHECK ( price>=0 )
);

DROP TABLE producer, material;

ALTER TABLE medical_history
ADD COLUMN date DATE NOT NULL DEFAULT '1999-01-01';