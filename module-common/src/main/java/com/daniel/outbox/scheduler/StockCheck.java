package com.daniel.outbox.scheduler;

import com.daniel.domain.VO.OutBox;
import com.daniel.mapper.OutBoxMapper;
import com.daniel.mapper.ProductMapper;
import com.daniel.service.KakaoPayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockCheck {

    private final KakaoPayService kakaoPayService;
    private final ProductMapper productMapper;
    private final OutBoxMapper outBoxMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void outBoxStockCheck(ObjectMapper objectMapper, List<Long> completedList, OutBox outBox) {
        String payload = outBox.getPayload();
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            String itemName = jsonNode.get("item_name").asText();

            if (productMapper.selectStockOfProduct(itemName) < Integer.parseInt(jsonNode.get("quantity").asText())) {
                kakaoPayService.changeStockFlag(false);
            }

            completedList.add(outBox.getId());

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            outBoxMapper.deleteById(outBox);
            outBoxMapper.insertOrderOutBox(outBox);
        }
    }
}
