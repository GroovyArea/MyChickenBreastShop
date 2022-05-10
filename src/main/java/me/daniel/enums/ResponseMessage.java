package me.daniel.enums;

public enum ResponseMessage {

    LOGIN_MESSAGE("Login succeed"),
    LOGOUT_MESSAGE("Logout succeed"),
    ADD_MESSAGE("Add successful"),
    MODIFY_MESSAGE("Modify successful"),
    DELETE_MESSAGE("Delete successful");

    private final String message;

    private ResponseMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return message;
    }
}
