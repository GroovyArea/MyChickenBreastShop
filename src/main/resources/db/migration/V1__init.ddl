# 이전 테이블 지우고
drop table if exists testtest;

# 초기 스키마 작성
create table testtest(
    id int auto_increment;
    name varchar(250),
    primary key (id)
);

insert into testtest (name) values('test1');
insert into testtest (name) values('test2');
insert into testtest (name) values('test3');
