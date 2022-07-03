package com.email.outbox.scheduler;

import com.email.domain.VO.OutBox;
import com.email.mapper.OutBoxEmailMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * 이메일 스케쥴러 <br>
 * 10초 간격으로 outbox 테이블 데이터 조회 후 메일 발송
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSendScheduler {

    private final OutBoxEmailMapper outBoxEmailMapper;
    private final EmailCheck emailCheck;

    @Scheduled(cron = "0/10 * * * * ?")
    @Transactional
    public void schedulingValidNumberEmail() {
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("이메일 전송 중...");

        List<OutBox> outBoxList = outBoxEmailMapper.selectAllEmailOutBox();
        if (!outBoxList.isEmpty()) {
            List<Long> completedList = new LinkedList<>();
            outBoxList.forEach(outBox ->
                    emailCheck.validateEmailNumber(objectMapper, completedList, outBox)
            );
            if (!completedList.isEmpty()) {
                outBoxEmailMapper.deleteAllById(completedList);
            }
        }
    }


}
