package com.nataraj.currencyExchange.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ChangeNotAvailableException extends ResponseStatusException {

    public ChangeNotAvailableException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
}
