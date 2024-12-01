package com.pi.trading_investment_backend.exception;

public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String message) {
        super(message);
    }
    public ExternalApiException(String message,Exception e) {
        super(message,e);
    }
}
