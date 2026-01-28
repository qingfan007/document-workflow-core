package com.qingfan.documentcoredemo.exception;

public class InvalidStateException extends DomainException {
    public InvalidStateException(String message) {
        super(message);
    }
}
