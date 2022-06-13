package com.daniel.mapper;

import com.daniel.domain.VO.OutBox;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OutBoxMapper {

    void insertOutBox(OutBox outbox);

}
