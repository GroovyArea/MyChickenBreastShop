package me.daniel.enums.users;

public enum ExceptionMessages {

    USER_EXISTS_MESSAGE("이미 사용중인 아이디를 입력 하셨습니다."),
    LOGIN_FAIL_MESSAGE("해당 아이디의 회원 정보가 존재하지 않습니다."),
    WITHDRAWAL_USER_MESSAGE("탈퇴 회원입니다."),
    WRONG_PASSWORD_MESSAGE("비밀번호가 일치하지 않습니다.");

    private String exceptionMessage;

    private ExceptionMessages(String exceptionMessage){
        this.exceptionMessage = exceptionMessage;
    }

    public String getValue() {
        return exceptionMessage;
    }
}
