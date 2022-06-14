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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * 주문 아웃 박스 조회 스케줄러 <br>
 * 10초마다 주문 건에 대한 재고 파악을 한다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CheckStockScheduler {

    private final OutBoxMapper outBoxMapper;
    private final ProductMapper productMapper;
    private final KakaoPayService kakaoPayService;

    @Scheduled(cron = "0/10 * * * * ?")
    @Transactional
    public void schedulingCheckStock() {
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("재고 확인 중 . . .");

        List<OutBox> outBoxList = outBoxMapper.selectAllOrderOutBox();
        if (!outBoxList.isEmpty()) {
            List<Long> completedList = new LinkedList<>();
            outBoxList.forEach(outBox -> {
                outBoxStockCheck(objectMapper, completedList, outBox);
            });
            if (!completedList.isEmpty()) {
                outBoxMapper.deleteAllById(completedList);
            }
        }
    }

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
            outBoxMapper.insertOrderOutBox(outBox);
        }
    }
}
