CREATE TABLE amount
(
    tid      VARCHAR(30) NOT NULL,
    total    INT(10)   NULL,
    tax_free INT(10)    NULL,
    vat      INT(10)    NULL,
    point    INT(10)    NULL,
    discount INT(10)    NULL,

    INDEX `idx_tid` USING BTREE (tid) COMMENT '결제 고유 번호 인덱스' VISIBLE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '결제 금액 테이블';