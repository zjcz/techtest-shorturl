package com.example.shorturl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidShortUrlException extends RuntimeException {
    public InvalidShortUrlException(final String url) {
        super("Could not encode url " + url);
    }
}
