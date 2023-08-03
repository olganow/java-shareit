package ru.practicum.shareit;


import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ShareItAppTest {

    @Test
    void testMainMethod() {
        String[] args = new String[0];
        ShareItApp.main(args);

        assertNotNull(SpringApplication.getShutdownHandlers());
    }
}