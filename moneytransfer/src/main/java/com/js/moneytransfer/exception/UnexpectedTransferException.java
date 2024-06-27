package com.js.moneytransfer.exception;

public class UnexpectedTransferException extends RuntimeException {
    public UnexpectedTransferException(String message, Throwable cause) {
        super(message, cause);
    }
}
