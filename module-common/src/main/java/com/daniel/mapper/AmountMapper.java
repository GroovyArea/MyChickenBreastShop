package com.daniel.mapper;

import com.daniel.domain.VO.AmountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AmountMapper {

    AmountVO selectAmount(String tid);

    void insertAmount(@Param("amountVO") AmountVO amountVO, @Param("tId") String tId);
}
