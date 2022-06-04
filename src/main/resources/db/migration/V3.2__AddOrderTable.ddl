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