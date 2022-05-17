ALTER TABLE USER
    CHANGE COLUMN `user_grade` `user_grade` INT(1) NULL DEFAULT 1,
    CHANGE COLUMN `user_reserves` `user_reserves` INT(6) NULL DEFAULT 2000;