package com.example.shorturl.helpers;

import org.apache.commons.lang3.StringUtils;

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

    public static final int MINIMUM_ENCODED_LENGTH = 7;
    /**
     * Encode a numeric identifier as a string.
     * This process involves converting the number to a base 62 string
     * (62 is the number of chars in our acceptable char list)
     * Taken from the examples at
     *  - https://stackoverflow.com/questions/742013/how-do-i-create-a-url-shortener
     *  - https://github.com/aniket91/DataStructures/blob/master/src/com/osfg/questions/ShortURLGenerator.java
     * @param identifierToEncode identifier to encode
     * @return encoded string
     */
    public static String encodeIdentifier(final long identifierToEncode) {
        return encodeIdentifier(identifierToEncode, MINIMUM_ENCODED_LENGTH);
    }

    /**
     * Encode a numeric identifier as a string and pad to a minimum length
     * This process involves converting the number to a base 62 string
     * (62 is the number of chars in our acceptable char list)
     * Taken from the examples at
     *  - https://stackoverflow.com/questions/742013/how-do-i-create-a-url-shortener
     *  - https://github.com/aniket91/DataStructures/blob/master/src/com/osfg/questions/ShortURLGenerator.java
     * @param identifierToEncode identifier to encode
     * @param minLength minimum length of the encoded string to return.  String is left padded with 'a'.
     * @return encoded string
     */
    public static String encodeIdentifier(final long identifierToEncode, final int minLength) {
        long identifier = identifierToEncode;
        StringBuilder sb = new StringBuilder();

        while (identifier > 0) {
            Long x = identifier % BASE;
            sb.append(CHAR_MAP.charAt(x.intValue()));
            identifier = identifier / BASE;
        }

        // pad the string to ensure it is the minimum length.
        // Pad with character 'a' as that decodes to 0 if we ever implement decoding
        return StringUtils.leftPad(sb.reverse().toString(), minLength, "a");
    }
}
