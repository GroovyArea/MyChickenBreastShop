drop table if exists testtest;
drop table if exists `user`;
drop table if exists `product`;
drop table if exists `amount`;
drop table if exists `CARDINFO`;
drop table if exists `order`;
drop table if exists `email`;
drop table if exists `OUTBOX-EMAIL`;
drop table if exists `OUTBOX-ORDER`;

CREATE TABLE `user` (
                        `user_id` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
                        `user_pw` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `user_salt` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `user_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `user_phone` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `user_email` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `user_main_address` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `user_detail_address` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `user_zipcode` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `user_grade` int(1) DEFAULT '1',
                        PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='chicken breast shop user';

CREATE TABLE `product` (
                           `product_no` int(100) NOT NULL AUTO_INCREMENT,
                           `product_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
                           `product_category` int(1) DEFAULT NULL,
                           `product_price` int(5) DEFAULT NULL,
                           `product_stock` int(5) DEFAULT NULL,
                           `product_detail` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
                           `product_image` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
                           `product_status` int(1) DEFAULT '1',
                           PRIMARY KEY (`product_no`),
                           KEY `idx_name` (`product_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='chicken breast shop product';

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

CREATE TABLE `order`
(
    tid             VARCHAR(50) NOT NULL,
    user_id          VARCHAR(45) NULL,
    aid              VARCHAR(45) NULL,
    cid              VARCHAR(45) NULL,
    partner_order_id VARCHAR(45) NULL,
    partner_user_id  VARCHAR(45) NULL,
    item_name       VARCHAR(60) NULL,
    item_code        VARCHAR(60) NULL,
    quantity         INT(100)    NULL,
    created_at       TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP,
    approved_at      TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP,
    order_status     CHAR(6)     NULL,
    PRIMARY KEY (tid),
    INDEX `idx_userid` USING BTREE (user_id) COMMENT '유저 아이디 인덱스' VISIBLE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '주문 테이블';

ALTER TABLE `order` ADD COLUMN PAYMENT_METHOD_TYPE CHAR(10);

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

CREATE TABLE `OUTBOX-ORDER`
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
    COMMENT = '아웃박스 주문 테이블';

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