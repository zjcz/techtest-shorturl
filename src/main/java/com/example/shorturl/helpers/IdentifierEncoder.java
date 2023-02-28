package com.example.shorturl.helpers;

/**
 * Utility class to encode unique identifiers
 */
public class IdentifierEncoder {
    /**
     * acceptable char list
     */
    private static final String CHAR_MAP = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Base number for converting our numeric identifier to a string
     */
    private static final long BASE = CHAR_MAP.length();

    /**
     * Encode a numeric identifier as a string.
     * This process involves converting the number to a base 62 string
     * (62 is the number of chars in our acceptable char list)
     * Taken from the examples at
     *  - https://stackoverflow.com/questions/742013/how-do-i-create-a-url-shortener
     *  - https://github.com/aniket91/DataStructures/blob/master/src/com/osfg/questions/ShortURLGenerator.java
     * @param identifier identifier to encode
     * @return encoded string
     */
    public static String encodeIdentifier(long identifier) {

        StringBuilder sb = new StringBuilder();

        while (identifier > 0) {
            Long x = identifier % BASE;
            sb.append(CHAR_MAP.charAt(x.intValue()));
            identifier = identifier / BASE;
        }

        return sb.reverse().toString();
    }
}
