package com.example.shorturl.dataaccess;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entity object representing a stored url
 */
@Entity
public class ShortUrl {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String longUrl;
    private String shortUrl;

    protected ShortUrl() {}

    public ShortUrl(final String longUrl) {
        this.longUrl = longUrl;
    }

    @Override
    public String toString() {
        return String.format(
                "ShortUrl[id=%s, longUrl='%s', shortUrl='%s']",
                id, longUrl, shortUrl);
    }

    public Long getId() {
        return id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setShortUrl(final String shortUrl) {
        this.shortUrl = shortUrl;
    }
    public String getShortUrl() {
        return shortUrl;
    }
}