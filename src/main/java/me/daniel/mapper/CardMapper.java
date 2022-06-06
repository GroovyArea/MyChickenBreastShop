package me.daniel.mapper;

import me.daniel.domain.VO.CardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CardMapper {

    CardVO selectCard(String tid);

    void insertCard(@Param("CardVO") CardVO cardVO, @Param("tId") String tId);
}
