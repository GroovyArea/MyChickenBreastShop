package com.daniel.outbox.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailKeyCreated {

    private Long emailKeyId;

    private Long emailKey;

    private LocalDateTime expireDate;

    private String email;
}
