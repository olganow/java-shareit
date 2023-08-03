package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantsTest {

    @Test
    void testRequestHeaderUserIdConstant() {
        String expectedUserId = "X-Sharer-User-Id";
        assertEquals(Constants.REQUEST_HEADER_USER_ID, expectedUserId);
    }

    @Test
    void testTimePatternConstant() {
        String expectedTimePattern = "yyyy-MM-dd HH:mm:ss";
        assertEquals(Constants.TIME_PATTERN, expectedTimePattern);
    }
}