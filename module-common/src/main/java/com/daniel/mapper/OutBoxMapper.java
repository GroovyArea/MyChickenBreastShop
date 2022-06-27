package com.daniel.mapper;

import com.daniel.domain.VO.OutBox;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OutBoxMapper {

    List<OutBox> selectAllOrderOutBox();

    void insertEmailOutBox(OutBox outbox);

    void insertOrderOutBox(OutBox outBox);

    void deleteById(OutBox outBox);

    void deleteAllById(List<Long> completedList);
}
