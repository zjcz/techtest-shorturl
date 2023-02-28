package com.example.shorturl.services;

import com.example.shorturl.dataaccess.ShortUrl;
import com.example.shorturl.dataaccess.ShortUrlRepository;
import com.example.shorturl.helpers.IdentifierEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortUrlService {

    @Autowired
    ShortUrlRepository repository;

    /**
     * Add the url to the database and return the ShortUrl record
     * @param url url to add
     * @return ShortUrl record
     */
    public ShortUrl add(final String url) {
        if (url == null || url.length() == 0) {
            throw new IllegalArgumentException("url cannot be null or empty string");
        }

        ShortUrl su = new ShortUrl(url);
        su = repository.save(su); // save to generate ID

        if (su != null) {
            su.setShortUrl(IdentifierEncoder.encodeIdentifier(su.getId()));
            repository.save(su); // save to store the shortUrl (encoded from the generated ID)
        }

        return su;
    }

    /**
     * Retrieve the ShortUrl record for this identifier, or null if not found
     * @param shortUrl identifier of the ShortUrl record
     * @return the ShortUrl record for this identifier, or null if not found
     */
    public ShortUrl find(final String shortUrl) {
        return repository.findByShortUrl(shortUrl);
    }
}
