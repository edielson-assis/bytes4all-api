CREATE TABLE books(
    book_id BIGINT AUTO_INCREMENT,
    author VARCHAR(150) NOT NULL,
    launch_date DATE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    download_url VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT PK_BOOK PRIMARY KEY(book_id),
    CONSTRAINT FK_BOOK_USER FOREIGN KEY(user_id) REFERENCES users(user_id)
);