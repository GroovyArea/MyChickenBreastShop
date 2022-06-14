package com.daniel.exceptions.error;

public class RunOutOfStockException extends Exception {
    public RunOutOfStockException() {
        super("상품이 품절되었습니다.");
    }
}
