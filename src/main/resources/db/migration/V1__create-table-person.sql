CREATE TABLE person(
    person_id BIGINT AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    gender ENUM('MALE','FEMALE') NOT NULL,
    street VARCHAR(100) NOT NULL,
    neighborhood VARCHAR(50) NOT NULL,
    city VARCHAR(30) NOT NULL,
    state VARCHAR(30) NOT NULL,

    CONSTRAINT PK_PERSON PRIMARY KEY(person_id)
);