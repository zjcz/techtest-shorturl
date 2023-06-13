package com.example.shorturl;

import com.example.shorturl.dataaccess.ShortUrl;
import com.example.shorturl.exceptions.UrlNotFoundException;
import com.example.shorturl.helpers.IdentifierEncoder;
import com.example.shorturl.services.ShortUrlService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlController.class)
public class UrlControllerTest {
    @MockBean
    ShortUrlService shortUrlService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testDecodeValidShortCodeAndRedirectToUrl() throws Exception {
        Long originalId = 123L;
        String originalUrl = "https://www.google.co.uk";
        String shortUrlCode = IdentifierEncoder.encodeIdentifier(originalId);

        ShortUrl shortUrl = new ShortUrl(originalUrl);
        shortUrl.setShortUrl(shortUrlCode);

        Mockito.when(shortUrlService.find(shortUrlCode)).thenReturn(shortUrl);

        this.mockMvc.perform(get("/" + shortUrlCode))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(originalUrl));
    }

    @Test
    void testDecodeInvalidShortCodeAndReturnError() throws Exception {
        Long originalId = 123L;
        String originalUrl = "https://www.google.co.uk";
        String shortUrlCode = IdentifierEncoder.encodeIdentifier(originalId);
        String invalidShortUrlCode = "abcdefg";

        ShortUrl shortUrl = new ShortUrl(originalUrl);
        shortUrl.setShortUrl(shortUrlCode);

        Mockito.when(shortUrlService.find(shortUrlCode)).thenReturn(shortUrl);

        this.mockMvc.perform(get("/" + invalidShortUrlCode))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof UrlNotFoundException))
                .andExpect(result -> assertEquals("Could not find url for shortcode " + invalidShortUrlCode, result.getResolvedException().getMessage()));
    }

    @Test
    void testShortCodeEqualsEncodeAndReturnError() throws Exception {
        Long originalId = 123L;
        String originalUrl = "https://www.google.co.uk";
        String shortUrlCode = IdentifierEncoder.encodeIdentifier(originalId);
        String invalidShortUrlCode = "encode";

        ShortUrl shortUrl = new ShortUrl(originalUrl);
        shortUrl.setShortUrl(shortUrlCode);

        Mockito.when(shortUrlService.find(shortUrlCode)).thenReturn(shortUrl);

        this.mockMvc.perform(get("/" + invalidShortUrlCode))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof UrlNotFoundException))
                .andExpect(result -> assertEquals("Could not find url for shortcode " + invalidShortUrlCode, result.getResolvedException().getMessage()));
    }

    @Test
    void testShortCodeEqualsDecodeAndReturnError() throws Exception {
        Long originalId = 123L;
        String originalUrl = "https://www.google.co.uk";
        String shortUrlCode = IdentifierEncoder.encodeIdentifier(originalId);
        String invalidShortUrlCode = "decode";

        ShortUrl shortUrl = new ShortUrl(originalUrl);
        shortUrl.setShortUrl(shortUrlCode);

        Mockito.when(shortUrlService.find(shortUrlCode)).thenReturn(shortUrl);

        this.mockMvc.perform(get("/" + invalidShortUrlCode))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof UrlNotFoundException))
                .andExpect(result -> assertEquals("Could not find url for shortcode " + invalidShortUrlCode, result.getResolvedException().getMessage()));
    }
}
