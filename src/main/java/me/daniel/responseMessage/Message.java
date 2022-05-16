package me.daniel.responseMessage;

/**
 * 응답 Body에 보낼 메시지 클래스
 * Builder 패턴 적용
 */
public class Message {

    private Object data;
    private String message;

    public static class Builder {

        private Object data;
        private String message;

        public Builder(Object data) {
            this.data = data;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }

    private Message(Builder builder) {
        data = builder.data;
        message = builder.message;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
