package com.daniel.mapper;

import com.daniel.domain.vo.EmailKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EmailKeyMapper {

    EmailKey selectEmailKey(@Param("id") long emailKeyId);

    void insertEmailKey(EmailKey emailKey);
}
