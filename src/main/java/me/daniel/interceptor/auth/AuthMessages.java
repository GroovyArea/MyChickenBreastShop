package me.daniel.interceptor.auth;

public enum AuthMessages {

    NULL_TOKEN("DB에 토큰이 존재하지 않습니다. 로그인이 필요합니다."),
    INVALID_TOKEN("토큰이 일치하지 않습니다. 잘못된 접근입니다."),
    NOT_ADMIN_AUTH("관리자 권한을 필요로 하는 접근입니다.");

    private final String message;

    private AuthMessages(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
