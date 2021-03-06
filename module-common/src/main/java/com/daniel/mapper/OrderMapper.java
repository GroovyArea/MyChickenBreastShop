package com.daniel.mapper;

import com.daniel.domain.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<OrderVO> selectOrderList(String userId);

    void insertOrder(OrderVO orderVO);

    void updateOrder(String tid);
}
