package com.example.shorturl;

import com.example.shorturl.dataaccess.ShortUrl;
import com.example.shorturl.exceptions.UrlNotFoundException;
import com.example.shorturl.services.ShortUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UrlController {
    private static final Logger log = LoggerFactory.getLogger(ShortUrlApiController.class);

    @Autowired
    ShortUrlService shortUrlService;

    @GetMapping("/{shortUrl}")
    public String urlRedirect(@PathVariable String shortUrl) {
        if (shortUrl.endsWith(".html")) {
            return null;
        }

        log.info("Web request received for short url: " + shortUrl);
        ShortUrl url = shortUrlService.find(shortUrl);

        if (url == null) {
            log.warn("Url not found for short url: " + shortUrl);
            throw new UrlNotFoundException(shortUrl);
        }

        return "redirect:" + url.getLongUrl();
    }
}
