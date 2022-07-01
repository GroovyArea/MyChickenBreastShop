package com.daniel.service;

import com.daniel.domain.DTO.order.response.AmountDTO;
import com.daniel.domain.DTO.order.response.CardDTO;
import com.daniel.domain.DTO.order.response.OrderDTO;
import com.daniel.mapper.OrderMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 서비스 <br>
 * 주문 조회, 추가, 수정, 삭제 처리를 실행한다.
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.28 최초 작성
 *     김남영, 1.1, 2022.06.27 스트림 수정
 * </pre>
 *
 * @author 김남영
 * @version 1.1
 */
@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final ModelMapper modelMapper;

    public OrderService(OrderMapper orderMapper, ModelMapper modelMapper) {
        this.orderMapper = orderMapper;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrderInfoList(String userId) {
        return orderMapper.selectOrderList(userId).stream()
                .map(a -> {
                            OrderDTO orderDTO = modelMapper.map(a, OrderDTO.class);
                            orderDTO.setCardDTO(modelMapper.map(a.getCardVO(), CardDTO.class));
                            orderDTO.setAmountDTO(modelMapper.map(a.getAmountVO(), AmountDTO.class));
                            return orderDTO;
                        }
                )
                .collect(Collectors.toList());
    }
}
