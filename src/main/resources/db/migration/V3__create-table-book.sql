CREATE TABLE book(
    book_id BIGINT AUTO_INCREMENT,
    author VARCHAR(150) NOT NULL,
    launch_date DATE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,

    CONSTRAINT PK_BOOK PRIMARY KEY(book_id)
);