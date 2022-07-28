DROP TABLE USER;

DROP TABLE PRODUCT;

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

