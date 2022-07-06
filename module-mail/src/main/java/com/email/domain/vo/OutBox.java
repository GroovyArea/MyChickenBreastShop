package com.email.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutBox {

    private Long id;

    private Long aggregateId;

    private String aggregateType;

    private String eventType;

    private String payload;
}


