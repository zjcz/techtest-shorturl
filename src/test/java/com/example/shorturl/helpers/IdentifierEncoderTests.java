package com.example.shorturl.helpers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IdentifierEncoderTests {
    @Test
    void IdentifierEncoder_isIdentifierEncoded() {
        String identifier = IdentifierEncoder.encodeIdentifier(123);
        assertNotNull(identifier);
        assertFalse(identifier.isEmpty());
        assertNotEquals("123", identifier);
    }

    @Test
    void IdentifierGenerator_isEncodingConsistent() {
        String identifier1 = IdentifierEncoder.encodeIdentifier(123);
        String identifier2 = IdentifierEncoder.encodeIdentifier(123);
        assertEquals(identifier1, identifier2);
    }
}
