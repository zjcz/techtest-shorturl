package com.example.shorturl.viewmodels;

/**
 * View model used to return data when the /encode or /decode API methods are called
 */
public class ShortUrlViewModel {
    /**
     * Original full url
     */
    public String url;

    /**
     * Short url code
     */
    public String shortUrl;

    public ShortUrlViewModel(final String url, final String shortUrl) {
        this.url = url;
        this.shortUrl = shortUrl;
    }
}
