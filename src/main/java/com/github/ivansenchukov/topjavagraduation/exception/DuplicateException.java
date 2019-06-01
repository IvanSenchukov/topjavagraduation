package com.github.ivansenchukov.topjavagraduation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateException extends ResponseStatusException {

    public DuplicateException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
