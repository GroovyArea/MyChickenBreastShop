package com.email.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDTO {

    private String title;

    private String to;

    private String from;

    private String content;
}
