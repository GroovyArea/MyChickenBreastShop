package com.daniel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 응답 Body에 보낼 메시지 클래스
 * Builder 패턴 적용
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private Object data;
    private String message;

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
