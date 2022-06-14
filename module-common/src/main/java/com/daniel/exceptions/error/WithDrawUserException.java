package com.daniel.exceptions.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WithDrawUserException extends Exception {

    public WithDrawUserException(String message) {
        super(message);

    }
}
