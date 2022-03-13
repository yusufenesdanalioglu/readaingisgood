package com.readingisgood.app.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StockNotEnoughException extends RuntimeException {

    public StockNotEnoughException() {
    }

    public StockNotEnoughException(String message) {
        super(message);
    }

    public StockNotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }
}
