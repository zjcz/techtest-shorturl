package com.example.shorturl.dataaccess;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository to store ShortUrl objects
 */
public interface ShortUrlRepository extends CrudRepository<ShortUrl, Long> {
    ShortUrl findByShortUrl(String shortUrl);
}
