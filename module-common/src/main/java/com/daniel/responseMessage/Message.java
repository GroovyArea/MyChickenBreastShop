package com.daniel.responseMessage;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * 응답 Body에 보낼 메시지 클래스
 * Builder 패턴 적용
 */
public class Message {

    private Object data;
    private String message;
    private MediaType mediaType;
    private HttpStatus httpStatus;

    public static class Builder {

        private Object data;
        private String message;
        private MediaType mediaType;
        private HttpStatus httpStatus;

        public Builder(Object data) {
            this.data = data;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }

    private Message(Builder builder) {
        data = builder.data;
        message = builder.message;
        mediaType = builder.mediaType;
        httpStatus = builder.httpStatus;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
