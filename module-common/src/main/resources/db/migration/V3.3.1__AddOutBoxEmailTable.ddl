CREATE TABLE `OUTBOX-EMAIL`
(
    id             INT         NOT NULL AUTO_INCREMENT,
    aggregate_id   VARCHAR(40) NOT NULL,
    aggregate_type VARCHAR(50) NOT NULL,
    event_type     VARCHAR(50) NOT NULL,
    payload        TEXT        NOT NULL,
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '아웃박스 이메일 테이블';