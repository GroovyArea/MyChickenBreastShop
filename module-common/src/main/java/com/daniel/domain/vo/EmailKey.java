package com.daniel.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailKey {

    private Long id;

    private Long emailKey;

    private String email;

    private LocalDateTime expiredAt;
}
