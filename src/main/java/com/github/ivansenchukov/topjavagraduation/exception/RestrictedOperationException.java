package com.github.ivansenchukov.topjavagraduation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RestrictedOperationException extends ResponseStatusException {
    public RestrictedOperationException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
