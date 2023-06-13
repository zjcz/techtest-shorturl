package com.example.shorturl;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.shorturl.exceptions.InvalidUrlException;
import com.example.shorturl.exceptions.UrlNotFoundException;
import com.example.shorturl.helpers.IdentifierEncoder;
import com.example.shorturl.viewmodels.EncodeUrlViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.shorturl.dataaccess.ShortUrl;
import com.example.shorturl.services.ShortUrlService;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ShortUrlApiController.class)
class ShortUrlApiControllerTest {

    @MockBean
    ShortUrlService shortUrlService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void testEncodeValidUrlAndReturnShortCode() throws Exception {
        Long originalId = 123L;
        String originalUrl = "https://www.google.co.uk";
        String shortUrlCode = IdentifierEncoder.encodeIdentifier(originalId);

        ShortUrl shortUrl = new ShortUrl(originalUrl);
        shortUrl.setShortUrl(shortUrlCode);

        EncodeUrlViewModel postData = new EncodeUrlViewModel();
        postData.url = originalUrl;

        Mockito.when(shortUrlService.add(originalUrl)).thenReturn(shortUrl);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(postData));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.url", Matchers.is(originalUrl)))
                .andExpect(jsonPath("$.shortUrl", Matchers.is(shortUrlCode)));
    }

    @Test
    void testEncodeTheSameUrlProducesDifferentShortCodeAndReturnShortCode() throws Exception {
        String originalUrl = "https://www.msn.co.uk";

        Long originalId1 = 123L;
        String shortUrlCode1 = IdentifierEncoder.encodeIdentifier(originalId1);
        Long originalId2 = 456L;
        String shortUrlCode2 = IdentifierEncoder.encodeIdentifier(originalId2);

        ShortUrl shortUrl1 = new ShortUrl(originalUrl);
        shortUrl1.setShortUrl(shortUrlCode1);
        ShortUrl shortUrl2 = new ShortUrl(originalUrl);
        shortUrl2.setShortUrl(shortUrlCode2);

        EncodeUrlViewModel postData = new EncodeUrlViewModel();
        postData.url = originalUrl;

        Mockito.when(shortUrlService.add(originalUrl)).thenReturn(shortUrl1, shortUrl2);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(postData));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.url", Matchers.is(originalUrl)))
                .andExpect(jsonPath("$.shortUrl", Matchers.is(shortUrlCode1)));

        // now run a second time and expect a different shortcode
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.url", Matchers.is(originalUrl)))
                .andExpect(jsonPath("$.shortUrl", Matchers.is(shortUrlCode2)));
    }

    @Test
    void testEncodeInvalidUrlAndReturnError() throws Exception {
        Long originalId = 123L;
        String originalUrl = "https://www.google.co.uk";
        String shortUrlCode = IdentifierEncoder.encodeIdentifier(originalId);

        ShortUrl shortUrl = new ShortUrl(originalUrl);
        shortUrl.setShortUrl(shortUrlCode);

        Mockito.when(shortUrlService.add(originalUrl)).thenReturn(shortUrl);

        // post the data with a null url
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(new EncodeUrlViewModel()));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof InvalidUrlException))
                .andExpect(result -> assertEquals("Invalid Url: null", result.getResolvedException().getMessage()));
    }

    @Test
    void testDecodeValidShortCodeAndReturnShortUrl() throws Exception {
        Long originalId = 123L;
        String originalUrl = "https://www.google.co.uk";
        String shortUrlCode = IdentifierEncoder.encodeIdentifier(originalId);

        ShortUrl shortUrl = new ShortUrl(originalUrl);
        shortUrl.setShortUrl(shortUrlCode);

        Mockito.when(shortUrlService.find(shortUrlCode)).thenReturn(shortUrl);

        this.mockMvc.perform(get("/decode/" + shortUrlCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.url", Matchers.is(originalUrl)))
                .andExpect(jsonPath("$.shortUrl", Matchers.is(shortUrlCode)));
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

        this.mockMvc.perform(get("/decode/" + invalidShortUrlCode))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof UrlNotFoundException))
                .andExpect(result -> assertEquals("Could not find url for shortcode " + invalidShortUrlCode, result.getResolvedException().getMessage()));
    }
}