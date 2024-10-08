CREATE TABLE users(
	user_id BIGINT AUTO_INCREMENT,
    full_name VARCHAR(150) NOT NULL,
	email VARCHAR(100) NOT NULL,
	password VARCHAR(255) NOT NULL,
	is_account_non_expired BOOLEAN NOT NULL,
	is_account_non_locked BOOLEAN NOT NULL,
	is_credentials_non_expired BOOLEAN NOT NULL,
	is_enabled BOOLEAN NOT NULL,
	
	CONSTRAINT PK_USER PRIMARY KEY(user_id),
	CONSTRAINT U_USER_EMAIL UNIQUE(email)
);

CREATE TABLE permission(
    permission_id BIGINT AUTO_INCREMENT,
    description VARCHAR(11) NOT NULL,

    CONSTRAINT PK_PERMISSION PRIMARY KEY(permission_id)
);

CREATE TABLE user_permission(
    user_id BIGINT,
    permission_id BIGINT,

    CONSTRAINT PK_USER_PERMISSION PRIMARY KEY(user_id, permission_id),
    CONSTRAINT FK_USER_PERMISSION FOREIGN KEY(user_id) REFERENCES users(user_id),
    CONSTRAINT FK_PERMISSION_USER FOREIGN KEY(permission_id) REFERENCES permission(permission_id)
);