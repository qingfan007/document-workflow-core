package com.qingfan.documentcoredemo.exception;

public class ForbiddenOperationException extends DomainException {
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
