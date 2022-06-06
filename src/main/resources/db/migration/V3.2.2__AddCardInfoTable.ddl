CREATE TABLE CARDINFO
(
    tid                   VARCHAR(30) NOT NULL,
    issuer_corp           VARCHAR(40) NULL,
    issuer_corp_code      VARCHAR(30) NULL,
    bin                   VARCHAR(30) NULL,
    card_type             VARCHAR(5)  NULL,
    install_month         VARCHAR(5)  NULL,
    interest_free_install VARCHAR(2)  NULL,

    INDEX `idx_tid` USING BTREE (tid) COMMENT '결제 고유 번호 인덱스' VISIBLE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '카드 정보 테이블';