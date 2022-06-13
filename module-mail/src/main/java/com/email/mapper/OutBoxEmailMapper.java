package com.email.mapper;

import com.email.domain.VO.OutBox;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OutBoxEmailMapper {

    List<OutBox> selectAllEmailOutBox();

    void insertOutBox(OutBox outBox);

    void deleteAllById(List<Long> completedList);
}
