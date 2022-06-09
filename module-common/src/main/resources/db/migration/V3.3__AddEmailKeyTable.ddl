CREATE TABLE EMAIL
(
    id         INT         NOT NULL AUTO_INCREMENT,
    email_key  VARCHAR(40) NOT NULL,
    email      VARCHAR(50) NOT NULL,
    expired_at DATETIME    NULL,
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '이메일 테이블';