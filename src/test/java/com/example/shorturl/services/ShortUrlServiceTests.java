package com.example.shorturl.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.shorturl.helpers.IdentifierEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.shorturl.dataaccess.*;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ShortUrlServiceTests {
    @InjectMocks
    ShortUrlService service;
    @Mock
    ShortUrlRepository repo;

    /**
     * Test the url can be saved and loaded from the service object
     * Requires mocking the underlying repository
     */
    @Test
    public void testAddAndRetrieveShortUrl() {
        Long originalId = 123L;
        String originalUrl = "https://www.google.co.uk";
        String shortUrlCode = IdentifierEncoder.encodeIdentifier(originalId);

        // mock the save call to return the shortUrl record but with the Id set
        when(repo.save(any(ShortUrl.class))).thenAnswer(new Answer<ShortUrl>(){
            @Override
            public ShortUrl answer(InvocationOnMock invocation){
                ShortUrl u = invocation.getArgument(0, ShortUrl.class);
                ReflectionTestUtils.setField(u, "id", originalId);
                return u;
            }});

        ShortUrl url = service.add(originalUrl);

        // now mock the repository find method to return the url record for this short url code
        when(repo.findByShortUrl(url.getShortUrl())).thenReturn(url);

        // assert
        ShortUrl retrievedUrl = service.find(url.getShortUrl());
        assertNotNull(retrievedUrl);
        assertEquals(retrievedUrl.getId(), url.getId());
        assertEquals(retrievedUrl.getShortUrl(), url.getShortUrl());
        assertEquals(retrievedUrl.getLongUrl(), url.getLongUrl());

        assertEquals(retrievedUrl.getId(), originalId);
        assertEquals(retrievedUrl.getLongUrl(), originalUrl);
        assertEquals(retrievedUrl.getShortUrl(), shortUrlCode);
    }

    /**
     * Test the url can be saved and loaded from the service object
     * Requires mocking the underlying repository
     */
    @Test
    public void testHandlesEmptyUrl() {
        assertThrows(IllegalArgumentException.class, () -> service.add(null));
        assertThrows(IllegalArgumentException.class, () -> service.add(""));
    }
}
