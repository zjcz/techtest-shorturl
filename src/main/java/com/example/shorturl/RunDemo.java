package com.example.shorturl;

import com.example.shorturl.dataaccess.ShortUrl;
import com.example.shorturl.services.ShortUrlService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class RunDemo {

    private static final Logger log = LoggerFactory.getLogger(RunDemo.class);

    @Bean
    public CommandLineRunner demo(ShortUrlService suService) {
        return (args) -> {
            // save a few customers
            ShortUrl bbc = suService.add("https://www.bbc.co.uk");
            ShortUrl google = suService.add("https://www.google.co.uk");
            ShortUrl microsoft = suService.add("https://www.microsoft.com");

            // fetch records by short url code
            log.info("Find the urls based on the short url");
            log.info("--------------------------------------------");
            log.info("BBC: " + suService.find(bbc.getShortUrl()).toString());
            log.info("Google: " + suService.find(google.getShortUrl()).toString());
            log.info("Microsoft: " + suService.find(microsoft.getShortUrl()).toString());
            log.info("");
        };
    }
}
