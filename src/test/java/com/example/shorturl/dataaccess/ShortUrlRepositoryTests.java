package com.example.shorturl.dataaccess;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShortUrlRepositoryTests {
    @Autowired
    ShortUrlRepository shortUrlRepository;

    @Test
    public void testCreateReadDelete() {
        ShortUrl url = new ShortUrl("https://www/google.co.uk");

        shortUrlRepository.save(url);

        Iterable<ShortUrl> urls = shortUrlRepository.findAll();
        Assertions.assertThat(urls).extracting(ShortUrl::getLongUrl).containsOnly("https://www/google.co.uk");

        shortUrlRepository.deleteAll();
        Assertions.assertThat(shortUrlRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindByShortUrl() {
        String assignedShortUrl = "abcdefg";
        ShortUrl url = new ShortUrl("https://www/google.co.uk");
        url.setShortUrl(assignedShortUrl);
        shortUrlRepository.save(url);

        ShortUrl retrievedUrl = shortUrlRepository.findByShortUrl(assignedShortUrl);
        assertEquals(retrievedUrl.getShortUrl(), url.getShortUrl());
        assertEquals(retrievedUrl.getLongUrl(), url.getLongUrl());
    }
}