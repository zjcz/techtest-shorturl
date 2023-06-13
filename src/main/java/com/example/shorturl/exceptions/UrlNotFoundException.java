package com.example.shorturl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(final String shortUrl) {
        super("Could not find url for shortcode " + shortUrl);
    }
}
