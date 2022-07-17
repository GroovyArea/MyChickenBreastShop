package com.daniel.enums.global;

public enum ResponseMessage {

    ADD_MESSAGE("Add successful"),
    MODIFY_MESSAGE("Modify successful"),
    DELETE_MESSAGE("Delete successful");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return message;
    }
}
