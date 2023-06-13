package com.example.shorturl;

import com.example.shorturl.dataaccess.ShortUrl;
import com.example.shorturl.exceptions.InvalidShortUrlException;
import com.example.shorturl.exceptions.InvalidUrlException;
import com.example.shorturl.exceptions.UrlNotFoundException;
import com.example.shorturl.services.ShortUrlService;
import com.example.shorturl.viewmodels.EncodeUrlViewModel;
import com.example.shorturl.viewmodels.ShortUrlViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle the API calls /encode and /decode
 */
@RestController
public class ShortUrlApiController {

    private static final Logger log = LoggerFactory.getLogger(ShortUrlApiController.class);

    @Autowired
    ShortUrlService shortUrlService;

    @PostMapping("/encode")
    ShortUrlViewModel encode(@RequestBody EncodeUrlViewModel payload) {
        // validate the input
        if (payload == null || payload.url == null || payload.url.trim().length() == 0) {
            log.warn("Url shortener failed: url payload is empty");
            throw new InvalidUrlException(payload == null ? "payload is null" : payload.url);
        }

        log.info("API request received to encode url: " + payload.url);

        ShortUrl shortUrl = shortUrlService.add(payload.url);
        if (shortUrl == null) {
            log.warn("Url shortener failed for : " + payload.url + ", shortUrl is null");
            throw new InvalidShortUrlException(payload.url);
        }

        log.info("Url shortened:" + shortUrl.toString());
        return new ShortUrlViewModel(payload.url, shortUrl.getShortUrl());
    }

    @GetMapping("/decode/{shortUrl}")
    ShortUrlViewModel decode(@PathVariable String shortUrl) {
        log.info("API request received to decode short url: " + shortUrl);
        ShortUrl url = shortUrlService.find(shortUrl);

        if (url == null) {
            log.warn("Url not found for short url: " + shortUrl);
            throw new UrlNotFoundException(shortUrl);
        }

        return new ShortUrlViewModel(url.getLongUrl(), shortUrl);
    }
}
